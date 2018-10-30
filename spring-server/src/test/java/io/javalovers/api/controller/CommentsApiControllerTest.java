package io.javalovers.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import io.javalovers.api.CommentsApiController;
import io.javalovers.entity.CommentEntity;
import io.javalovers.model.Comment;
import io.javalovers.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentsApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    HttpHeaders httpHeaders = new HttpHeaders();
    private CommentEntity commentEntity = new CommentEntity();
    private List<CommentEntity> commentEntities = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();


    /**
     * Testing GET on /comments endpoint. Expected behaviour is to get a 200 status code and a list of existing comments
     * initialized in init()
     *
     * @throws Exception
     */
    @Test
    public void test_get_comments_success() throws Exception {
        httpHeaders.add("Accept","application/json");
        commentEntities.add(commentEntity);
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
    public void test_get_comments_wrong_accept() throws Exception {
        httpHeaders.add("Accept","application/xml");
        commentEntities.add(commentEntity);
        mockMvc.perform(get("/comments").headers(httpHeaders))
                .andExpect(status().isNotAcceptable());
    }
}
