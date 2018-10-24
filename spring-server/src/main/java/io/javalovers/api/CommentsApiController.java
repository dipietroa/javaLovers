package io.javalovers.api;

import io.javalovers.entity.CommentEntity;
import io.javalovers.mapper.CommentMapper;
import io.javalovers.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalovers.service.CommentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-10-24T16:40:41.784Z")

@Controller
public class CommentsApiController implements CommentsApi {

    private static final Logger log = LoggerFactory.getLogger(CommentsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    CommentService commentService;

    @org.springframework.beans.factory.annotation.Autowired
    public CommentsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> addComment(@ApiParam(value = "Comment to add" ,required=true )  @Valid @RequestBody Comment body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            commentService.addComment(CommentMapper.INSTANCE.commentToCommentEntity(body));
            try {
                return ResponseEntity.created(new URI("test")).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<Void>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public ResponseEntity<Comment> getCommentById(@ApiParam(value = "The comment that needs to be fetched.",required=true) @PathVariable("id") Long id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            CommentEntity comment = commentService.getCommentById(id);
            if(comment == null) {
                return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.ok(CommentMapper.INSTANCE.commentEntityToComment(comment));
            }
        }

        return new ResponseEntity<Comment>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public ResponseEntity<List<Comment>> getComments() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            List<CommentEntity> commentEntities = commentService.getCommentList();
            List<Comment> comments = commentEntities.stream()
                                                    .map(c -> CommentMapper.INSTANCE.commentEntityToComment(c))
                                                    .collect(Collectors.toList());
            return ResponseEntity.ok(comments);
        }

        return new ResponseEntity<List<Comment>>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
