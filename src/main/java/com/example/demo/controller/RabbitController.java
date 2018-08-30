package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entities.Student;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yym
 * @create create by yym 2018/8/29 0029 14:38
 * @desc
 **/
@RestController
public class RabbitController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/topic")
    public Student topic(@RequestBody Student student) {
        System.out.println("topic");
        rabbitTemplate.convertAndSend("exchange", "topic.message", JSONObject.toJSONBytes(student));
        return student;
    }

    @RequestMapping("/fanout")
    public Student fanout(@RequestBody Student student) {
        System.out.println("fanout");
        rabbitTemplate.convertAndSend("fanoutExchange", "topic.message", JSONObject.toJSONBytes(student));
        return student;
    }

    @RequestMapping("/headers")
    public Student headers(@RequestBody Student student) {
        System.out.println("headers");
        Map<String, Object> headers = new HashMap<>();
        headers.put("key1", "B");
        headers.put("key2", "value2");
        MessageProperties properties = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("key1","B").setHeader("key2", "value2").build();
        Message message = MessageBuilder.withBody(JSONObject.toJSONBytes(student)).andProperties(properties).build();
        rabbitTemplate.convertAndSend("headersExchange", "topic.message", message);
        return student;
    }

    @RequestMapping("/directA")
    public Student directA(@RequestBody Student student) {
        System.out.println("direct");
        rabbitTemplate.convertAndSend("directExchange", "directQueue.a", JSONObject.toJSONBytes(student));
        return student;
    }

    @RequestMapping("/directB")
    public Student directB(@RequestBody Student student) {
        System.out.println("direct");
        rabbitTemplate.convertAndSend("directExchange", "directQueue.b", JSONObject.toJSONBytes(student));
        return student;
    }
}
