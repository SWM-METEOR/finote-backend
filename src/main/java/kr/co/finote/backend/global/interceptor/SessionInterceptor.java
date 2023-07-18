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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info(request.getRequestURL().toString());
        log.info(request.getRequestURI());
        log.info(request.getMethod());
        HttpSession session = request.getSession();
        if (request.getMethod().equals("GET") && request.getRequestURI().contains("/articles/")) return true;   // 블로그 글 조회인 경우 인증되지 않은 사용자도 조회 가능
        else if (session.getAttribute(SessionUtils.LOGIN_USER) == null) {
            throw new CustomException(ResponseCode.UNAUTHENTICATED);
        }
        else return true;
    }
}
