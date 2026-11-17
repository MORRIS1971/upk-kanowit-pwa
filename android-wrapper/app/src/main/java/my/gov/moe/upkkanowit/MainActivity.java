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
import android.view.Window;\nimport org.json.JSONObject;

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
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // Final mobile layout guard: correct horizontal overflow even when the hosted
        // Apps Script HTML contains desktop-sized children or restrictive min-widths.
        String mobileCss =
          "html,body{width:100%!important;max-width:100%!important;margin:0!important;padding:0!important;overflow-x:hidden!important}" +
          "*,*:before,*:after{box-sizing:border-box!important;min-width:0}" +
          ".layout{display:flex!important;flex-direction:column!important;width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:hidden!important}" +
          ".side{width:100%!important;max-width:100%!important;min-width:0!important}" +
          ".nav{display:flex!important;max-width:100%!important;overflow-x:auto!important;overscroll-behavior-x:contain}" +
          ".nav button{flex:0 0 auto!important;white-space:nowrap!important}" +
          "main,.top,.content{display:block;float:none!important;width:100%!important;max-width:100%!important;min-width:0!important;margin-left:0!important;margin-right:0!important}" +
          ".top{display:flex!important;flex-wrap:wrap!important;align-items:center!important;gap:8px!important;padding:10px 12px!important}" +
          ".content{padding:14px 12px 28px!important;overflow-x:hidden!important}" +
          ".hero,.panel,.stat,.portal,.request,.notice,.foot{width:100%!important;max-width:100%!important;min-width:0!important;overflow-wrap:anywhere!important;word-break:normal!important}" +
          ".hero{padding:20px 16px!important}" +
          ".hero h1,.hero p,.section h2,.section p,.portal h3,.request h4,.request p{max-width:100%!important;white-space:normal!important;overflow-wrap:anywhere!important;word-break:normal!important}" +
          ".stats,.grid,.formgrid{display:grid!important;grid-template-columns:minmax(0,1fr)!important;width:100%!important;max-width:100%!important;min-width:0!important}" +
          ".stat,.portal,.request{padding:13px!important}" +
          ".request h4{font-size:15px!important;line-height:1.45!important}" +
          ".request p{font-size:14px!important;line-height:1.7!important}" +
          ".portal .actions,.request .actions,.tools,.section,.foot{max-width:100%!important;flex-wrap:wrap!important}" +
          "input,select,textarea,button,a{max-width:100%!important;overflow-wrap:anywhere!important}" +
          ".modalback{overflow-y:auto!important}.modal{width:100%!important;max-width:100%!important}";
        String script = "(function(){var id='upk-android-mobile-fix';var s=document.getElementById(id);if(!s){s=document.createElement('style');s.id=id;(document.head||document.documentElement).appendChild(s);}s.textContent=" +
          JSONObject.quote(mobileCss) + ";document.documentElement.style.setProperty('overflow-x','hidden','important');document.body.style.setProperty('overflow-x','hidden','important');})();";
        view.evaluateJavascript(script, null);
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
