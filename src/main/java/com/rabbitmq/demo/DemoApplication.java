package com.rabbitmq.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {
    private static final String QUEUE = "spring.publisher.sample";
    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context  = SpringApplication.run(DemoApplication.class, args);

        context.getBean(DemoApplication.class).runDemo();
        context.close();
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final CountDownLatch listenLatch = new CountDownLatch(1);

    private final CountDownLatch confirmLatch = new CountDownLatch(1);

    private final CountDownLatch returnLatch = new CountDownLatch(1);

    private void runDemo() throws Exception {
        setupCallbacks();
        // send a message to the default exchange to be routed to the queue
        this.rabbitTemplate.convertAndSend("", QUEUE, "foo", new CorrelationData("Correlation for message 1"));
        if (this.confirmLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Confirm received");
        }
        else {
            System.out.println("Confirm NOT received");
        }
        if (this.listenLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Message received by listener");
        }
        else {
            System.out.println("Message NOT received by listener");
        }
        // send a message to the default exchange to be routed to a non-existent queue
        this.rabbitTemplate.convertAndSend("", QUEUE + QUEUE, "bar", message -> {
            System.out.println("Message after conversion: " + message);
            return message;
        });
        if (this.returnLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Return received");
        }
        else {
            System.out.println("Return NOT received");
        }
    }

    private void setupCallbacks() {
        /*
         * Confirms/returns enabled in application.properties - add the callbacks here.
         */
        this.rabbitTemplate.setConfirmCallback((correlation, ack, reason) -> {
            if (correlation != null) {
                System.out.println("Received " + (ack ? " ack " : " nack ") + "for correlation: " + correlation);
            }
            this.confirmLatch.countDown();
        });
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Returned: " + message + "\nreplyCode: " + replyCode
                    + "\nreplyText: " + replyText + "\nexchange/rk: " + exchange + "/" + routingKey);
            this.returnLatch.countDown();
        });
        /*
         * Replace the correlation data with one containing the converted message in case
         * we want to resend it after a nack.
         */
        this.rabbitTemplate.setCorrelationDataPostProcessor((message, correlationData) ->
                new CompleteMessageCorrelationData(correlationData != null ? correlationData.getId() : null, message));
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false, false, true);
    }

    @RabbitListener(queues = QUEUE)
    public void listen(String in) {
        System.out.println("Listener received: " + in);
        this.listenLatch.countDown();
    }

    static class CompleteMessageCorrelationData extends CorrelationData {

        private final Message message;

        CompleteMessageCorrelationData(String id, Message message) {
            super(id);
            this.message = message;
        }

        public Message getMessage() {
            return this.message;
        }

        @Override
        public String toString() {
            return "CompleteMessageCorrelationData [id=" + getId() + ", message=" + this.message + "]";
        }

    }
}
