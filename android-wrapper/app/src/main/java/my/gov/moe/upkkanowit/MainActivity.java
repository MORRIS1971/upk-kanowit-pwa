package my.gov.moe.upkkanowit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
  private WebView web;

  // Confirmed V2 development deployment; production backend is not changed.
  private static final String APP_URL =
      "https://script.google.com/macros/s/AKfycbzQJRfDghFrKUzcolSou6wg4lxp16lWZglo-vSAY_QsxNJRUEYrDBP1P89EttRtAL3z/exec";

  @Override
  public void onCreate(Bundle state) {
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

    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.setAcceptThirdPartyCookies(web, true);

    WebSettings settings = web.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setJavaScriptCanOpenWindowsAutomatically(true);

    // Use the page's viewport meta tag and mobile CSS media queries.
    settings.setUseWideViewPort(false);
    settings.setLoadWithOverviewMode(false);
    settings.setTextZoom(100);
    settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

    // Initial scale is a WebView method, not a WebSettings method.
    web.setInitialScale(0);

    settings.setBuiltInZoomControls(false);
    settings.setDisplayZoomControls(false);
    settings.setAllowFileAccess(false);
    settings.setAllowContentAccess(true);

    // Keep target=_blank/window.open in this WebView for URL routing below.
    settings.setSupportMultipleWindows(false);

    web.setWebChromeClient(new WebChromeClient());
    web.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(
          WebView view, WebResourceRequest request) {
        return routeUrl(request.getUrl());
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return routeUrl(Uri.parse(url));
      }
    });

    web.loadUrl(APP_URL);
  }

  private boolean routeUrl(Uri uri) {
    String scheme = uri.getScheme() == null
        ? "" : uri.getScheme().toLowerCase();
    String host = uri.getHost() == null
        ? "" : uri.getHost().toLowerCase();

    if ("mailto".equals(scheme) || "tel".equals(scheme)) {
      openExternal(uri);
      return true;
    }

    if ("https".equals(scheme) || "http".equals(scheme)) {
      // Keep Apps Script HTML Service redirects and Google sign-in internal.
      if ("script.google.com".equals(host)
          || "script.googleusercontent.com".equals(host)
          || "accounts.google.com".equals(host)) {
        return false;
      }
      openExternal(uri);
      return true;
    }

    return true;
  }

  private void openExternal(Uri uri) {
    try {
      startActivity(new Intent(Intent.ACTION_VIEW, uri));
    } catch (Exception ignored) {
      // No compatible browser, mail client or dialler is installed.
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
      web.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onDestroy() {
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