package com.server;

public class RequestResponseStats {
    private int totalRequests;
    private int totalResponses;

    public int getTotalRequests() {
        return totalRequests;
    }

    public void incrementTotalRequests() {
        this.totalRequests++;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    public void incrementTotalResponses() {
        this.totalResponses++;
    }
}
