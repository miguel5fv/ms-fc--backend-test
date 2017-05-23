package com.scmspain.controller.command;

/**
 * <h1>DiscardTweetCommand</h1>
 * Handler the discarding of tweets commands.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
public class DiscardTweetCommand {
    private Long idTweet;

    public Long getTweet() {
        return idTweet;
    }

    public void setTweet(Long idTweet) {
        this.idTweet = idTweet;
    }
}
