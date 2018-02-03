package ru.comptech.bc.server.security.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.web3j.crypto.*;

public class WalletAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        if (!(authentication instanceof WalletAuthentication))
            return null;
        final WalletAuthentication auth = (WalletAuthentication) authentication;

        final WalletFile walletFile = auth.getPrincipal();
        final String password = auth.getCredentials();

        final Credentials credentials;
        try {
            credentials = Credentials.create(Wallet.decrypt(password, walletFile));
        } catch (CipherException e) {
            throw new BadCredentialsException("Password is incorrect");
        }

        return new WalletAuthentication(credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(WalletAuthentication.class);
    }
}
