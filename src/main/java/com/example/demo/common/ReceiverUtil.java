package com.example.demo.common;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entities.Student;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author yym
 * @create create by yym 2018/8/30 0030 10:39
 * @desc
 **/
public class ReceiverUtil {

    public static void handle(Message message, Channel channel, String msg) throws IOException {
        try {
            Student student = JSONObject.parseObject(message.getBody(), Student.class);
            System.out.println(msg + " : " + student);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
