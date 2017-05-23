package com.scmspain.services;

import com.scmspain.dtos.TweetDto;
import com.scmspain.entities.Tweet;
import com.scmspain.entities.UrlTweet;
import com.scmspain.repositories.UrlTweetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>UrlTweetService</h1>
 * Service to handler the url tweets.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Service
public class UrlTweetService {
    private final UrlTweetRepository urlTweetRepository;
    private String regexUrl = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public UrlTweetService(UrlTweetRepository urlTweetRepository) {
        this.urlTweetRepository = urlTweetRepository;
    }

    /**
     Clean text of urls
     Parameter - text - text to remove urls
     Result - text without urls
     */
    public String getTextWithoutUrls(String text) {
        return text.replaceAll(this.regexUrl, "");
    }

    /**
     Store urls in repository
     Parameter - idTweet - identifier of the tweet to store the urls
     Parameter - tweet - Content with urls to extract and store
     Result - UrlTweet from text stored
     */
    public void storeUrlsFromTweet(Long idTweet, String tweetContent) {
        this.persistUrlsFromTweet(idTweet, tweetContent, this.getUrlsFromTweetContent(tweetContent));
    }

    private List<String> getUrlsFromTweetContent(String tweet) {
        List<String> allUrl = new ArrayList<String>();
        Matcher m = Pattern.compile(this.regexUrl)
                .matcher(tweet);

        while (m.find()) {
            allUrl.add(m.group());
        }

        return allUrl;
    }

    private void persistUrlsFromTweet(Long idTweet, String originalTweet, List<String> urls) {
        List<UrlTweet> allTweetUrls = new ArrayList<UrlTweet>();

        for (String url: urls) {
            UrlTweet urlTweet = new UrlTweet();

            urlTweet.setIdTweet(idTweet);
            urlTweet.setPositionInTweet(originalTweet.indexOf(url));
            urlTweet.setUrl(url);

            allTweetUrls.add(urlTweet);

            this.urlTweetRepository.addUrlTweet(urlTweet);

            originalTweet.replaceFirst(Matcher.quoteReplacement(url), "");
        }
    }

    /**
     Given a tweet returns a tweetDto with the content decorated with the urls
     Parameter - tweet - tweet entity
     Result - TweetDto with the content text decorated with its urls
     */
    public TweetDto decorateTweetWithUrls(Tweet tweet) {
        List<UrlTweet> listOfUrlTweets = this.urlTweetRepository.listAllUrlTweets(tweet.getId());
        String tweetContentToDecorate = tweet.getTweet();

        TweetDto tweetDto = new TweetDto();

        tweetDto.setId(tweet.getId());
        tweetDto.setPre2015MigrationStatus(tweet.getPre2015MigrationStatus());
        tweetDto.setPublisher(tweet.getPublisher());

        for(UrlTweet urlTweet: listOfUrlTweets) {
            tweetContentToDecorate = this.decorateTweetContent(tweetContentToDecorate, urlTweet);
        }

        tweetDto.setTweet(tweetContentToDecorate);

        return tweetDto;
    }

    private String decorateTweetContent(String tweetContentToDecorate, UrlTweet urlTweet) {
        return tweetContentToDecorate.substring(0, urlTweet.getPositionInTweet())
                + urlTweet.getUrl()
                + tweetContentToDecorate.substring(urlTweet.getPositionInTweet());
    }
}

