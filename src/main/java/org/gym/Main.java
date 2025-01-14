package org.gym;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Configuration
@ComponentScan("org.gym")
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        context.registerShutdownHook();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                context.close();
            }});
    }
}
