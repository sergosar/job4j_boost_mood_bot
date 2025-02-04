package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

@Service
public class BotCommandHandler implements BeanNameAware {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        System.out.println("Bean BotCommandHandler is going through init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean BotCommandHandler will be destroyed now.");
    }

    void receive(Content content) {
        System.out.println(content);
    }

    @Override
    public void setBeanName(String name) {
        String[] beanNames = applicationContext.getBeanNamesForType(BotCommandHandler.class);
        System.out.println("Имя этого бина: " + beanNames[0]);
    }
}