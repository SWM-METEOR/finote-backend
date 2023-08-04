package kr.co.finote.backend.global.advice;

import static kr.co.finote.backend.global.code.ResponseCode.*;

import javax.servlet.http.HttpServletResponse;
import kr.co.finote.backend.global.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(
        basePackages = "kr.co.finote.backend.src")
public class GlobalRestControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@Nullable MethodParameter returnType, @Nullable Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @Nullable MethodParameter returnType,
            @Nullable MediaType selectedContentType,
            @Nullable Class selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {

        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status);

        if (resolve == null) return body;
        else if (resolve.is2xxSuccessful()) {
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatus(SUCCESS.getStatus().value());
            apiResponse.setMessage(SUCCESS.getMessage());
            apiResponse.setData(body);
            return apiResponse;
        } else {
            return body;
        }
    }
}
