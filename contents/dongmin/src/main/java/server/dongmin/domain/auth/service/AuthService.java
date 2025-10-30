package server.dongmin.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dongmin.domain.auth.dto.req.LoginRequest;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.entity.User;
import server.dongmin.domain.user.repository.UserRepository;
import server.dongmin.global.jwt.JwtToken;
import server.dongmin.global.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.email())) {
            throw new IllegalArgumentException("Email already exists");
        } else if (userRepository.existsByNickName(signUpRequest.nickName())) {
            throw new IllegalArgumentException("Nickname already exists");
        } else if (userRepository.existsByPhoneNumber(signUpRequest.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = User.create(signUpRequest, bCryptPasswordEncoder.encode(signUpRequest.password()));

        userRepository.save(user);
    }

    @Transactional
    public JwtToken login(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
