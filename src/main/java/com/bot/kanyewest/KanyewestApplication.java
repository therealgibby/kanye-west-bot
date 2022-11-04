package com.bot.kanyewest;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KanyewestApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanyewestApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(value = "discord-api")
	public DiscordApi discordApi() {
		DiscordApi api = new DiscordApiBuilder()
				.setToken("token")
				.setAllNonPrivilegedIntents().login().join();
		api.updateActivity(ActivityType.WATCHING, "Kim and Pete");

		return api;
	}
}
