package project.shopclone.global.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final AuthUserRepository authUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 요청을 바탕으로 사용자 정보를 담은 객체 반환(load)
        // 사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // google, naver, kakao
        String provider = userRequest.getClientRegistration().getRegistrationId();

        saveOrUpdate(oAuth2User, provider);
        return oAuth2User;
//        return new PrincipalDetails(memberEntity, user.getAttributes());
    }

    // 이메일에 대응되는 사용자 찾고 없으면 회원 데이터 생성
    private AuthUser saveOrUpdate(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email, name, oauthId;

        // 파라미터 확인용
//        System.out.println("attributes = " + attributes);
//        System.out.println("provider = " + provider);

        if (provider.equals("google")){ // 구글
            System.out.println("진입1");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            oauthId = (String) attributes.get("sub");
        }else if (provider.equals("kakao")){ // 카카오
            System.out.println("진입2");
            Map attributesProperties = (Map) attributes.get("properties");
            name = (String) attributesProperties.get("nickname");
            Map attributesKakaoAcount = (Map) attributes.get("kakao_account");
            email = (String) attributesKakaoAcount.get("email");
            oauthId = attributes.get("id").toString();
        }else { // 네이버
            System.out.println("진입3");
            Map attributesResponse = (Map) attributes.get("response");
            name = (String) attributesResponse.get("name");
            email = (String) attributesResponse.get("email");
            oauthId = attributesResponse.get("id").toString();
        }
        System.out.println("oauthId = " + oauthId);

        // 기존 회원 여부 찾는 기준은 각 소셜 고유ID
        AuthUser authUser = authUserRepository.findByOauthId(oauthId)
//                .map(entity -> entity.update(@@)) // 회원가입된 폼로그인 기존 회원 정보가 있으면 @@ 업데이트
                .orElse(AuthUser.builder()
                        .oauthId(oauthId)
                        .email(email)
                        .nickname(name)
                        .channel(provider)
                        .build());

        return authUserRepository.save(authUser);
    }
}
