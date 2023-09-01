package kr.co.finote.backend.global.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import kr.co.finote.backend.global.annotation.Liked;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.jwt.JwtTokenProvider;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikedArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLikedAnnotation = parameter.hasParameterAnnotation(Liked.class);
        boolean hasMemberType = User.class.isAssignableFrom(parameter.getParameterType());

        return hasLikedAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String AccessToken = jwtTokenProvider.resolveToken(request);

        if (AccessToken == null) {
            return null;
        }

        boolean isValid = jwtTokenProvider.validateTokenExpiration(AccessToken);

        if (!isValid) {
            throw new UnAuthorizedException(ResponseCode.INVALID_ACCESS_TOKEN);
        }

        String memberEmail = jwtTokenProvider.getMemberEmail(AccessToken);
        User byEmail = userService.findByEmail(memberEmail);

        return byEmail;
    }
}
