package ru.comptech.bc.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoRedirectLogoutFilter extends LogoutFilter {

    public NoRedirectLogoutFilter() {
        super((req, res, au) -> {}, NoRedirectLogoutFilter::logout,
                new SecurityContextLogoutHandler());
    }

    private static void logout(HttpServletRequest req, HttpServletResponse response,
                               Authentication auth) {
        if (auth == null)
            throw new RuntimeException();

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
