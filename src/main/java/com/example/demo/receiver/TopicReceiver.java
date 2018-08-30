package com.example.demo.receiver;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entities.Student;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yym
 * @create create by yym 2018/8/30 0030 10:22
 * @desc
 **/
@Component
public class TopicReceiver {

    @RabbitListener(queues = "topic.messages")
    public void process(Message message, Channel channel) throws IOException {
        try {
            Student student = JSONObject.parseObject(message.getBody(), Student.class);
            System.out.println("topicReceiver" + student);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
