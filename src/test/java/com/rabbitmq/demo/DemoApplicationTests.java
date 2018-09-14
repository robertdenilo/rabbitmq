package com.rabbitmq.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private Sender sender;

    @Autowired
    private OneToMSender oneToMSender;

    @Autowired
    private OneToMSender2 oneToMSender2;
    @Autowired
    private FanoutSender  fanoutSender;
    @Test
    public void contextLoads() throws Exception {
        //1 to 1
        //sender.send();
        //1 to M
//        for (int i=0;i<100;i++){
//            oneToMSender.send(i);
//        }
    }

//    @Test
//    public void manyToMany() throws Exception {
//        for (int i=0;i<100;i++){
//            oneToMSender.send(i);
//            oneToMSender2.send(i);
//        }
//    }
    @Test
    public void funoutSend() {
        fanoutSender.send();
    }
}
