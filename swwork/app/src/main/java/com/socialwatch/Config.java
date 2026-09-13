package com.socialwatch;

/**
 * Edit these three values to set the Short/Reel/Video that opens
 * when the user first enters each service.
 *
 * The app does not provide a general browser; WebActivity only permits
 * navigation inside the selected platform's allowed domains.
 */
public final class Config {
    private Config() {}

    // Replace these with your preferred direct links.
    public static final String DEFAULT_YOUTUBE_URL =
            "https://www.youtube.com/shorts/";
    public static final String DEFAULT_FACEBOOK_URL =
            "https://www.facebook.com/reels/";
    public static final String DEFAULT_TIKTOK_URL =
            "https://www.tiktok.com/";

    public static final String YOUTUBE_SEARCH_BASE =
            "https://www.youtube.com/results?search_query=";
    public static final String FACEBOOK_SEARCH_BASE =
            "https://www.facebook.com/search/top?q=";
    public static final String TIKTOK_SEARCH_BASE =
            "https://www.tiktok.com/search?q=";
}
