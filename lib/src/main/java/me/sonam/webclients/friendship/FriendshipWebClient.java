package me.sonam.webclients.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class FriendshipWebClient {
    private static final Logger LOG = LoggerFactory.getLogger(FriendshipWebClient.class);

    private final String endpoint;
    private final WebClient.Builder webClientBuilder;
    private final String requestFriendship = "/friendships/request/{userId}";

    public FriendshipWebClient(WebClient.Builder webClientBuilder, String endpoint) {
        this.webClientBuilder = webClientBuilder;
        this.endpoint = endpoint;
    }
    /**
     * this will call request friendship endpoint.  It will request friendship with logged-in user with another user
     *  identified with their userId.*
     * @param userId - the other user's userId
     * @return
     */
    public Mono<SeUserFriend> requestFriendshipWithUserId(UUID userId) {
        StringBuilder stringBuilder = new StringBuilder(endpoint).append(requestFriendship);
        String endpoint = stringBuilder.toString().replace("{userId}", userId.toString());

        LOG.info("create Account record with http call on endpoint: {}", endpoint);
        WebClient.ResponseSpec spec = webClientBuilder.build().post().uri(endpoint).retrieve();

        return spec.bodyToMono(SeUserFriend.class).doOnNext(seUserFriend -> {
                    LOG.info("freindship created with response: {}", seUserFriend);
                })
                .onErrorResume(throwable -> {
                    LOG.debug("exception occurred when requesting friendship with endpoint", throwable);
                    LOG.error("request friendship account rest call failed: {}", throwable.getMessage());
                    if (throwable instanceof WebClientResponseException webClientResponseException) {
                        LOG.error("error body contains: {}", webClientResponseException.getResponseBodyAsString());
                        return Mono.error(new FriendshipException("request friendship call failed with error: " +
                                                webClientResponseException.getResponseBodyAsString()));
                    }
                    else {
                        return Mono.error(new FriendshipException("request friendship call failed with error: " +throwable.getMessage()));
                    }
                });
    }

}
