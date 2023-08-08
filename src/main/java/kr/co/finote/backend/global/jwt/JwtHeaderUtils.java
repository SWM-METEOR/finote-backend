package kr.co.finote.backend.global.jwt;

import javax.servlet.http.HttpServletRequest;

public class JwtHeaderUtils {

    private final static String HEADER_NAME = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_NAME);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
