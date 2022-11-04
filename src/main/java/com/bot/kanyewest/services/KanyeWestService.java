package com.bot.kanyewest.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class KanyeWestService implements ApplicationContextAware {

    private final Set<Long> textChannels;
    private ApplicationContext applicationContext;
    private DiscordApi discordApi;
    private final KanyeQuoteService kanyeQuoteService;

    public KanyeWestService(KanyeQuoteService kanyeQuoteService) {
        this.kanyeQuoteService = kanyeQuoteService;
        textChannels = new HashSet<>();
    }

    @Scheduled(fixedRate = 600000)
    public void sendMessage() {
        if(textChannels.isEmpty()) return;

        String response = kanyeQuoteService.createResponse();
        JsonElement element = JsonParser.parseString(response);
        JsonObject jsonObject = element.getAsJsonObject();
        String quote = jsonObject.get("quote").getAsString();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Kanye Quote")
                .setColor(Color.ORANGE)
                .setDescription(quote)
                .setThumbnail("https://preview.redd.it/y2aoxuw3l6s71.jpg?auto=webp&s=2e9b2a965ae3e4ac54213ccc468a128c521a79c6");

        for(long id : textChannels) {
            if(discordApi.getTextChannelById(id).isPresent()) {
                discordApi.getTextChannelById(id).get().sendMessage(embed);
            }
        }
    }

    public void setChannel(long textChannelId) {
        if(textChannels.contains(textChannelId)) return;

        textChannels.add(textChannelId);

        /*
        Removes a channel id if the incoming id is from the same server
         */
        textChannels.removeIf(id -> (discordApi.getServerChannelById(id).get().getServer().getId() == discordApi.getServerChannelById(textChannelId).get().getServer().getId()) && id != textChannelId);

        if(discordApi.getTextChannelById(textChannelId).isPresent())
            discordApi.getTextChannelById(textChannelId).get().sendMessage("This is now Kanye's channel");
    }

    private void setDiscordApi() {
        this.discordApi = (DiscordApi) applicationContext.getBean("discordApi");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        setDiscordApi();
    }
}
