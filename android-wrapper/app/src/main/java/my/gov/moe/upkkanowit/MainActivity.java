package my.gov.moe.upkkanowit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Color;
import android.view.Window;

public class MainActivity extends Activity {
  private WebView web;
  private static final String APP_URL = "https://script.google.com/macros/s/AKfycbzQJRfDghFrKUzcolSou6wg4lxp16lWZglo-vSAY_QsxNJRUEYrDBP1P89EttRtAL3z/exec";

  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    Window window = getWindow();
    window.setStatusBarColor(Color.rgb(23, 54, 93));
    window.setNavigationBarColor(Color.rgb(23, 54, 93));

    web = new WebView(this);
    web.setLayoutParams(new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ));
    setContentView(web);

    CookieManager cookies = CookieManager.getInstance();
    cookies.setAcceptCookie(true);
    cookies.setAcceptThirdPartyCookies(web, true);

    WebSettings settings = web.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setJavaScriptCanOpenWindowsAutomatically(true);
    // Keep target="_blank" portal links in this WebView.
    settings.setSupportMultipleWindows(false);
    // Use the physical device width as the CSS layout viewport so portrait media queries activate.
    settings.setUseWideViewPort(false);
    settings.setLoadWithOverviewMode(false);
    settings.setTextZoom(100);
    settings.setBuiltInZoomControls(false);
    settings.setDisplayZoomControls(false);
    settings.setAllowFileAccess(false);
    settings.setAllowContentAccess(true);

    web.setWebChromeClient(new WebChromeClient());
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return routeUrl(request.getUrl().toString());
      }
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return routeUrl(url);
      }
    });
    web.loadUrl(APP_URL);
  }

  private boolean routeUrl(String value) {
    Uri uri = Uri.parse(value);
    String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase();
    if ("https".equals(scheme) || "http".equals(scheme)) {
      return false;
    }
    if ("mailto".equals(scheme) || "tel".equals(scheme)) {
      try {
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
      } catch (Exception ignored) { }
      return true;
    }
    return true;
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
      web.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override protected void onDestroy() {
    if (web != null) {
      web.stopLoading();
      web.setWebChromeClient(null);
      web.setWebViewClient(null);
      web.destroy();
      web = null;
    }
    super.onDestroy();
  }
}
