package kr.co.finote.backend.src.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {

    public void startSession(HttpServletRequest servletRequest, User loginUser) {
        HttpSession session = servletRequest.getSession();
        session.setAttribute(SessionUtils.LOGIN_USER, loginUser);
    }
}
