package io.javalovers.service;

import io.javalovers.entity.CommentEntity;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentService {
    CommentEntity getCommentById(Long id);

    CommentEntity deleteCommentById(Long id);

    List<CommentEntity> getCommentList();

    CommentEntity addComment(CommentEntity comment);
}
