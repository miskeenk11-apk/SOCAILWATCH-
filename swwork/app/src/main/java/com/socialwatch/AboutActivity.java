package com.socialwatch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private int dp(float v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView make(String s, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.START);
        t.setPadding(dp(12), dp(6), dp(12), dp(6));
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(10), dp(18), dp(10));
        root.setBackgroundColor(Color.BLACK);

        TextView title = make("SOCIAL WATCH", 25, Color.rgb(0, 150, 245), true);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(52)));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.addView(make(
            "SOCIAL WATCH کیا ہے؟\n\n" +
            "SOCIAL WATCH ایک سادہ، lightweight Short Video Browser ہے جو YouTube Shorts، Facebook Reels اور TikTok Short Videos تک آسان رسائی فراہم کرتا ہے۔ اس کا مقصد short-video استعمال کو ایک صاف اور focused screen میں رکھنا ہے۔\n\n" +
            "MAIN SCREEN\n\n" +
            "Main screen پر YouTube، Facebook اور TikTok کے الگ buttons موجود ہیں۔ ہر platform کے اوپر اس کا اپنا الگ search box بھی دیا گیا ہے تاکہ آپ اسی platform پر اپنی مطلوبہ short videos تلاش کر سکیں۔\n\n" +
            "INTERNET STATUS\n\n" +
            "Main screen کے اوپر دائیں کونے میں Internet status دکھائی دیتا ہے۔ Wi-Fi یا Mobile Data connected ہو تو AVAILABLE، اور connection بند ہو تو UNAVAILABLE دکھائی دیتا ہے۔\n\n" +
            "APP کا طریقۂ کار\n\n" +
            "YouTube، Facebook اور TikTok app کے اندر اپنے مخصوص WebView میں کھلتے ہیں۔ SOCIAL WATCH عام unrestricted browser نہیں ہے؛ navigation کو متعلقہ platform کے allowed domains تک محدود رکھا گیا ہے۔\n\n" +
            "SEARCH\n\n" +
            "YouTube کا search صرف YouTube search استعمال کرتا ہے، Facebook کا search Facebook search استعمال کرتا ہے، اور TikTok کا search TikTok search استعمال کرتا ہے۔ Search box میں لفظ لکھیں اور SEARCH دبائیں، یا keyboard کا Search بٹن استعمال کریں۔\n\n" +
            "ABOUT\n\n" +
            "About section میں SOCIAL WATCH کے بنیادی features اور استعمال کی معلومات دی گئی ہیں۔ Main screen پر ABOUT button سب سے نیچے موجود ہے۔\n\n" +
            "DEVICE SUPPORT\n\n" +
            "یہ Android app Android 5.0 (API 21) اور اس سے اوپر کے لیے تیار کی گئی ہے۔ پرانے devices پر YouTube، Facebook یا TikTok کی website compatibility device اور platform کے مطابق مختلف ہو سکتی ہے۔\n\n" +
            "اہم بات\n\n" +
            "SOCIAL WATCH کے لیے internet connection ضروری ہے۔ Wi-Fi یا Mobile Data بند ہونے کی صورت میں online short videos نہیں چل سکیں گی۔\n\n" +
            "SOCIAL WATCH\nSimple • Fast • Focused",
            15, Color.WHITE, false
        ));

        scroll.addView(body, new ScrollView.LayoutParams(-1, -2));
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView back = make("←  BACK", 14, Color.WHITE, true);
        back.setGravity(Gravity.CENTER);
        back.setBackgroundResource(com.socialwatch.R.drawable.bg_about);
        back.setOnClickListener(v -> finish());
        root.addView(back, new LinearLayout.LayoutParams(dp(105), dp(40)));

        setContentView(root);
    }
}
