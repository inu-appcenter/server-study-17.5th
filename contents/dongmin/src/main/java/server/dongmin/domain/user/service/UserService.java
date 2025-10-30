package server.dongmin.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.dongmin.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
