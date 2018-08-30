package com.example.demo.receiver;

import com.example.demo.common.ReceiverUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yym
 * @create create by yym 2018/8/30 0030 10:32
 * @desc
 **/
@Component
public class DirectReceiver {

    @RabbitListener(queues = "directQueue.a")
    public void processA(Message message, Channel channel) throws IOException {
        ReceiverUtil.handle(message, channel, "ReceiverA");
    }

    @RabbitListener(queues = "directQueue.b")
    public void processB(Message message, Channel channel) throws IOException {
        ReceiverUtil.handle(message, channel, "ReceiverB");
    }


}
