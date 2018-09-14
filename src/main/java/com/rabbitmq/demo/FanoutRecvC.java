package com.rabbitmq.demo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "fanout.C")
public class FanoutRecvC {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("ZM fanout Receiver C  : " + hello);
    }
}
