package ru.comptech.bc.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import ru.comptech.bc.server.security.auth.WalletAuthenticationFilter;
import ru.comptech.bc.server.security.auth.WalletAuthenticationProvider;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private EntryPoint entryPoint;

    @Resource
    private NoRedirectLogoutFilter noRedirectLogoutFilter;

    @Resource
    private WalletAuthenticationFilter authenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint);

        http.csrf().disable();

//        ------   noRedirectLogoutFilter   ------

        noRedirectLogoutFilter.setLogoutRequestMatcher(
                new RegexRequestMatcher("/rest/logout", "POST"));

        http.addFilter(noRedirectLogoutFilter);

//        ------   authenticationFilter   ------

        authenticationFilter.setRequiresAuthenticationRequestMatcher(
                new RegexRequestMatcher("/rest/login", "POST"));
        authenticationFilter.setAuthenticationFailureHandler(this::authFailure);
        authenticationFilter.setAuthenticationSuccessHandler(this::authSuccess);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(new WalletAuthenticationProvider());
    }

    private void authFailure(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final OutputStream out = response.getOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, Map.of("error", "Not authorized"));
        out.flush();
    }

    private void authSuccess(HttpServletRequest request, HttpServletResponse response,
                             Authentication auth) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

//    ------   Beans   -----

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
