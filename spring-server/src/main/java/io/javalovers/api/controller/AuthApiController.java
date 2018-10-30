package io.javalovers.api.controller;

import io.javalovers.api.AuthApi;
import io.javalovers.model.Credentials;
import io.javalovers.security.TokenUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class AuthApiController implements AuthApi {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public ResponseEntity<Void> authentication(@ApiParam(value = "authentication information" ,required=true )
                                                   @Valid @RequestBody Credentials body) {
        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body
                .getUsername(), body.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.generateToken(body.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }
}
