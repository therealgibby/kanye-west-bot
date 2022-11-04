package com.bot.kanyewest.listener;

import com.bot.kanyewest.services.KanyeWestService;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class KanyeWestListener implements MessageCreateListener, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final KanyeWestService kanyeWestService;
    private final String PREFIX = "$";

    public KanyeWestListener(KanyeWestService kanyeWestService) {
        this.kanyeWestService = kanyeWestService;
    }

    public void init() {
        DiscordApi api = (DiscordApi) applicationContext.getBean("discordApi");
        api.addMessageCreateListener(this);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if(event.getMessageContent().equalsIgnoreCase(PREFIX + "channel") && event.getMessageAuthor().isServerAdmin()) {
            kanyeWestService.setChannel(event.getChannel().getId());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        init();
    }
}
