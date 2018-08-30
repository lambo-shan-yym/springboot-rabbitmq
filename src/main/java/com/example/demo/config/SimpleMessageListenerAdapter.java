package com.example.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entities.Student;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author yym
 * @create create by yym 2018/8/29 0029 14:28
 * @desc
 **/
@Component
public class SimpleMessageListenerAdapter extends MessageListenerAdapter{
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {

            Student student=JSONObject.parseObject(message.getBody(), Student.class);
            System.out.println(student);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println(e.getMessage());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
    }

}
