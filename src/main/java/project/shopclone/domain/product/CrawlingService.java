package project.shopclone.domain.product;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CrawlingService {
    private final ProductRepository productRepository;

    private Integer no;
    private String name;
    private Integer price;
    private Integer originPrice;
    private Integer discountRate;
    private Integer releaseDate;
    private String model;
    private String country;
    private String category;    // 대분류 g1
    private String brand;       // 중분류 fender
    private String series;       // 소분류
    private String image;
    private String description;

    // 1. 한 브랜드의 모든 상품번호 크롤링 (현재는 fender 브랜드만 진행)
    public void crawlingPages(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless"); // 브라우저 안뜨게 설정
        chromeOptions.addArguments("--no-sandbox");

        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        WebDriver driver = new ChromeDriver(chromeOptions); // (옵션전달)

        // fender 전체 조회 페이지. 기본이 최신순인듯
        String url = "https://musicforce.co.kr/product/list.html?cate_no=1345";

        // 몇페이지까지 있는지 확인 (fender는 8페이지)
        driver.get(url);
        int lastPage = Integer.parseInt(driver.findElement(By.cssSelector(".posthan .last")).getAttribute("href").split("page=")[1]);

        // 모든 페이지에서 상품번호 전부 가져오기
        List<Integer> productNoList = new ArrayList<>();
        for (int i = 1; i <= lastPage ; i++) {
            driver.get(url+"&page="+i);

            // .prdList 클래스의 id가 상품번호
            List<WebElement> productList = driver.findElements(By.cssSelector(".prdList .item" ));
            for (WebElement product : productList) {
                productNoList.add(Integer.parseInt(product.getAttribute("id").split("_")[1]));
            }
        }

        // 상품번호, 개수 확인
//        System.out.println(productNoList);
//        System.out.println(productNoList.size());

        // 상품 1개 크롤링
        for(Integer productNo : productNoList){
            crawlingOne(driver, productNo, "Guitars 1", "Fender");
        }
    }

    // 2. 상품 1개 크롤링
    public void crawlingOne(WebDriver driver, Integer productNo, String cate, String brd) {
        // fender 할인가O
//        String url = "https://musicforce.co.kr/product/detail.html?product_no=42033&cate_no=1246&display_group=1";
        // prs 할인가X
//        String url = "https://musicforce.co.kr/product/detail.html?product_no=41823&cate_no=1348&display_group=1";
        String url = "https://musicforce.co.kr/product/detail.html?product_no="+productNo;
        driver.get(url);

        // 상위에서 할당
        no = productNo;
        category = cate;
        brand = brd;

        // heading area
        name = driver.findElement(By.cssSelector(".headingArea h2")).getText();
        if (name.contains("]")){  // 상품명에 이벤트 멘트인 대괄호(]) 앞은 제거하기
            name = name.split("]")[1];
        }
        name = name.strip();
        String priceStr = driver.findElement(By.cssSelector(".quantity_price")).getText()
                .replaceAll(",", "").replace("원", "");
        if (priceStr.contains("예약중")){ // 예약중인 상품은 건너뜀
            return;
        }
        price = Integer.valueOf(priceStr);

        String originPriceStr = driver.findElement(By.cssSelector(".price_de")).getText().replaceAll(",", "").replace("원", "");
        String discountRateSr = driver.findElement(By.cssSelector(".percent > .cost")).getText();
        if (!originPriceStr.isEmpty()) { // 할인 적용 없는 상품인 경우
            originPrice = Integer.valueOf(originPriceStr);
            discountRate = Integer.valueOf(discountRateSr);
        }
        image = driver.findElement(By.cssSelector(".keyImg img"))
                .getAttribute("src");

        // table
        model = driver.findElement(By.cssSelector(".cont table .xl73")).getText().strip();
        List<WebElement> table_x171 = driver.findElements(By.cssSelector(".cont table .xl71")); // 클래스명 태그
        int i = 0;
        for (WebElement x171 : table_x171) {
            if (i==0){ // 원산지는 첫번째
                country = x171.getText();
            }else if (i==2){ // 출시연월은 세번째
                releaseDate = Integer.parseInt(x171.getText().split("년")[0]);
            }
            i++;
        }

        // 확인용
//        System.out.println(no);
//        System.out.println(category);
//        System.out.println(brand);
//        System.out.println(name);
//        System.out.println(price);
//        System.out.println(originPrice);
//        System.out.println(discountRate);
//        System.out.println(image);
//        System.out.println(model_name);
//        System.out.println(country);
//        System.out.println(releaseDate);

        // 엔티티 저장
        productRepository.save(Product.builder()
                .no(no)
                .category(category)
                .brand(brand)
                .name(name)
                .price(price)
                .originPrice(originPrice)
                .discountRate(discountRate)
                .image(image)
                .model(model)
                .country(country)
                .releaseDate(releaseDate)
                .build());

    }

}
