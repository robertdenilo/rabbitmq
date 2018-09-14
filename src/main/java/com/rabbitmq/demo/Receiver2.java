package com.rabbitmq.demo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(queues = "hello-mq")
public class Receiver2 {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("ZM2 Receiver  : " + hello);
    }
}
