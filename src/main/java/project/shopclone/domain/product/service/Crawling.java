package project.shopclone.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.product.entity.Brand;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.BrandRepository;
import project.shopclone.domain.product.repository.ProductImageRepository;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Crawling {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;

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

    // 0. 카테고리-브랜드명-브랜드별이미지 크롤링
    public void crawlingCateAndBrand(){
        WebDriver driver =  webDriver();

        // fender 전체 조회 페이지. 기본이 최신순인듯
        String url = "https://musicforce.co.kr/product/list.html?cate_no=1345";
        driver.get(url);

        // Guitar, Bass, Acoustic 까지만
        List<WebElement> categories = driver.findElements(By.cssSelector(".catedep1 .catedep2_li > a" ));
        for (WebElement category : categories) {
            String tmpUrl = category.getAttribute("href");

//            WebDriver driverTmp = new ChromeDriver(chromeOptions);
            WebDriver driverTmp = webDriver();
            driverTmp.get(tmpUrl);

            List<WebElement> names = driverTmp.findElements(By.cssSelector(".xans-product-menupackage > .xans-product-headcategory li"));
            String cate = names.get(1).getText();
            if (cate.equals("Recording")) return;

            String brand = names.get(2).getText();
            String cateNo = tmpUrl.split("=")[1];
            String brandImage = null;
            try{

                brandImage = driverTmp.findElement(By.cssSelector("#container > .protitle .banner > img")).getAttribute("src");;
            }catch (Exception e){
                System.out.println("e.getMessage() = " + e.getMessage());
            }

            brandRepository.save(Brand.builder()
                            .category(cate)
                            .brand(brand)
                            .cateNo(Integer.parseInt(cateNo))
                            .image(brandImage)
                            .build());
        }

    }

    // 1. 한 브랜드의 모든 상품번호 크롤링
    public void crawlingPages(Integer cateNo, String brand, String category){ // cateNo = 브랜드 번호
        WebDriver driver =  webDriver();

        // 전체 조회
        String url = "https://musicforce.co.kr/product/list.html?cate_no="+cateNo;
//        // fender 전체 조회 페이지. 기본이 최신순인듯
//        String url = "https://musicforce.co.kr/product/list.html?cate_no=1345";

        // 몇페이지까지 있는지 확인 (fender는 8페이지)
        driver.get(url);
        int lastPage = 1;
        try{
             lastPage = Integer.parseInt(driver.findElement(By.cssSelector(".posthan .last")).getAttribute("href").split("page=")[1]);
        }catch (Exception ignored){

        }

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

        // 상품 1개씩 크롤링
        for(Integer productNo : productNoList){
            // driver, 상품번호, 카테고리명, 브랜드명
            crawlingOne(driver, productNo, category, brand);
        }
    }

    // 2. 상품 1개 크롤링
    public void crawlingOne(WebDriver driver, Integer productNo, String cate, String brd) {
//        WebDriver driver =  webDriver();
        // gibson
//        String url = "https://musicforce.co.kr/product/detail.html?product_no=42133&cate_no=1348&display_group=1";
//        driver.get(url);

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
        if (priceStr.isEmpty()){
            return;
        }else if(priceStr.contains("예약중")){ // 예약중인 상품은 건너뜀
            return;
        }
        price = Integer.valueOf(priceStr);

        // PRS는 너무 많아서 저가는 제외
        if(brand.equals("PRS") && price<1000000) return;

        String originPriceStr = driver.findElement(By.cssSelector(".price_de")).getText().replaceAll(",", "").replace("원", "");
        String discountRateSr = driver.findElement(By.cssSelector(".percent > .cost")).getText();
        if (!originPriceStr.isEmpty()) { // 할인 적용 없는 상품인 경우
            originPrice = Integer.valueOf(originPriceStr);
            discountRate = Integer.valueOf(discountRateSr);
        }
        image = driver.findElement(By.cssSelector(".keyImg img"))
                .getAttribute("src");



        // table
        try{
            model = driver.findElement(By.cssSelector(".cont table .xl73")).getText().strip();
            List<WebElement> table_x171 = driver.findElements(By.cssSelector(".cont table .xl71")); // 클래스명 태그
            int i = 0;
            for (WebElement x171 : table_x171) {
                if (i==0){ // 원산지는 첫번째
                    country = x171.getText().strip();
                }else if (i==2){ // 출시연월은 세번째
                    releaseDate = Integer.parseInt(x171.getText().split("년")[0]);
                }
                i++;
            }
        }catch(Exception ignored){

        }

        // 상품 엔티티 저장
        Product product = productRepository.save(Product.builder()
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


        // 상세 이미지들 저장
//        List<WebElement> images = driver.findElements(By.cssSelector(".posthan .cont img"));
//        for (WebElement image : images){
//            productImageRepository.save(ProductImage.builder()
//                    .imageDetail(image.getAttribute("src"))
//                    .product(product)
//                    .build());
//        }
    }

    public void crawlingImages(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless"); // 브라우저 안뜨게 설정
        chromeOptions.addArguments("--no-sandbox");

        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        WebDriver driver = new ChromeDriver(chromeOptions); // (옵션전달)

        // fender 전체 조회 페이지. 기본이 최신순인듯
        String url = "https://musicforce.co.kr/product/detail.html?product_no=42133";
        driver.get(url);

//        List<WebElement> images = driver.findElements(By.cssSelector(".posthan .cont > div:first img" ));
        List<WebElement> divs = driver.findElements(By.cssSelector(".posthan .cont > div"));
        List<WebElement> images = divs.get(1).findElements(By.cssSelector("img"));
        for (WebElement image : images){
            String imgUrl = image.getAttribute("src");
            System.out.println("imgUrl = " + imgUrl);
        }

    }

    public WebDriver webDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless"); // 브라우저 안뜨게 설정
        chromeOptions.addArguments("--no-sandbox");

        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        WebDriver driver = new ChromeDriver(chromeOptions); // (옵션전달)
        return driver;
    }

    // 분류번호 작업
    @Transactional
    public void cateNumbering(Integer cateNo, String brand, String category) {
        List<Product> productList = productRepository.findAllByBrandAndCategory(brand, category);
        System.out.println("brand = " + brand + ", category = " + category + ", cateNo = " + cateNo + ", productList.size() = " + productList.size());
        for(Product product : productList){
            product.updateCateNo(cateNo);
//            System.out.println("product.getCateNo() = " + product.getCateNo());
        }
        System.out.println(productRepository.findAllByCateNo(cateNo).size());
    }
}
