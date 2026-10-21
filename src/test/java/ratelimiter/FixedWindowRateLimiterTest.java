
package ratelimiter;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class FixedWindowRateLimiterTest {

    @Test
    void allowsRequestsUpToLimitAndRejectsExtraRequests() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC"));

        FixedWindowRateLimiter limiter =
                new FixedWindowRateLimiter(
                        3, Duration.ofSeconds(10), clock);

        assertTrue(limiter.allowRequest("client-A"));
        assertTrue(limiter.allowRequest("client-A"));
        assertTrue(limiter.allowRequest("client-A"));

        assertFalse(limiter.allowRequest("client-A"));
    }

    @Test
    void clientsHaveIndependentLimits() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC"));

        FixedWindowRateLimiter limiter =
                new FixedWindowRateLimiter(
                        2, Duration.ofSeconds(10), clock);

        assertTrue(limiter.allowRequest("client-A"));
        assertTrue(limiter.allowRequest("client-A"));
        assertFalse(limiter.allowRequest("client-A"));

        // A different client has its own counter.
        assertTrue(limiter.allowRequest("client-B"));
    }

    @Test
    void allowsRequestsAgainAfterWindowExpires() {
        MutableClock clock = new MutableClock(
                Instant.parse("2026-01-01T10:00:00Z"));

        FixedWindowRateLimiter limiter =
                new FixedWindowRateLimiter(
                        2, Duration.ofSeconds(10), clock);

        assertTrue(limiter.allowRequest("client-A"));
        assertTrue(limiter.allowRequest("client-A"));
        assertFalse(limiter.allowRequest("client-A"));

        // Advance time to the exact 10-second boundary.
        clock.advance(Duration.ofSeconds(10));

        assertTrue(limiter.allowRequest("client-A"));
    }

    @Test
    void rejectsBlankClientId() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC"));

        FixedWindowRateLimiter limiter =
                new FixedWindowRateLimiter(
                        3, Duration.ofSeconds(10), clock);

        assertThrows(
                IllegalArgumentException.class,
                () -> limiter.allowRequest(" "));
    }

    // Test-only clock whose time we can advance manually.
    private static class MutableClock extends Clock {
        private Instant currentInstant;

        MutableClock(Instant initialInstant) {
            this.currentInstant = initialInstant;
        }

        void advance(Duration duration) {
            currentInstant = currentInstant.plus(duration);
        }

        @Override
        public Instant instant() {
            return currentInstant;
        }

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }
}
