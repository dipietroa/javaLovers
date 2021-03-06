package io.javalovers.api.controller;

import io.javalovers.entity.CommentEntity;
import io.javalovers.model.Comment;
import io.javalovers.model.Credentials;
import io.javalovers.service.CommentService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static io.javalovers.utils.Serializer.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentsApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthenticationManager authenticationManager;

    HttpHeaders httpHeaders = new HttpHeaders();
    private CommentEntity commentEntity = new CommentEntity();
    private List<CommentEntity> commentEntities = new ArrayList<>();
    private Comment comment = new Comment();
    private static String COM_NAME = "test";
    private static String COM_TXT = "test1";
    private static Long COM_ID = 1L;
    Credentials usr = new Credentials();

    private String getToken() throws Exception {
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                usr.getUsername(), usr.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(usr.getUsername(), usr.getPassword()));

        // Get a JWT for secured endpoints
        ResultActions result = mockMvc.perform(post("/auth").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usr)))
                .andExpect(status().isOk())
                .andExpect(header().exists("authorization"));
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(usr.getUsername(), usr.getPassword()));
        verifyNoMoreInteractions(authenticationManager);

        return result.andReturn().getResponse().getHeader("Authorization");
    }

    @Before
    public void setUp() {
        comment.setName(COM_NAME);
        comment.setText(COM_TXT);
        commentEntity.setId(COM_ID);
        commentEntity.setName(COM_NAME);
        commentEntity.setComment(COM_TXT);
        commentEntities.add(commentEntity);
        usr.setPassword("test");
        usr.setUsername("test");

        httpHeaders.add("Accept","application/json");
    }

    /**
     * Testing GET on /comments endpoint. Expected behaviour is to get a 200 status code and a list of existing comments
     * initialized in init()
     *
     * @throws Exception
     */
    @Test
    public void test_get_comments_success() throws Exception {
        when(commentService.getCommentList()).thenReturn(commentEntities);
        mockMvc.perform(get("/comments").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
        verify(commentService, times(1)).getCommentList();
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing GET on /comments endpoint. Expected behaviour is to get a 406 status code when header
     * Accept is not expected
     *
     * @throws Exception
     */
    @Test
    public void test_get_comments_not_acceptable() throws Exception {
        httpHeaders.set("Accept", "application/xml");
        commentEntities.add(commentEntity);
        mockMvc.perform(get("/comments").headers(httpHeaders))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Testing GET on /comments/{id} endpoint. Expected behaviour is to get a 200 status code and a
     * single comment
     *
     * @throws Exception
     */
    @Test
    public void test_get_comment_by_id_success() throws Exception {
        when(commentService.getCommentById(1L)).thenReturn(commentEntity);
        mockMvc.perform(get("/comments/1").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(COM_NAME)))
                .andExpect(jsonPath("$.text", is(COM_TXT)));
        verify(commentService, times(1)).getCommentById(COM_ID);
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing GET on /comments/{id} endpoint. Expected behaviour is to get a 404 status code
     * when comment identified by given ID doesn't exist
     *
     * @throws Exception
     */
    @Test
    public void test_get_comment_by_id_not_acceptable() throws Exception {
        httpHeaders.set("Accept", "application/xml");
        mockMvc.perform(get("/comments/1").headers(httpHeaders))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Testing GET on /comments/{id} endpoint. Expected behaviour is to get a 404 status code
     * when comment identified by given ID doesn't exist
     *
     * @throws Exception
     */
    @Test
    public void test_get_comment_by_id_not_found() throws Exception {
        when(commentService.getCommentById(1L)).thenReturn(null);
        mockMvc.perform(get("/comments/1").headers(httpHeaders))
                .andExpect(status().isNotFound());
        verify(commentService, times(1)).getCommentById(1L);
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing POST on /comments endpoint. Expected behaviour is to get a 201 status code
     * when a comment is successfully added
     *
     * @throws Exception
     */
    @Test
    public void test_post_comment_success() throws Exception {
        when(commentService.addComment(any())).thenReturn(commentEntity);
        mockMvc.perform(post("/comments").headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isCreated());
        verify(commentService, times(1)).addComment(any());
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing POST on /comments endpoint. Expected behaviour is to get a 500 status code
     * when a problem occurs with mongoDB
     *
     * @throws Exception
     */
    @Test
    public void test_post_comment_mongo_error() throws Exception {
        when(commentService.addComment(any())).thenReturn(null);
        mockMvc.perform(post("/comments").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(comment)))
                .andExpect(status().isInternalServerError());
        verify(commentService, times(1)).addComment(any());
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing POST on /comments endpoint. Expected behaviour is to get a 400 status code
     * when the received is not in the expected format
     *
     * @throws Exception
     */
    @Test
    public void test_post_comment_bad_request() throws Exception {
        // min chars for name field is 3, here we set it to 1 length
        comment.setName("a");
        mockMvc.perform(post("/comments").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(comment)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testing POST on /comments endpoint. Expected behaviour is to get a 406 status code when header
     * Accept is not expected
     *
     * @throws Exception
     */
    @Test
    public void test_post_comment_not_acceptable() throws Exception {
        httpHeaders.set("Accept", "application/xml");
        mockMvc.perform(post("/comments").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(comment)))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Testing DELETE on /comments/{id} endpoint. Expected behaviour is to get a 200 status code when
     * the focused comment has been deleted
     *
     * @throws Exception
     */
    @Test
    public void test_delete_comment_by_id_success() throws Exception {
        httpHeaders.set("Authorization", getToken());
        when(commentService.deleteCommentById(1L)).thenReturn(commentEntity);
        mockMvc.perform(delete("/comments/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(commentService, times(1)).deleteCommentById(1L);
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing DELETE on /comments/{id} endpoint. Expected behaviour is to get a 404 status code when
     * the focused comment doesn't exist
     *
     * @throws Exception
     */
    @Test
    public void test_delete_comment_by_id_not_found() throws Exception {
        httpHeaders.set("Authorization", getToken());
        when(commentService.deleteCommentById(1L)).thenReturn(null);
        mockMvc.perform(delete("/comments/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(commentService, times(1)).deleteCommentById(1L);
        verifyNoMoreInteractions(commentService);
    }

    /**
     * Testing DELETE on /comments/{id} endpoint. Expected behaviour is to get a 406 status code when header
     * Accept is not expected
     *
     * @throws Exception
     */
    @Test
    public void test_delete_comment_by_id_not_acceptable() throws Exception {
        httpHeaders.set("Authorization", getToken());
        httpHeaders.set("Accept", "application/xml");
        mockMvc.perform(delete("/comments/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Testing DELETE on /comments/{id} endpoint. Expected behaviour is to get a 401 status code when header
     * Authorization does not exist
     *
     * @throws Exception
     */
    @Test
    public void test_delete_comment_by_id_unauthorized() throws Exception {
        mockMvc.perform(delete("/comments/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Testing DELETE on /comments/{id} endpoint. Expected behaviour is to get a 401 status code when header
     * Authorization contains an invalid JWT
     *
     * @throws Exception
     */
    @Test
    public void test_delete_comment_by_id_unauthorized_invalid_jwt() throws Exception {
        httpHeaders.set("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        httpHeaders.set("Accept", "application/xml");
        mockMvc.perform(delete("/comments/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
