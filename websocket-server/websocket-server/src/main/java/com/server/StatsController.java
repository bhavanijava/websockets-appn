package com.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final RequestResponseStats stats;

    @Autowired
    public StatsController(RequestResponseStats stats) {
        this.stats = stats;
    }

    @GetMapping("/requests")
    public int getTotalRequests() {
        return stats.getTotalRequests();
    }

    @GetMapping("/responses")
    public int getTotalResponses() {
        return stats.getTotalResponses();
    }
}
