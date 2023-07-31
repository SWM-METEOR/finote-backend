package kr.co.finote.backend.global.authentication.oauth;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.authentication.PrincipalDetails;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String email = oAuth2User.getAttribute("email");

        Optional<User> findUser = userRepository.findByUsernameAndIsDeleted(username, false);
        User user;

        if (findUser.isEmpty()) {
            user = User.newSocialUser(username, email, provider, providerId, LocalDateTime.now());
            userRepository.save(user);
        } else {
            user = findUser.get();
            user.updateLastLoginDate(LocalDateTime.now());
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
