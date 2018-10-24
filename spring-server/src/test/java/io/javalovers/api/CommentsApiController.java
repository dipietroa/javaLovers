package io.javalovers.api;

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
import java.util.Arrays;
import java.util.List;

import io.javalovers.entity.CommentEntity;
import io.javalovers.model.Comment;
import io.javalovers.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


public class CommentsApiController {
    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentsApiController commentsApiController;

    private CommentEntity commentEntity;
    private List<CommentEntity> commentEntities;
    private List<Comment> comments;

    /**
     * Setting Mockito and MockMvc. Setting groups for tests
     */
    @Before
    public void init() {
        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setName("test");
        commentEntity.setComment("my comment");

        commentEntities = new ArrayList<CommentEntity>();
        commentEntities.add(commentEntity);


        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentsApiController).build();
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
        mockMvc.perform(get("/comments")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text", is(commentEntity.getComment())))
                .andExpect(jsonPath("$[0].user", is(commentEntity.getName())));
        verify(commentService, times(1)).getCommentList();
        verifyNoMoreInteractions(commentService);
    }
}
