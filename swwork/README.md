# SOCIAL WATCH — Short Video Browser

A lightweight Android app shell designed for older Android devices.

## Target
- Minimum Android version: **Android 5.0 / API 21**
- Main screen intentionally has no general browser.
- Three entry points only: YouTube Shorts, Facebook Reels, TikTok.
- YouTube and Facebook use Android WebView.
- TikTok uses a desktop-style User-Agent.
- Each platform has a small in-app back button.
- About page is included.
- Default short/reel URLs are editable in `Config.java`.

## Important compatibility note
The app itself targets Android 5.0+, but YouTube/Facebook/TikTok control their own websites and may change browser/WebView requirements. Very old system WebViews may therefore have limitations. An updated Android System WebView, when available for the device, can improve compatibility.

## Change the default video
Edit:
`app/src/main/java/com/socialwatch/Config.java`

Replace the three `DEFAULT_*_URL` values with your preferred direct Short/Reel/Video links.

## Build on GitHub
1. Upload this project to a GitHub repository.
2. Open **Actions**.
3. Run **Build Android APK**.
4. Download the generated APK from the workflow artifact.

The included GitHub Actions workflow uses Gradle 8.7 and JDK 17.

## Package
Application id: `com.socialwatch`

\n## Home screen artwork
The Home screen uses cropped artwork from the supplied reference image for the header and the three service buttons, so the visual design stays faithful to the reference.
