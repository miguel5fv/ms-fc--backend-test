package com.scmspain.controller.command;

/**
 * <h1>DiscardTweetCommand</h1>
 * Handler the discarding of tweets commands.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
public class DiscardTweetCommand {
    private Long tweet;

    public Long getTweet() {
        return tweet;
    }

    public void setTweet(Long tweet) {
        this.tweet = tweet;
    }
}
