# 쇼핑몰 클론 프로젝트
[프로젝트 바로가기](http://hyun-clone.shop)
<img width="1459" alt="프로젝트 소개" src="https://github.com/user-attachments/assets/b69ddf9f-0b61-47c8-86ec-808b53d70ba9" />

#### 프로젝트 vs 원본 사이트
<img width="2048" alt="비교캡처 조회" src="https://github.com/user-attachments/assets/08a2b4f3-a7ee-4681-984a-3bd5be502231" /><br>

<img width="2048" alt="비교캡쳐 위시" src="https://github.com/user-attachments/assets/6eaec8e0-713e-4df6-9c8e-cb286f04e04a" />

---

## 📃 개요
### 소개
- 쇼핑몰 '뮤직포스' 사이트를 클론한 개인 프로젝트 <br>
- 상품 정보는 크롤링한 데이터를 사용

### 프로젝트 목적
- 웹 전반에 대한 학습과 이해도를 높이기 위해 풀스택으로 온라인 쇼핑몰을 클론하는 프로젝트를 진행 <br>

### 개발기간
- 25년 2월 ~ 진행중 <br>

### 도메인
- [hyun-clone.shop](http://hyun-clone.shop)

### 원본 사이트
- [뮤직포스 홈페이지](https://musicforce.co.kr/index.html)

---

## 기술 스택
### Backend
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazon-s3&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0769AD?style=for-the-badge&logo=querydsl&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)

### Frontend
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

### Infrastructure
![Amazon AWS](https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)

### CI/CD
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

### Performance Test & Monitoring
![k6](https://img.shields.io/badge/k6-%2300B5E2.svg?style=for-the-badge&logo=k6&logoColor=white)
![AWS CloudWatch](https://img.shields.io/badge/AWS%20CloudWatch-%23FF4F8B.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

### IDE & Tools
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![DataGrip](https://img.shields.io/badge/DataGrip-000000?style=for-the-badge&logo=datagrip&logoColor=white)
![WebStorm](https://img.shields.io/badge/webstorm-143?style=for-the-badge&logo=webstorm&logoColor=white&color=black)

---

## 🔨 서버 아키텍처
<img width="1153" alt="SA" src="https://github.com/user-attachments/assets/218c13d5-0b5b-478d-97fa-7e4e69f2a017" />

## 📊 ERD 설계
<img width="876" alt="ERD" src="https://github.com/user-attachments/assets/1d559af5-e042-431f-abb0-ab00d9479a64" />

---

## 모니터링

## :link: 트러블 슈팅
SSL을 연결하면서 발생한 302 CORS 에러

## :link: 성능 테스트 및 성능 개선
#### [성능 확인]
[병목 지점과 최대 쓰루풋](https://github.com/chyun5197/shop-clone-backend/issues/1)

#### [성능 개선]
1. 커버링 인덱스와 서브쿼리를 활용한 인덱스 튜닝
2. 캐시 서버를 도입하여 캐싱 및 DB 부하 분산
3. 로드밸런싱을 통한 스케일 아웃(nginx, alb)

---

## :link: 가상 서버 코드 링크
docker-compose.yml <br>
nginx.conf





