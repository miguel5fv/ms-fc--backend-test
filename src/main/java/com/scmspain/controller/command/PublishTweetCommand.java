package com.scmspain.controller.command;

/**
 * <h1>PublishTweetCommand</h1>
 * Handler the publishing of tweets commands.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
public class PublishTweetCommand {
    private String publisher;
    private String tweet;

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
}
