package kr.co.finote.backend.src.user.api;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // TODO rest controller 변환
@RequiredArgsConstructor
@Slf4j
public class LoginApi {

    private final UserRepository userRepository;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        System.out.println("LoginController.index");
        return "index";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        log.info("hello");
        System.out.println("LoginController.loginForm");
        return "loginForm";
    }

    @GetMapping("/test/custom")
    public void test() {
        throw new CustomException(ResponseCode.TEST_ERROR);
    }

    @GetMapping("/test/ex")
    public void testEx() throws Exception {
        throw new Exception();
    }
}
