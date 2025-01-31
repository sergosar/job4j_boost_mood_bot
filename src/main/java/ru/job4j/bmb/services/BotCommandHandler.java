package ru.job4j.bmb.services;

import ru.job4j.bmb.content.Content;

public class BotCommandHandler {
    void receive(Content content) {
        System.out.println(content);
    }
}