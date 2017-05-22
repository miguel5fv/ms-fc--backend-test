package com.scmspain.services;

import com.scmspain.dtos.TweetDto;
import com.scmspain.entities.Tweet;
import com.scmspain.repositories.TweetRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest {
    private TweetService tweetService;
    private TweetRepository tweetRepository;
    private UrlTweetService urlTweetService;

    @Before
    public void setUp() throws Exception {
        this.tweetRepository = mock(TweetRepository.class);
        this.urlTweetService = mock(UrlTweetService.class);

        this.tweetService = new TweetService(tweetRepository, urlTweetService);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate.";
        when(urlTweetService.getTextWithoutUrls(any(String.class))).thenReturn(mockText);
        tweetService.publishTweet("Guybrush Threepwood", mockText);

        verify(tweetRepository).addTweet(any(Tweet.class));
        verify(urlTweetService).storeUrlsFromTweet(any(Long.class), any(String.class));
    }

    @Test
    public void shouldInsertANewTweetWhenUrlExceedLimit() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate. http://guybrush.threepwood.com/monkey/island/search-joke?enemy=Lechuck";
        String mockCleanText = "I am Guybrush Threepwood, mighty pirate. What can I do to achieve it? I checked this page to try it";
        when(urlTweetService.getTextWithoutUrls(any(String.class))).thenReturn(mockCleanText);
        tweetService.publishTweet("Guybrush Threepwood", mockText);

        verify(tweetRepository).addTweet(any(Tweet.class));
        verify(urlTweetService).storeUrlsFromTweet(any(Long.class), any(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPublisherTweetEmpty() throws Exception {
        tweetService.publishTweet("", "Caribbean Island.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetEmpty() throws Exception {
        tweetService.publishTweet("Pirate", "");
    }

    @Test
    public void shouldDiscardTweet() throws Exception {
        Tweet tweet = new Tweet();
        when(tweetRepository.getTweet(any(Long.class))).thenReturn(tweet);

        tweetService.discardTweet(1L);

        verify(tweetRepository).discardTweet(any(Tweet.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenDiscardANotExistingTweet() throws Exception {
        when(tweetRepository.getTweet(any(Long.class))).thenReturn(null);

        tweetService.discardTweet(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenDiscardAnInvalidIdTweet() throws Exception {
        when(tweetRepository.getTweet(any(Long.class))).thenReturn(null);

        tweetService.discardTweet(0L);
    }

    @Test
    public void shouldGetATweet() throws Exception {
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setPublisher("test");
        tweet.setTweet("test message tweet");

        when(tweetRepository.getTweet(any(Long.class))).thenReturn(tweet);

        TweetDto tweetDto = tweetService.getTweet(1L);

        assertThat(tweetDto.getId()).isEqualTo(tweet.getId());
        assertThat(tweetDto.getTweet()).isEqualTo(tweet.getTweet());
        assertThat(tweetDto.getPublisher()).isEqualTo(tweet.getPublisher());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetNotExists() throws Exception {
        when(tweetRepository.getTweet(any(Long.class))).thenReturn(null);

        tweetService.getTweet(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenIdTweetIsNotValid() throws Exception {
        when(tweetRepository.getTweet(any(Long.class))).thenReturn(null);

        tweetService.getTweet(0L);
    }

    @Test
    public void shouldListZeroPublishedTweets() throws Exception {
        when(tweetRepository.listAllTweets()).thenReturn(new ArrayList<Tweet>());

        List<TweetDto> tweets = tweetService.listAllTweets();

        assertThat(tweets.size()).isEqualTo(0);
    }

    @Test
    public void shouldListPublishedTweets() throws Exception {
        List<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(new Tweet());
        when(tweetRepository.listAllTweets()).thenReturn(tweets);

        List<TweetDto> tweetsDto = tweetService.listAllTweets();

        assertThat(tweetsDto.size()).isEqualTo(1);
    }

    @Test
    public void shouldListZeroDiscardedTweets() throws Exception {
        when(tweetRepository.listAllTweets()).thenReturn(new ArrayList<Tweet>());

        List<TweetDto> tweets = tweetService.listAllDiscardedTweets();

        assertThat(tweets.size()).isEqualTo(0);
    }

    @Test
    public void shouldListDiscardedTweets() throws Exception {
        List<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(new Tweet());
        when(tweetRepository.listAllDiscardedTweets()).thenReturn(tweets);

        List<TweetDto> tweetsDto = tweetService.listAllDiscardedTweets();

        assertThat(tweetsDto.size()).isEqualTo(1);
    }
}
