package io.javalovers.api.controller;

import io.javalovers.model.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.javalovers.utils.Serializer.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    HttpHeaders httpHeaders = new HttpHeaders();
    Credentials usr = new Credentials();

    @Before
    public void setUp() {
        usr.setUsername("test");
        usr.setPassword("test");
        httpHeaders.add("Accept","application/json");
    }

    /**
     * Testing POST on /auth endpoint. Expected behaviour is to get a 200 status code
     * when the authentication succeeds
     *
     * @throws Exception
     */
    @Test
    public void test_post_auth_success() throws Exception {
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usr
                .getUsername(), usr.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(usr.getUsername(), usr.getPassword()));
        mockMvc.perform(post("/auth").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usr)))
                .andExpect(status().isOk())
                .andExpect(header().exists("authorization"));
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(usr
                .getUsername(), usr.getPassword()));
        verifyNoMoreInteractions(authenticationManager);
    }

    /**
     * Testing POST on /auth endpoint. Expected behaviour is to get a 200 status code
     * when the authentication succeeds
     *
     * @throws Exception
     */
    @Test
    public void test_post_auth_fails() throws Exception {
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usr
                .getUsername(), usr.getPassword())))
                .thenThrow(new BadCredentialsException(""));
        mockMvc.perform(post("/auth").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usr)))
                .andExpect(status().isUnauthorized());
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(usr.getUsername(), usr.getPassword()));
        verifyNoMoreInteractions(authenticationManager);
    }
}
