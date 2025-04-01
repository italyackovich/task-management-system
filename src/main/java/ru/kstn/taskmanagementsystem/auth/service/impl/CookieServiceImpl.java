package ru.kstn.taskmanagementsystem.auth.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.auth.exception.MissingCookieException;
import ru.kstn.taskmanagementsystem.auth.service.CookieService;

import java.util.Arrays;

@Service
public class CookieServiceImpl implements CookieService {
    @Override
    public ResponseCookie createCookie(String cookieName, String cookieValue, Long cookieExpiration) {
        return ResponseCookie.from(cookieName, cookieValue)
                .maxAge(cookieExpiration)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    @Override
    public void deleteCookie(String cookieName) {
        ResponseCookie.from(cookieName, "")
                .httpOnly(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getValueFromCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new MissingCookieException("Cookies are missing"));
    }
}
