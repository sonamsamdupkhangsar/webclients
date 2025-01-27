package me.sonam.webclients.notification;

import me.sonam.webclients.friendship.FriendNotification;
import me.sonam.webclients.friendship.SeUserFriend;
import me.sonam.webclients.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class NotificationWebClient {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebClient.class);

    private final WebClient.Builder webClientBuilder;

    private final String notificationsPath = "/notifications";
    private String host;

    public NotificationWebClient(WebClient.Builder webClientBuilder,
                                 String host) {
        this.webClientBuilder = webClientBuilder;
        this.host = host;
    }

    public Mono<String> sendFriendNotification(User user, SeUserFriend seUserFriend, FriendNotification.Event event) {
        final String endpoint = host + notificationsPath;
        LOG.info("send friend notification '{}' to  endpoint: {}", event.name(), host + notificationsPath);

        WebClient.ResponseSpec responseSpec = webClientBuilder.build().post().uri(endpoint)
                .retrieve();

        return responseSpec.bodyToMono(String.class)
                .flatMap(string -> {
                    LOG.info("got notification response {}", string);
                    return Mono.just(string);
                })
                .thenReturn("notification has been sent").onErrorResume(throwable -> {
            LOG.error("failed to call notification endpoint '{}' with error: {}",
                    endpoint, throwable.getMessage());
            return Mono.error(new RuntimeException("notification call failed, error: " + throwable.getMessage()));
        });
    }



}