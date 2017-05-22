package com.scmspain.repositories;

import com.scmspain.entities.UrlTweet;
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

public class UrlTweetRepositoryTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private UrlTweetRepository urlTweetRepository;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);

        this.urlTweetRepository = new UrlTweetRepository(entityManager, metricWriter);
    }

    @Test
    public void shouldAddANewTweet() throws Exception {
        UrlTweet urlTweet = new UrlTweet();
        urlTweetRepository.addUrlTweet(urlTweet);

        verify(entityManager).persist(any(UrlTweet.class));
    }

    @Test
    public void shouldAListZeroUrlTweets() throws Exception {
        UrlTweet urlTweet = new UrlTweet();
        urlTweetRepository.addUrlTweet(urlTweet);

        verify(entityManager).persist(any(UrlTweet.class));
    }

    @Test
    public void shouldListZeroDiscardedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        this.mockQuery(idList);

        List<UrlTweet> resultUrlTweets = urlTweetRepository.listAllUrlTweets(1L);

        assertThat(resultUrlTweets.size()).isEqualTo(0);
    }

    @Test
    public void shouldListDiscardedTweets() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        Long idTweet = 1L;
        UrlTweet urlTweet = new UrlTweet();
        when(entityManager.find(same(UrlTweet.class), same(idTweet))).thenReturn(urlTweet);
        idList.add(idTweet);

        this.mockQuery(idList);

        List<UrlTweet> resultUrlTweets = urlTweetRepository.listAllUrlTweets(1L);

        assertThat(resultUrlTweets.size()).isEqualTo(1);
    }

    private void mockQuery(List<Long> idList) {
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(idList);
        when(entityManager.createQuery(any(String.class), same(Long.class))).thenReturn(query);
    }
}