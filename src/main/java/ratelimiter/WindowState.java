package ratelimiter;

import java.time.Instant;

public class WindowState {

    private Instant windowStart;
    private int requestCount;

    public WindowState(Instant windowStart) {
        this.windowStart = windowStart;
        this.requestCount = 0;
    }

    public Instant getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Instant windowStart) {
        this.windowStart = windowStart;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        requestCount++;
    }

    public void reset(Instant newWindowStart) {
        this.windowStart = newWindowStart;
        this.requestCount = 0;
    }
}
