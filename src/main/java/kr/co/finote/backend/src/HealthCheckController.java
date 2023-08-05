package kr.co.finote.backend.src;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthCheckController {

    @Operation(summary = "Server Health Check")
    @GetMapping("/health-check")
    public boolean getHealthCheck() {
        return true;
    }
}
