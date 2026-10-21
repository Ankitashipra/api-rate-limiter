package ratelimiter;

import java.time.Clock;
import java.time.Duration;

public class Main {

    public static void main(String[] args) {
        FixedWindowRateLimiter limiter =
                new FixedWindowRateLimiter(
                        3,
                        Duration.ofSeconds(10),
                        Clock.systemUTC()
                );

        sendRequest(limiter, "Client-A");
        sendRequest(limiter, "Client-A");
        sendRequest(limiter, "Client-A");
        sendRequest(limiter, "Client-A");

        System.out.println("--- Another client ---");

        sendRequest(limiter, "Client-B");
        sendRequest(limiter, "Client-B");

        System.out.println("--- Client A tries again ---");

        sendRequest(limiter, "Client-A");
    }

    private static void sendRequest(
            FixedWindowRateLimiter limiter,
            String clientId) {

        boolean allowed = limiter.allowRequest(clientId);

        System.out.println(
                clientId + " -> "
                + (allowed ? "ACCEPTED" : "REJECTED"));
    }
}
