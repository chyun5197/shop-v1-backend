package project.shopclone.domain.wish.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.repository.MemberRepository;
import project.shopclone.domain.product.dto.response.ProductResponse;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.domain.wish.entity.Wish;
import project.shopclone.domain.wish.repository.WishRepository;
import project.shopclone.global.jwt.service.TokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static project.shopclone.global.common.TokenDuration.ACCESS_TOKEN_DURATION;

@Slf4j
@SpringBootTest
class WishControllerTest {
    RestClient restClient = RestClient.create("http://localhost:8080");

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AuthUserRepository authUserRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    WishRepository wishRepository;

    // 사용자 5명이 동시에 좋아요(위시 등록) API 실행
    @Test
    @DisplayName("좋아요 동시성 테스트")
    void likeConcurrentTest() throws InterruptedException {
        List<String> tokenList = new ArrayList<>();
        int userCount = 5; // 테스트 유저 5명
        Long productId = 364L;
        Product product = productRepository.findById(productId).get();

        // 기존 AuthUser에서 테스트 유저 수만큼 인원 선정하여 토큰 생성
        List<AuthUser> randAuthUserList = authUserRepository.findAll().subList(0, userCount);
        for (AuthUser authUser : randAuthUserList) {
            // 이미 위시 등록 되어 있으면 해제
            Member member = memberRepository.findByAuthUser(authUser);
            Optional<Wish> wish = wishRepository.findByMemberAndProduct(member, product);
            wish.ifPresent(value -> wishRepository.delete(value));
            // 토큰 리스트에 추가
            tokenList.add(tokenProvider.generateToken(authUser, ACCESS_TOKEN_DURATION));
        }
        int startLike = getLikeCount(productId);
        log.info("실행전 좋아요 개수 = " + startLike);

        ExecutorService executorService = Executors.newFixedThreadPool(5); // 스레드풀 개수
        CountDownLatch countDownLatch = new CountDownLatch(userCount);

        for(String token : tokenList) {
            executorService.submit(() -> {
               try{
                   createWish(token, productId);
               }catch (Exception e){
                   log.error("Error during createWish", e);
               }finally { // 예외 발생시(좋아요 트랜잭션 실패시)에도 카운트다운
                   countDownLatch.countDown();
               }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        boolean awaitTermination = executorService.awaitTermination(10, TimeUnit.SECONDS);
        if(!awaitTermination){
            log.error("스레드풀 타임아웃");
        }

        int endLike = getLikeCount(productId);
        log.info("실행후 좋아요 개수 = " + endLike);
        log.info("상품 좋아요 개수 변화량 = " + (endLike - startLike));

        // 각 사용자들의 위시 등록 성공 여부 확인
        int number = 1;
        for (AuthUser authUser : randAuthUserList){
            Member member = memberRepository.findByAuthUser(authUser);
            Optional<Wish> wish = wishRepository.findByMemberAndProduct(member, product);
            if(wish.isPresent()){
                log.info("{}번 멤버 좋아요 등록 성공", number);
            }else{
                log.info("{}번 멤버 좋아요 등록 실패", number);
            }
            number++;
        }
    }

    void createWish(String token, Long productId) {
        try{
            restClient.post()
                    .uri("/api/wish/{productId}", productId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .toEntity(Void.class);
            ResponseEntity.ok().build();
        }catch (Exception e){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    Integer getLikeCount(Long productId) {
        ProductResponse productResponse = restClient.get()
                .uri("/api/products/detail/{productId}", productId)
                .retrieve()
                .body(ProductResponse.class);
        return productResponse.getWishCount();
    }
}