package io.javalovers.service;

import io.javalovers.entity.CommentEntity;
import io.javalovers.exception.SequenceException;
import io.javalovers.seq.SequenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private static final String HOSTING_SEQ_KEY = "hosting";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SequenceDao sequenceDao;

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
        try {
            comment.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
        } catch (SequenceException e) {
            return null;
        }
        mongoTemplate.save(comment);
        return comment;
    }
}
