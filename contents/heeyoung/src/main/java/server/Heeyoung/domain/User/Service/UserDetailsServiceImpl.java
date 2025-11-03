package server.Heeyoung.domain.User.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User findUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 조회 실패"));

        return new UserDetailsImpl(findUser);
    }
}
