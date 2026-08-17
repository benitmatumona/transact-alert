package com.transactalert.notification;
import org.springframework.boot.SpringApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJms
public class NotificationServiceApplication{
    public static void main(String[] args){
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
