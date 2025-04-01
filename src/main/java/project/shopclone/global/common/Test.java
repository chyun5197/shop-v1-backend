package project.shopclone.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@Tag(name = "테스트용 API")
@RequiredArgsConstructor
@RestController
public class Test {
    private final HttpSession httpSession;

    // 로드밸런싱으로 서버가 바뀌면 응답도 바뀌도록
    public static final UUID randomUUID = UUID.randomUUID();

    @PostMapping("/test/session/{value}")
    public void putValue(@PathVariable int value) {
        httpSession.setAttribute("value", value);
    }

    @GetMapping("/test/session")
    public ResponseEntity<Integer> getValue() {
        Integer value = (Integer) httpSession.getAttribute("value");
        return ResponseEntity.ok(value);
    }

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
