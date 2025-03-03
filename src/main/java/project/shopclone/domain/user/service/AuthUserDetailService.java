package project.shopclone.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;

@RequiredArgsConstructor
@Service
    // UserDetailsService: 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class AuthUserDetailService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는걸 가능하게 해주는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("(loadUserByUsername)UserDetailService loadUserByUsername()의 매개값 email: " + email);

        AuthUser authUser = authUserRepository.findByEmail(email);
//        System.out.println("(loadUserByUsername)UserDetailService authUser.getEmail(): " + authUser.getEmail());

        // 사용자 없다면
        if(authUser == null){ // 여기서 아이디 잘못된걸 판별해서 응답해줄 수 있음
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, authUser.getPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("(실행1)authentication.getName(): " + authentication.getName());


        return authUserRepository.findByEmail(email);
    }


}
