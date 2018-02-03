package ru.comptech.bc.server.security.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.stereotype.Component;
import org.web3j.crypto.WalletFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WalletAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static class JSON {
        private final WalletFile wallet;
        private final String password;

        @JsonCreator
        private JSON(@JsonProperty(value = "wallet") WalletFile wallet,
                     @JsonProperty(value = "password") String password) {
            this.wallet = wallet;
            this.password = password;
        }
    }

    protected WalletAuthenticationFilter() {
        super(AnyRequestMatcher.INSTANCE);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final StringBuilder builder = new StringBuilder();
        while (true) {
            final String line = request.getReader().readLine();
            if (line == null)
                break;
            builder.append(line);
        }

        final ObjectMapper mapper = new ObjectMapper();
        final JSON json = mapper.readValue(builder.toString(), JSON.class);

        final WalletAuthentication authRequest =
                new WalletAuthentication(json.wallet, json.password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
