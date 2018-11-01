package io.javalovers.mapper;

import io.javalovers.entity.CommentEntity;
import io.javalovers.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mappings({ @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "text", source = "entity.comment"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "date", source = "entity.dateTime")})
    Comment commentEntityToComment(CommentEntity entity);

    @Mappings({ @Mapping(target = "comment", source = "entity.text"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "dateTime", source = "entity.date")})
    CommentEntity commentToCommentEntity(Comment entity);
}
