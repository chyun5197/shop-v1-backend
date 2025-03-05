package project.shopclone.global.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class Test {
    // 로드밸런싱으로 서버가 바뀌면 응답도 바뀌도록
    public static final UUID randomUUID = UUID.randomUUID();

    @GetMapping("/test/balancer")
    public ResponseEntity<UUID> test() {
        return ResponseEntity.ok(randomUUID);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Success Health Check";
    }
}
