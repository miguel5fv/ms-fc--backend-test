package com.scmspain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * <h1>Tweet</h1>
 * Entity which represents the entity Tweet
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false, length = Tweet.TWEET_LENGTH)
    private String tweet;
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    @Column (nullable=false)
    private Integer status = 0;
    @Column (nullable=false)
    private Long updatedAt;

    public static final int TWEET_LENGTH = 140;

    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    public Long getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt() {this.updatedAt = Calendar.getInstance().getTimeInMillis();}

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() { return this.status; }
}