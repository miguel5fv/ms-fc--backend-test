package com.scmspain.enums;

/**
 * <h1>TweetStatusEnum</h1>
 * Enumeration with the status of a Tweet.
 *
 * @author  Miguel Florido (miguel5fv@gmail.com)
 */
public enum TweetStatusEnum {
        PUBLISHED(0), DISCARDED(1);

        private Integer value;

        TweetStatusEnum(Integer value) {
                this.value = value;
        }

        public Integer getValue() { return value; }
}