package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AchievementService implements BeanNameAware {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        System.out.println("Bean AchievementService is going through init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean AchievementService will be destroyed now.");
    }

    @Override
    public void setBeanName(String name) {
        String[] beanNames = applicationContext.getBeanNamesForType(AchievementService.class);
        System.out.println("Имя этого бина: " + beanNames[0]);
    }
}
