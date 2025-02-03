package ru.job4j.bmb.recommendation;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class RecommendationEngine implements ContentProvider {
    @PostConstruct
    public void init() {
        System.out.println("Bean RecommendationEngine is going through init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean RecommendationEngine will be destroyed now.");
    }
}
