package project.shopclone.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    private final CrawlingService productService;

    // DB에 저장하는 처음 한번만 실행하고 끝이기에 코드 실행시간이 빠를필요 없음
    // => 실행 오래 걸리더라도 정확하게끔, 나중에 코드 확장하기 편하게 알아보기 쉽도록 정리
    @GetMapping("/crawling")
    public String crawl() {
        productService.crawlingPages();
        return "성공";
    }

}
