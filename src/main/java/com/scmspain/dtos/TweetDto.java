package com.scmspain.dtos;

/**
 * <h1>TweetDto</h1>
 * Dto to transport the information of a Tweet between layers
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
public class TweetDto {
    private Long id;
    private String publisher;
    private String tweet;
    private Long pre2015MigrationStatus = 0L;

    public TweetDto() {
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
}