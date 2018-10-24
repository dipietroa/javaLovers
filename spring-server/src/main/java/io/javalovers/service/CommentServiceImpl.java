package io.javalovers.service;

import io.javalovers.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommentEntity getCommentById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, CommentEntity.class);
    }

    @Override
    public List<CommentEntity> getCommentList() {
        return mongoTemplate.findAll(CommentEntity.class);
    }

    @Override
    public CommentEntity addComment(CommentEntity comment) {
        mongoTemplate.save(comment);
        return comment;
    }
}
