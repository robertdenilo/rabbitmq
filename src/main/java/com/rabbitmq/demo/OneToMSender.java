package com.rabbitmq.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OneToMSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(int i) {
        String context = "hello "+ String.valueOf(i) + new Date();
        System.out.println("ZM 1toM Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello-mq", context);
    }
}
