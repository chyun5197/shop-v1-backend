package project.shopclone.global.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

// 쿠키 관리 클래스(생성, 삭제)
public class CookieUtil {
    // 요청값(이름, 값, 만료기간)을 바탕으로 HTTP 응답에 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
//        cookie.setPath("http://localhost:3000/"); //쿠키 후보
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    // 실제로 삭제하는 방법은 없기에 파라미터로 넘어온 키의 쿠키를 빈 값으로 바꾸고
    // 만료 시간을 0으로 설정해 쿠키가 재생성 되지마자 만료 처리
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return;
        }
        for(Cookie cookie : cookies){
            if(name.equals(cookie.getName())){
                cookie.setValue(""); // 쿠키 빈 값 처리
                cookie.setPath("/");
                cookie.setMaxAge(0); // 만료 시간 0으로 바로 만료되도록
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 반환
    public static String serialize(Object obj){
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls){
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
