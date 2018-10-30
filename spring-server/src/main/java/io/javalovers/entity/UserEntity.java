package io.javalovers.entity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public class UserEntity extends UsernamePasswordAuthenticationToken{
    private static final long serialVersionUID = 1L;

    public UserEntity(String username, String password) {
        super(username, password, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
