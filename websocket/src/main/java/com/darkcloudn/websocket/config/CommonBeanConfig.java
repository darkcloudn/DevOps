package com.darkcloudn.websocket.config;

import com.google.gson.Gson;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class CommonBeanConfig {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;


    @Bean
    public Gson createGsonInstance() {
        return new Gson();
    }

//    @Bean
//    public JavaMailSender getMailSender() {
//
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(mailHost);
//        mailSender.setPort(Integer.valueOf(mailPort));
//        mailSender.setUsername(mailUsername);
//        mailSender.setPassword(mailPassword);
//
////        Properties javaMailProperties = new Properties();
////        javaMailProperties.put(MailConstant.MAIL_SMTP_STARTTLS_ENABLE, "true");
////        javaMailProperties.put(MailConstant.MAIL_TRANSPORT_PROTOCOL, "smtps");
////        javaMailProperties.put(MailConstant.MAIL_SMTP_AUTH, "true");
////        javaMailProperties.put(MailConstant.MAIL_DEBUG, "true");
////        mailSender.setJavaMailProperties(javaMailProperties);
//        return mailSender;
//    }

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setDefaultEncoding("UTF-8");
//        mailSender.setHost("mail.mbageaslife.com");
//        mailSender.setPort(465);
//
//        mailSender.setUsername("viet.td@mbageaslife.com");
//        mailSender.setPassword("password");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtps");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }

    @Bean("threadPoolTaskExecutor")
    public Executor getTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Async-");
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }


}
