package com.scmspain.repositories;

import com.scmspain.entities.UrlTweet;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>UrlTweetRepository</h1>
 * Repository to handler the entities UrlTweet
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Repository
@Transactional
public class UrlTweetRepository {
    private EntityManager entityManager;
    private MetricWriter metricWriter;

    public UrlTweetRepository(EntityManager entityManager, MetricWriter metricWriter) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void addUrlTweet(UrlTweet tweet) {
        this.incrementMetrics("add-urlTweet");
        this.entityManager.persist(tweet);
    }

    /**
     Recover urls of a given tweet from repository
     Parameter - idTweet - id of the Tweet to retrieve the Urls related
     Result - retrieved UrlTweet
     */
    public List<UrlTweet> listAllUrlTweets(Long idTweet) {
        this.incrementMetrics("times-queried-urlTweet");
        List<UrlTweet> result = new ArrayList<UrlTweet>();
        TypedQuery<Long> query = this.entityManager.createQuery(
                "SELECT id FROM UrlTweet AS urlTweetId WHERE idTweet = " + idTweet + " ORDER BY positionInTweet ASC",
                Long.class
        );
        List<Long> ids = query.getResultList();
        for (Long id : ids) {
            result.add(this.entityManager.find(UrlTweet.class, id));
        }
        return result;
    }

    private void incrementMetrics(String statName) {
        this.metricWriter.increment(new Delta<Number>(statName, 1));
    }
}
