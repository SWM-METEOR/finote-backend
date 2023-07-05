package kr.co.finote.backend.src.user.api;

import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        System.out.println("LoginController.index");
        return "index";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        System.out.println("LoginController.loginForm");
        return "loginForm";
    }
}
