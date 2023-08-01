package kr.co.finote.backend.src;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public boolean getHealthCheck() {
        return true;
    }
}