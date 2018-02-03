package ru.comptech.bc.server.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

import java.util.Collection;
import java.util.List;

public class WalletAuthentication implements Authentication {

    private final static Collection<GrantedAuthority> AUTHORITIES =
            List.of(new SimpleGrantedAuthority("ROLE_USER"));

    private boolean authenticated;

    private WalletFile walletFile;
    private String password;

    private Credentials credentials;

    WalletAuthentication(WalletFile walletFile, String password) {
        this.walletFile = walletFile;
        this.password = password;

        authenticated = false;
    }

    WalletAuthentication(Credentials credentials) {
        this.credentials = credentials;

        authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authenticated)
            return AUTHORITIES;
        return List.of();
    }

    @Override
    public String getCredentials() {
        return password;
    }

    @Override
    public Credentials getDetails() {
        return credentials;
    }

    @Override
    public WalletFile getPrincipal() {
        return walletFile;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated)
            throw new IllegalArgumentException();
        authenticated = false;
    }

    @Override
    public String getName() {
        return (credentials == null) ? null : credentials.getAddress();
    }
}
