package kr.co.finote.backend.global.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.user.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info(request.getRequestURL().toString());
        log.info(request.getRequestURI());
        log.info(request.getMethod());
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionUtils.LOGIN_USER) == null) {
            throw new CustomException(ResponseCode.UNAUTHENTICATED);
        }
        return true;
    }
}
