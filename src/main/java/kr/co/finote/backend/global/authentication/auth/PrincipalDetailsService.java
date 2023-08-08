package kr.co.finote.backend.global.authentication.auth;

import kr.co.finote.backend.global.authentication.PrincipalDetails;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(email, false);

        if(findUser.isPresent()) {
            return new PrincipalDetails(findUser.get());
        }

        return null;
    }
}

