package com.socialwatch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE = "service";
    public static final String EXTRA_URL = "url";

    private WebView webView;
    private String service;
    private String initialUrl;

    private int dp(float v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setStatusBarColor(Color.BLACK);
        w.setNavigationBarColor(Color.BLACK);

        service = getIntent().getStringExtra(EXTRA_SERVICE);
        initialUrl = getIntent().getStringExtra(EXTRA_URL);
        if (service == null) service = "Short Video";
        if (initialUrl == null || initialUrl.trim().isEmpty()) initialUrl = "https://www.youtube.com/shorts/";

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.BLACK);
        root.addView(webView, new FrameLayout.LayoutParams(-1, -1));

        TextView back = new TextView(this);
        back.setText("‹");
        back.setTextColor(Color.BLACK);
        back.setTextSize(31);
        back.setGravity(Gravity.CENTER);
        back.setBackgroundResource(R.drawable.bg_back);
        back.setElevation(dp(8));
        back.setOnClickListener(v -> finish());

        FrameLayout.LayoutParams bp = new FrameLayout.LayoutParams(dp(46), dp(46), Gravity.TOP | Gravity.START);
        bp.leftMargin = dp(8);
        bp.topMargin = dp(7);
        root.addView(back, bp);

        setContentView(root);
        configureWebView();
        webView.loadUrl(initialUrl);
    }

    private boolean isAllowedHost(String host) {
        if (host == null) return false;
        host = host.toLowerCase(Locale.US);

        if ("youtube".equalsIgnoreCase(service)) {
            return host.equals("youtube.com") ||
                   host.endsWith(".youtube.com") ||
                   host.equals("youtu.be") ||
                   host.endsWith(".youtu.be") ||
                   host.equals("googlevideo.com") ||
                   host.endsWith(".googlevideo.com") ||
                   host.equals("ytimg.com") ||
                   host.endsWith(".ytimg.com") ||
                   host.equals("googleusercontent.com") ||
                   host.endsWith(".googleusercontent.com");
        }

        if ("facebook".equalsIgnoreCase(service)) {
            return host.equals("facebook.com") ||
                   host.endsWith(".facebook.com") ||
                   host.equals("fb.watch") ||
                   host.endsWith(".fb.watch") ||
                   host.equals("fbcdn.net") ||
                   host.endsWith(".fbcdn.net") ||
                   host.equals("fbsbx.com") ||
                   host.endsWith(".fbsbx.com");
        }

        // TikTok desktop mode.
        return host.equals("tiktok.com") ||
               host.endsWith(".tiktok.com") ||
               host.equals("tiktokcdn.com") ||
               host.endsWith(".tiktokcdn.com") ||
               host.equals("byteoversea.com") ||
               host.endsWith(".byteoversea.com") ||
               host.equals("ibytedtos.com") ||
               host.endsWith(".ibytedtos.com");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setSupportZoom(false);
        s.setLoadsImagesAutomatically(true);
        s.setJavaScriptCanOpenWindowsAutomatically(false);
        s.setSupportMultipleWindows(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Keep cookies/session support for platform pages.
        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        if ("TikTok".equalsIgnoreCase(service)) {
            // Desktop-style user agent, while preserving the WebView engine.
            s.setUserAgentString(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/120.0.0.0 Safari/537.36"
            );
        }

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                if (isAllowedHost(u.getHost())) {
                    return false;
                }
                // No general browser and no external arbitrary sites.
                return true;
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri u = Uri.parse(url);
                if (isAllowedHost(u.getHost())) {
                    return false;
                }
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // The visible in-app button is the primary route to Home.
        // Android back also follows normal browser history if available.
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
