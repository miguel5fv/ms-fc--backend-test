package com.scmspain.services;

import com.scmspain.dtos.TweetDto;
import com.scmspain.entities.Tweet;
import com.scmspain.repositories.TweetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>TweetService</h1>
 * Service to handler tweets.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Service
public class TweetService {
    private TweetRepository tweetRepository;
    private UrlTweetService urlTweetService;

    public TweetService(TweetRepository tweetRepository, UrlTweetService urlTweetService) {
        this.tweetRepository = tweetRepository;
        this.urlTweetService = urlTweetService;
    }

    /**
     Publish a tweet
     Parameter - publisher - creator of the Tweet
     Parameter - text - Content of the Tweet
     Result - published Tweet
     */
    public void publishTweet(String publisher, String text) {
        String cleanedText = this.urlTweetService.getTextWithoutUrls(text);

        checkPublisherTweet(publisher);
        checkContentTweet(cleanedText);

        Tweet tweet = new Tweet();
        tweet.setTweet(cleanedText);
        tweet.setPublisher(publisher);

        this.tweetRepository.addTweet(tweet);
        this.urlTweetService.storeUrlsFromTweet(tweet.getId(), text);
    }

    private void checkPublisherTweet(String publisher) {
        if (publisher == null || publisher.length() < 1) {
            throw new IllegalArgumentException("Tweet must have a not empty publisher name");
        }
    }

    private void checkContentTweet(String text) {
        if (text == null || text.length() < 1 || text.length() >= Tweet.TWEET_LENGTH) {
            throw new IllegalArgumentException("Tweet must not be greater than " + Tweet.TWEET_LENGTH + " characters");
        }
    }

    /**
     Recover tweet
     Parameter - id - id of the Tweet to retrieve
     Result - retrieved TweetDto
     */
    public TweetDto getTweet(Long id) {
        checkIdTweet(id);
        Tweet tweet = this.tweetRepository.getTweet(id);

        if (tweet != null) {
            TweetDto tweetDto = new TweetDto();

            tweetDto.setId(tweet.getId());
            tweetDto.setTweet(tweet.getTweet());
            tweetDto.setPublisher(tweet.getPublisher());
            tweetDto.setPre2015MigrationStatus(tweet.getPre2015MigrationStatus());

            return tweetDto;
        } else {
            throw new IllegalArgumentException("The tweet does not exists");
        }
    }

    /**
     List all of the published tweets
     Result - List of TweetDto published
     */
    public List<TweetDto> listAllTweets() {
        List<Tweet> tweets = this.tweetRepository.listAllTweets();

        return this.decorateTweetsWithUrls(tweets);
    }

    /**
     Discard a tweet
     Parameter - id - identifier of the Tweet
     Result - Tweet discarded
     */
    public void discardTweet(Long idTweet) {
        checkIdTweet(idTweet);

        Tweet tweet = this.tweetRepository.getTweet(idTweet);

        if (tweet != null) {
            this.tweetRepository.discardTweet(tweet);
        } else {
            throw new IllegalArgumentException("The tweet cannot be discarded because does not exists");
        }
    }

    private void checkIdTweet(Long idTweet) {
        if (idTweet == null || idTweet < 1) {
            throw new IllegalArgumentException("Id of a tweet should be an integer greater than 0");
        }
    }

    /**
     List all of the discarded tweets
     Result - List of TweetDto discarded
     */
    public List<TweetDto> listAllDiscardedTweets() {
        List<Tweet> tweets = this.tweetRepository.listAllDiscardedTweets();

        return this.decorateTweetsWithUrls(tweets);
    }

    private List<TweetDto> decorateTweetsWithUrls(List<Tweet> tweets) {
        List<TweetDto> tweetDtos = new ArrayList<TweetDto>();

        for(Tweet tweet: tweets) {
            tweetDtos.add(this.urlTweetService.decorateTweetWithUrls(tweet));
        }

        return tweetDtos;
    }
}
