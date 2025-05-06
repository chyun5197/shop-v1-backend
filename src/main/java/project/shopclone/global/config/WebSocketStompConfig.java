package project.shopclone.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    // Stomp에서 사용하는 메시지 브로커를 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 stomp 접속 주소는 url = ws://백엔드주소/ws (http 아님)
        // 현재 nginx에서 /ws/ -> 백엔드주소로 리버스 프록시 후 여기서 -> /ws 엔드포인트
        registry.addEndpoint("/ws") // 처음 웹소켓 핸드쉐이크 연결 수립을 위한 경로
                .setAllowedOriginPatterns("https://*.hyun-clone.shop") // 하위 도메인 허용
//                .setAllowedOrigins("https://www.hyun-clone.shop")
//                .setAllowedOriginPatterns("*")
                .withSockJS();
//                .setAllowedOrigins("*")
    }
    // STOMP를 사용하면 웹소켓만 사용할 때와 다르게 하나의 연결주소마다 핸들러 클래스를 따로 구현할 필요없이
    // 컨트롤러 방식으로 간편하게 사용할 수 있다.

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
        메세지를 구독(subscribe, 수신)하는 요청 엔드포인트
        파라미터로 지정한 prefix가 붙은 메시지를 발행할 경우, 메시지 브로커가 이를 처리하게 된다.
        /queue prefix는 메시지가 1대1로 송신될 때
        /topic prefix는 메시지가 1대다로 브로드캐스팅될때 사용
        */

        registry.enableSimpleBroker("/sub");
//        registry.enableSimpleBroker("/queue","/topic");

        // 메세지를 발행(publish, 송신)하는 엔드포인트
        registry.setApplicationDestinationPrefixes("/pub");
//        registry.setApplicationDestinationPrefixes("/app");
    }
}
