package my.gov.moe.upkkanowit;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.KeyEvent;

public class MainActivity extends Activity {
  private WebView web;
  private static final String APP_URL = "https://script.google.com/macros/s/AKfycbzQJRfDghFrKUzcolSou6wg4lxp16lWZglo-vSAY_QsxNJRUEYrDBP1P89EttRtAL3z/exec";
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    CookieManager.getInstance().setAcceptCookie(true);
    CookieManager.getInstance().setAcceptThirdPartyCookies(createWebView(), true);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setDomStorageEnabled(true);
    web.getSettings().setDatabaseEnabled(true);
    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    web.getSettings().setSupportMultipleWindows(true);
    web.setWebChromeClient(new WebChromeClient());
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String scheme = request.getUrl().getScheme();
        return !("https".equalsIgnoreCase(scheme));
      }
    });
    web.loadUrl(APP_URL);
  }
  private WebView createWebView() { web = new WebView(this); setContentView(web); return web; }
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) { web.goBack(); return true; }
    return super.onKeyDown(keyCode, event);
  }
}
