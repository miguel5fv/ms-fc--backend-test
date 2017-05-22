package com.scmspain.repositories;

import com.scmspain.entities.Tweet;
import com.scmspain.enums.TweetStatusEnum;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>TweetRepository</h1>
 * Repository to handler the entities Tweet
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Repository
@Transactional
public class TweetRepository {
    private EntityManager entityManager;
    private MetricWriter metricWriter;

    public TweetRepository(EntityManager entityManager, MetricWriter metricWriter) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
    */
    public void addTweet(Tweet tweet) {
        this.incrementMetrics("published-tweets");
        tweet.setStatus(TweetStatusEnum.PUBLISHED.getValue());
        tweet.setUpdatedAt();
        this.entityManager.persist(tweet);
    }

    /**
     Discard a tweet from repository
     Parameter - Tweet - Tweet to be discarded
     */
    public void discardTweet(Tweet tweet) {
        this.incrementMetrics("discarded-tweets");
        tweet.setStatus(TweetStatusEnum.DISCARDED.getValue());
        tweet.setUpdatedAt();
        this.entityManager.persist(tweet);
    }

    /**
      Recover a tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public Tweet getTweet(Long id) {
      return this.entityManager.find(Tweet.class, id);
    }

    /**
      Recover tweet from repository
      Result - retrieved Tweet
    */
    public List<Tweet> listAllTweets() {
        this.incrementMetrics("times-queried-tweets");
        return this.listAllTweetsByStatus(TweetStatusEnum.PUBLISHED.getValue(), "id");
    }

    /**
      Recover discarded tweets
      Result - retrieved discarded Tweet
    */
    public List<Tweet> listAllDiscardedTweets() {
        this.incrementMetrics("times-queried-discarded-tweets");
        return this.listAllTweetsByStatus(TweetStatusEnum.DISCARDED.getValue(), "updatedAt");
    }

    private List<Tweet> listAllTweetsByStatus(Integer status, String orderField) {
        List<Tweet> result = new ArrayList<Tweet>();
        TypedQuery<Long> query = this.entityManager.createQuery(
                "SELECT id FROM Tweet AS tweetId WHERE pre2015MigrationStatus <> 99 AND status = " + status + " ORDER BY " + orderField + " DESC",
                Long.class
        );
        List<Long> ids = query.getResultList();
        for (Long id : ids) {
            result.add(getTweet(id));
        }
        return result;
    }

    private void incrementMetrics(String statName) {
        this.metricWriter.increment(new Delta<Number>(statName, 1));
    }
}
