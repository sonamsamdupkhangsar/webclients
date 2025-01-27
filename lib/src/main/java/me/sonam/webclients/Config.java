package me.sonam.webclients;

import me.sonam.webclients.friendship.FriendshipWebClient;
import me.sonam.webclients.notification.NotificationWebClient;
import me.sonam.webclients.user.UserWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    //default to empty string if not defined
    @Value("${friendshipEndpoint:}")
    private String friendshipEndpoint;

    @Value("${notificationEndpoint:}")
    private String notificationEndpoint;

    @Value("${userEndpoint:}")
    private String userEndpoint;

    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {
        LOG.info("returning load balanced webclient part");
        return WebClient.builder();
    }

    @Bean
    public FriendshipWebClient friendshipWebClient() {
        return new FriendshipWebClient(webClientBuilder(), friendshipEndpoint);
    }

    @Bean
    public NotificationWebClient notificationWebClient() {
        return new NotificationWebClient(webClientBuilder(), notificationEndpoint);
    }

    @Bean
    public UserWebClient userWebClient() {
        return new UserWebClient(webClientBuilder(), userEndpoint);
    }

}
