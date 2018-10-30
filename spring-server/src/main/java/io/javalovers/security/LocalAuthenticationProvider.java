package io.javalovers.security;

import io.javalovers.entity.UserEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class LocalAuthenticationProvider implements AuthenticationProvider{

    // Here we just store credentials in code source
    // /!\ Not secure, it's just to show how it works
    // just modify authenticate() to check credentials via
    // a database query, where the credentials are stored safely, for example.
    private static String ADMIN_USR_NAME = "admin";
    private static String ADMIN_USR_PASS = "password";

    public LocalAuthenticationProvider() { }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if(username.equals(ADMIN_USR_NAME) && password.equals(ADMIN_USR_PASS))
            return new UserEntity(username, password);
        return null;
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationClass);
    }
}
