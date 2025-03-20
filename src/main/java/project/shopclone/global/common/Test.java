package project.shopclone.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@Tag(name = "테스트용 API")
@RestController
public class Test {
    // 로드밸런싱으로 서버가 바뀌면 응답도 바뀌도록
    public static final UUID randomUUID = UUID.randomUUID();

    @GetMapping("/test/balancer")
    public ResponseEntity<UUID> test() {
        return ResponseEntity.ok(randomUUID);
    }

    @GetMapping("/test/delay")
    public String testDelay() {
        int[] arr = new int[10000000]; // 천만 0.15초(로컬 응답시간)
//        int[] arr = new int[100000000]; // 일억 1.5초
        Random rd = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(10000);
        }

        return "fin";
    }

    @Operation(summary = "헬스 체크")
    @GetMapping("/health")
    public String healthCheck() {
        return "Success Health Check";
    }
}
