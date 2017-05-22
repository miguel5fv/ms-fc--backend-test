package com.scmspain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <h1>UrlTweet</h1>
 * Entity which represents an UrlTweet
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
@Entity
public class UrlTweet {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long idTweet;
    @Column(nullable = false)
    private String url;
    @Column (nullable=false, length = UrlTweet.URL_LENGTH)
    private Integer positionInTweet = 0;

    public static final int URL_LENGTH = 2083;

    public UrlTweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(Long idTweet) {
        this.idTweet = idTweet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPositionInTweet() {
        return positionInTweet;
    }

    public void setPositionInTweet(Integer positionInTweet) {
        this.positionInTweet = positionInTweet;
    }
}