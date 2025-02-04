package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MoodService implements BeanNameAware {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        System.out.println("Bean MoodService is going through init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean MoodService will be destroyed now.");
    }

    @Override
    public void setBeanName(String name) {
        String[] beanNames = applicationContext.getBeanNamesForType(MoodService.class);
        System.out.println("Имя этого бина: " + beanNames[0]);
    }
}
