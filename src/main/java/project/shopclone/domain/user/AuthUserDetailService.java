package project.shopclone.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
    // UserDetailsService: 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class AuthUserDetailService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername email = " + email);

        AuthUser authUser = authUserRepository.findByEmail(email);

        // 사용자 없다면
        if(authUser == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        System.out.println("loadUserByUsername 이메일: " + authUser.getEmail());

//        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser.getEmail(), securityUser.getPassword(), Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자가 있다면 UserDetails 객체 생성
//        return new org.springframework.security.core.userdetails.User(
//                securityUser.getEmail(),
//                securityUser.getPassword(),
//                Collections.singleton(new SimpleGrantedAuthority(securityUser.getAuthorities().toString())));
        return authUserRepository.findByEmail(email);
//                .orElseThrow(() -> new IllegalArgumentException((email)));
    }


}
