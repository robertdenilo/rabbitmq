package com.rabbitmq.demo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@RabbitListener(queues = "hello-mq")
public class Receiver {
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    public void receiveMessage(String message) {
//        System.out.println("Received <" + message + ">");
//        latch.countDown();
//
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }


    @RabbitHandler
    public void process(String hello) {
        System.out.println("ZM Receiver  : " + hello);
    }
}
