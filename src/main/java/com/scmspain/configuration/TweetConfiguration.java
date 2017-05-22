package com.scmspain.configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.repositories.TweetRepository;
import com.scmspain.repositories.UrlTweetRepository;
import com.scmspain.services.TweetService;
import com.scmspain.services.UrlTweetService;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * <h1>TweetConfiguration</h1>
 * Definition of the dependency injections.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Configuration
public class TweetConfiguration {


    @Bean
    public UrlTweetRepository getUrlTweetRepository(EntityManager entityManager, MetricWriter metricWriter) {
        return new UrlTweetRepository(entityManager, metricWriter);
    }

    @Bean
    public TweetRepository getTweetRepository(EntityManager entityManager, MetricWriter metricWriter) {
        return new TweetRepository(entityManager, metricWriter);
    }

    @Bean
    public UrlTweetService getUrlTweetService(UrlTweetRepository urlTweetRepository) {
        return new UrlTweetService(urlTweetRepository);
    }

    @Bean
    public TweetService getTweetService(TweetRepository tweetRepository, UrlTweetService urlTweetService) {
        return new TweetService(tweetRepository, urlTweetService);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }
}
