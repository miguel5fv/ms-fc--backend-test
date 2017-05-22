package com.scmspain.services;

import com.scmspain.dtos.TweetDto;
import com.scmspain.entities.Tweet;
import com.scmspain.entities.UrlTweet;
import com.scmspain.repositories.UrlTweetRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UrlTweetServiceTest {
    private UrlTweetRepository urlTweetRepository;
    private UrlTweetService urlTweetService;

    @Before
    public void setUp() throws Exception {
        this.urlTweetRepository = mock(UrlTweetRepository.class);

        this.urlTweetService = new UrlTweetService(urlTweetRepository);
    }

    @Test
    public void shouldReturnTheTextEqual() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate.";

        String resultText = this.urlTweetService.getTextWithoutUrls(mockText);

        assertThat(mockText).isEqualTo(resultText);
    }

    @Test
    public void shouldCleanTheUrls() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate. http://guybrush.threepwood.com";
        String expectedText = "I am Guybrush Threepwood, mighty pirate. ";

        String resultText = this.urlTweetService.getTextWithoutUrls(mockText);

        assertThat(resultText).isEqualTo(expectedText);
    }

    @Test
    public void shouldNotStoreAnyUrl() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate.";
        Long idTweet = 1L;

        this.urlTweetService.storeUrlsFromTweet(idTweet, mockText);

        verify(urlTweetRepository, never()).addUrlTweet(any(UrlTweet.class));
    }

    @Test
    public void shouldStoreAnUrl() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate. http://guybrush.threepwood.com";
        Long idTweet = 1L;

        this.urlTweetService.storeUrlsFromTweet(idTweet, mockText);

        verify(urlTweetRepository, atLeastOnce()).addUrlTweet(any(UrlTweet.class));
    }

    @Test
    public void shouldNotDecorateTheTweetWithUrls() throws Exception {
        String mockText = "I am Guybrush Threepwood, mighty pirate.";
        Long idTweet = 1L;
        Tweet tweet = this.buildTMock(idTweet, mockText);
        when(urlTweetRepository.listAllUrlTweets(any(Long.class))).thenReturn(new ArrayList<UrlTweet>());

        TweetDto tweetDto = this.urlTweetService.decorateTweetWithUrls(tweet);

        assertThat(tweetDto.getId()).isEqualTo(tweet.getId());
        assertThat(tweetDto.getTweet()).isEqualTo(tweet.getTweet());
    }

    @Test
    public void shouldDecorateTheTweetWithUrls() throws Exception {
        String urlString = "http://guybrush.threepwood.com";
        String mockText = "I am Guybrush Threepwood,  mighty pirate.";
        String expectedText = "I am Guybrush Threepwood, " + urlString + " mighty pirate.";
        Long idTweet = 1L;
        List<UrlTweet> listOfUrls = new ArrayList<UrlTweet>();
        Tweet tweet = this.buildTMock(idTweet, mockText);
        UrlTweet urlTweet = new UrlTweet();

        urlTweet.setIdTweet(idTweet);
        urlTweet.setPositionInTweet(26);
        urlTweet.setUrl(urlString);

        listOfUrls.add(urlTweet);

        when(urlTweetRepository.listAllUrlTweets(any(Long.class))).thenReturn(listOfUrls);

        TweetDto tweetDto = this.urlTweetService.decorateTweetWithUrls(tweet);

        assertThat(tweetDto.getId()).isEqualTo(tweetDto.getId());
        assertThat(expectedText).isEqualTo(tweetDto.getTweet());
    }

    private Tweet buildTMock(Long idTweet, String tweetContent)
    {
        Tweet tweet = new Tweet();

        tweet.setId(idTweet);
        tweet.setTweet(tweetContent);

        return tweet;
    }
}
