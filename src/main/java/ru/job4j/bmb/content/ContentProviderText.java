package ru.job4j.bmb.content;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("selected")
public class ContentProviderText implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setText("Text");
        return content;
    }
}
