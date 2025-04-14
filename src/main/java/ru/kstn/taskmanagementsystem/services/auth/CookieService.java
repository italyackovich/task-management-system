package ru.kstn.taskmanagementsystem.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface CookieService {
    ResponseCookie createCookie(String cookieName, String cookieValue, Long cookieExpiration);
    void deleteCookie(String cookieName);
    String getValueFromCookie(HttpServletRequest request, String cookieName);
}
