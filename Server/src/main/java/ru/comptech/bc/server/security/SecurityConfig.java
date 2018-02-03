package ru.comptech.bc.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private EntryPoint entryPoint;

    @Resource
    private NoRedirectLogoutFilter noRedirectLogoutFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint);

        http.csrf().disable();

//    ------   NoRedirectLogoutFilter   ------

        noRedirectLogoutFilter.setLogoutRequestMatcher(
                new RegexRequestMatcher("/rest/logout", "POST"));

        http.addFilter(noRedirectLogoutFilter);
    }
}
