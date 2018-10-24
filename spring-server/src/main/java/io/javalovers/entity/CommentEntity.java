package io.javalovers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 767 bytes is the stated prefix limitation for InnoDB tables in MySQL version 5.6 (and prior versions). It's
 * 1,000 bytes long for MyISAM tables. In MySQL version 5.7 and upwards this limit has been increased to 3072 bytes.
 * So for the unique key to work with all database collation (utf8_genral_ci, utf8mb4_unicode_ci, etc), the maximum
 * length of those column should be 191 and not 255 for varchar
 */
@Document
public class CommentEntity {

    @Id
    private long id;

    private String name;

    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}