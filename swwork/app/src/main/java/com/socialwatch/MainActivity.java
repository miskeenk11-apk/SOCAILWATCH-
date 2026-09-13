package com.socialwatch;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final android.os.Handler statusHandler = new android.os.Handler();
    private TextView internetStatus;

    private int dp(float v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private ImageButton imageButton(int drawableId, String description) {
        ImageButton b = new ImageButton(this);
        b.setImageResource(drawableId);
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setAdjustViewBounds(true);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setPadding(0, 0, 0, 0);
        b.setContentDescription(description);
        b.setFocusable(true);
        return b;
    }

    private void addGap(LinearLayout root, int heightDp) {
        Space gap = new Space(this);
        root.addView(gap, new LinearLayout.LayoutParams(-1, dp(heightDp)));
    }

    private LinearLayout searchRow(String hint, String service, String searchBase) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setHint(hint);
        search.setTextSize(14);
        search.setTextColor(Color.WHITE);
        search.setHintTextColor(Color.LTGRAY);
        search.setPadding(dp(10), 0, dp(10), 0);
        search.setInputType(InputType.TYPE_CLASS_TEXT);
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setBackgroundResource(R.drawable.bg_about);

        TextView button = new TextView(this);
        button.setText("SEARCH");
        button.setTextColor(Color.WHITE);
        button.setTextSize(12);
        button.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(R.drawable.bg_about);
        button.setFocusable(true);

        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(0, dp(42), 1f);
        ep.rightMargin = dp(5);
        row.addView(search, ep);
        row.addView(button, new LinearLayout.LayoutParams(dp(78), dp(42)));

        View.OnClickListener doSearch = v -> {
            String q = search.getText().toString().trim();
            if (!q.isEmpty()) {
                openService(service, searchBase + android.net.Uri.encode(q));
            }
        };
        button.setOnClickListener(doSearch);
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch.onClick(v);
                return true;
            }
            return false;
        });
        return row;
    }

    private void addServiceBlock(LinearLayout root, String service, int drawable, String description,
                                 String defaultUrl, String searchHint, String searchBase) {
        root.addView(searchRow(searchHint, service, searchBase),
                new LinearLayout.LayoutParams(-1, dp(42)));
        addGap(root, 2);

        ImageButton button = imageButton(drawable, description);
        root.addView(button, new LinearLayout.LayoutParams(-1, dp(88)));
        button.setOnClickListener(v -> openService(service, defaultUrl));
        addGap(root, 4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(4, 4, 4));
        getWindow().setNavigationBarColor(Color.BLACK);

        FrameLayout screen = new FrameLayout(this);
        screen.setBackgroundColor(Color.BLACK);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(12), dp(5), dp(12), dp(5));

        ImageView header = new ImageView(this);
        header.setImageResource(R.drawable.header);
        header.setScaleType(ImageView.ScaleType.FIT_CENTER);
        header.setAdjustViewBounds(true);
        root.addView(header, new LinearLayout.LayoutParams(-1, -2));
        addGap(root, 2);

        addServiceBlock(root, "YouTube", R.drawable.youtube_button, "YouTube Shorts",
                Config.DEFAULT_YOUTUBE_URL, "Search YouTube Shorts", Config.YOUTUBE_SEARCH_BASE);
        addServiceBlock(root, "Facebook", R.drawable.facebook_button, "Facebook Reels",
                Config.DEFAULT_FACEBOOK_URL, "Search Facebook Reels", Config.FACEBOOK_SEARCH_BASE);
        addServiceBlock(root, "TikTok", R.drawable.tiktok_button, "TikTok Short Videos",
                Config.DEFAULT_TIKTOK_URL, "Search TikTok Videos", Config.TIKTOK_SEARCH_BASE);

        Space spacer = new Space(this);
        root.addView(spacer, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView about = new TextView(this);
        about.setText("ABOUT");
        about.setTextColor(Color.LTGRAY);
        about.setTextSize(13);
        about.setGravity(Gravity.CENTER);
        about.setBackgroundResource(R.drawable.bg_about);
        about.setFocusable(true);
        about.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        root.addView(about, new LinearLayout.LayoutParams(dp(105), dp(38)));

        screen.addView(root, new FrameLayout.LayoutParams(-1, -1));

        internetStatus = new TextView(this);
        internetStatus.setTextSize(10);
        internetStatus.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        internetStatus.setGravity(Gravity.CENTER);
        internetStatus.setPadding(dp(5), 0, dp(5), 0);
        internetStatus.setBackgroundColor(Color.argb(180, 0, 0, 0));

        FrameLayout.LayoutParams statusParams = new FrameLayout.LayoutParams(
                dp(118), dp(28), Gravity.TOP | Gravity.END);
        statusParams.rightMargin = dp(5);
        statusParams.topMargin = dp(3);
        screen.addView(internetStatus, statusParams);

        setContentView(screen);
        startInternetStatusMonitor();
    }

    private void startInternetStatusMonitor() {
        updateInternetStatus();
        statusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateInternetStatus();
                statusHandler.postDelayed(this, 1500);
            }
        }, 1500);
    }

    private void updateInternetStatus() {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            connected = info != null && info.isConnected();
        }
        if (internetStatus != null) {
            internetStatus.setText(connected ? "Internet: AVAILABLE" : "Internet: UNAVAILABLE");
            internetStatus.setTextColor(connected ? Color.rgb(80, 220, 110) : Color.rgb(255, 90, 90));
        }
    }

    @Override
    protected void onDestroy() {
        statusHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void openService(String service, String url) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra(WebActivity.EXTRA_SERVICE, service);
        i.putExtra(WebActivity.EXTRA_URL, url);
        startActivity(i);
    }
}
