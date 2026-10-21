
package ratelimiter;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FixedWindowRateLimiter {

    private final int maxRequests;
    private final Duration windowDuration;
    private final Clock clock;

    private final Map<String, WindowState> clients = new HashMap<>();

    public FixedWindowRateLimiter(
            int maxRequests,
            Duration windowDuration,
            Clock clock) {

        if (maxRequests <= 0) {
            throw new IllegalArgumentException(
                    "maxRequests must be positive");
        }

        if (windowDuration == null
                || windowDuration.isZero()
                || windowDuration.isNegative()) {
            throw new IllegalArgumentException(
                    "windowDuration must be positive");
        }

        if (clock == null) {
            throw new IllegalArgumentException(
                    "clock must not be null");
        }

        this.maxRequests = maxRequests;
        this.windowDuration = windowDuration;
        this.clock = clock;
    }

    public synchronized boolean allowRequest(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException(
                    "clientId must not be blank");
        }

        Instant now = clock.instant();

        WindowState state = clients.get(clientId);

        if (state == null) {
            state = new WindowState(now);
            clients.put(clientId, state);
        }

        Duration elapsed = Duration.between(
                state.getWindowStart(), now);

        if (elapsed.compareTo(windowDuration) >= 0) {
            state.reset(now);
        }

        if (state.getRequestCount() >= maxRequests) {
            return false;
        }

        state.incrementRequestCount();
        return true;
    }
}
