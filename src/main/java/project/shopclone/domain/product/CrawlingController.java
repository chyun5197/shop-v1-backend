package project.shopclone.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    private final ProductService productService;

    @GetMapping("/crawling")
    public List<CrawlingResponse> crawl() {
        return productService.crawling();
    }
}
