package ru.antowka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAutoConfiguration
@ComponentScan("ru.antowka")
public class AppInit {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppInit.class, args);
    }
}
