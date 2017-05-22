package com.scmspain.repositories;

import com.scmspain.entities.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class TweetRepositoryTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetRepository tweetRepository;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);

        this.tweetRepository = new TweetRepository(entityManager, metricWriter);
    }

    @Test
    public void shouldAddANewTweet() throws Exception {
        Tweet tweet = new Tweet();
        tweetRepository.addTweet(tweet);

        verify(entityManager).persist(any(Tweet.class));
    }

    @Test
    public void shouldDiscardATweet() throws Exception {
        Tweet tweet = new Tweet();
        tweetRepository.discardTweet(tweet);

        verify(entityManager).persist(any(Tweet.class));
    }

    @Test
    public void shouldGetATweet() throws Exception {
        tweetRepository.getTweet(1L);

        verify(entityManager).find(same(Tweet.class), any(Long.class));
    }

    @Test
    public void shouldListZeroPublishedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        this.mockQuery(idList);

        List<Tweet> resultTweets = tweetRepository.listAllTweets();

        assertThat(resultTweets.size()).isEqualTo(0);
    }

    @Test
    public void shouldListPublishedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        Long idTweet = 1L;
        Tweet tweet = new Tweet();
        when(entityManager.find(same(Tweet.class), same(idTweet))).thenReturn(tweet);
        idList.add(idTweet);

        this.mockQuery(idList);

        List<Tweet> resultTweets = tweetRepository.listAllTweets();

        assertThat(resultTweets.size()).isEqualTo(1);
    }

    @Test
    public void shouldListZeroDiscardedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        this.mockQuery(idList);

        List<Tweet> resultTweets = tweetRepository.listAllDiscardedTweets();

        assertThat(resultTweets.size()).isEqualTo(0);
    }

    @Test
    public void shouldListDiscardedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        Long idTweet = 1L;
        Tweet tweet = new Tweet();
        when(entityManager.find(same(Tweet.class), same(idTweet))).thenReturn(tweet);
        idList.add(idTweet);

        this.mockQuery(idList);

        List<Tweet> resultTweets = tweetRepository.listAllDiscardedTweets();

        assertThat(resultTweets.size()).isEqualTo(1);
    }

    private void mockQuery(List<Long> idList) {
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(idList);
        when(entityManager.createQuery(any(String.class), same(Long.class))).thenReturn(query);
    }
}