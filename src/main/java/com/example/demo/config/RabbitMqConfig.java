package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yym
 * @create create by yym 2018/8/29 0029 14:00
 * @desc
 **/
@Configuration
public class RabbitMqConfig {

    @Value("${mq.host}")
    private String host;
    @Value("${mq.port}")
    private Integer port;
    @Value("${mq.username}")
    private String username;
    @Value("${mq.password}")
    private String password;
    @Value("${mq.vhost}")
    private String virtualHost;

    @Autowired
    SimpleMessageListenerAdapter simpleMessageListenerAdapter;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(virtualHost.trim());
        connectionFactory.setHost(host.trim());
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username.trim());
        connectionFactory.setPassword(password.trim());
        //connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;

    }


    //topic exchange
    @Bean
    public Queue Queue() {
        return new Queue("topic.message");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("exchange");
    }


    @Bean
    Binding binding() {
        return BindingBuilder.bind(Queue()).to(topicExchange()).with("topic.message");
    }


    @Bean
    public Queue queueMessages() {
        return new Queue("topic.messages");
    }

    @Bean
    Binding bindingExchangeMessages() {
        return BindingBuilder.bind(queueMessages()).to(topicExchange()).with("topic.#");
    }

    // fanout exchange

    @Bean
    public Queue fanoutQueueA() {
        return new Queue("fanout.A");
    }


    @Bean
    public Queue fanoutQueueB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue fanoutQueueC() {
        return new Queue("fanout.C");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue fanoutQueueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueueA).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue fanoutQueueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueueB).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue fanoutQueueC, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueueC).to(fanoutExchange);
    }



    // headers exchange

    @Bean
    public Queue headersQueueA() {
        return new Queue("headers.A");
    }

    @Bean
    public Queue headersQueueB() {
        return new Queue("headers.B");
    }

    @Bean
    public Queue headersQueueC() {
        return new Queue("headers.C");
    }

    @Bean
    HeadersExchange headersExchange() {
        return new HeadersExchange("headersExchange");
    }

    @Bean
    Binding bindingHeadersExchangeA(Queue headersQueueA, HeadersExchange headersExchange) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("key2", "value2");
        return BindingBuilder.bind(headersQueueA).to(headersExchange).whereAny(headers).match();
    }

    @Bean
    Binding bindingHeadersExchangeB(Queue headersQueueB, HeadersExchange headersExchange) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("key1", "B");
        headers.put("key2", "value2");
        return BindingBuilder.bind(headersQueueB).to(headersExchange).whereAll(headers).match();
    }

    @Bean
    Binding bindingHeadersExchangeC(Queue headersQueueC, HeadersExchange headersExchange) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("key2", "value1");
        return BindingBuilder.bind(headersQueueC).to(headersExchange).whereAny(headers).match();
    }

    // direct exchange

    @Bean
    public Queue directQueueA() {
        return new Queue("directQueue.a");
    }

    @Bean
    public Queue directQueueB() {
        return new Queue("directQueue.b");
    }


    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    @Bean
    Binding bindingDirectExchangeA(Queue directQueueA, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueA).to(directExchange).with("directQueue.a");
    }
    @Bean
    Binding bindingDirectExchangeB(Queue directQueueB, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueB).to(directExchange).with("directQueue.b");
    }


    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory());
        rabbitAdmin.declareQueue(Queue());
        return rabbitAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        //手动ACK
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(simpleMessageListenerAdapter);
        //container.setMessageConverter(new Jackson2JsonMessageConverter());
        container.setQueueNames("topic.message");
        return container;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory());
        // rabbitTemplate.setQueue("topic.message");
        rabbitTemplate.setChannelTransacted(true);
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
