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
import org.json.JSONObject;

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
        // Keep the existing desktop layout untouched. Apply responsive rules only on
        // narrow screens, using the CSS viewport rather than physical screen pixels.
        String script =
          "(function(){" +
          "var vp=document.querySelector('meta[name=viewport]');" +
          "if(!vp){vp=document.createElement('meta');vp.name='viewport';(document.head||document.documentElement).appendChild(vp);}" +
          "vp.setAttribute('content','width=device-width, initial-scale=1');" +
          "var id='upk-phone-layout-v5',st=document.getElementById(id);" +
          "if(!st){st=document.createElement('style');st.id=id;(document.head||document.documentElement).appendChild(st);}" +
          "st.textContent=" +
          "'@media screen and (max-width:820px){'+ " +
          "'html,body{width:100%!important;max-width:100%!important;min-width:0!important;margin:0!important;overflow-x:hidden!important}'+ " +
          "'*,*:before,*:after{box-sizing:border-box!important}'+ " +
          "'.layout{display:flex!important;flex-direction:column!important;width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:hidden!important}'+ " +
          "'.side{display:flex!important;flex-direction:column!important;width:100%!important;max-width:100%!important;min-width:0!important;padding:10px 12px!important;gap:9px!important}'+ " +
          "'.brand{width:100%!important;max-width:100%!important;min-width:0!important;padding:3px 4px 10px!important}'+ " +
          "'.navlabel,.sidefoot{display:none!important}'+ " +
          "'.nav{display:flex!important;flex-direction:row!important;flex-wrap:nowrap!important;width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:auto!important;overflow-y:hidden!important}'+ " +
          "'.nav button{flex:0 0 auto!important;white-space:nowrap!important}'+ " +
          "'main{display:block!important;width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:hidden!important}'+ " +
          "'.top{width:100%!important;max-width:100%!important;min-width:0!important;display:flex!important;flex-wrap:wrap!important;gap:8px!important;padding:10px 12px!important}'+ " +
          "'.content{width:100%!important;max-width:100%!important;min-width:0!important;margin:0!important;padding:14px 12px 28px!important;overflow-x:hidden!important}'+ " +
          "'.hero,.panel,.stat,.portal,.request,.notice,.foot{width:100%!important;max-width:100%!important;min-width:0!important;margin-left:0!important;margin-right:0!important}'+ " +
          "'.hero{padding:20px 16px!important;overflow:hidden!important}'+ " +
          "'.hero h1,.hero p,.section h2,.section p,.portal h3,.request h4,.request p{max-width:100%!important;white-space:normal!important;overflow-wrap:anywhere!important;word-break:normal!important}'+ " +
          "'.stats,.grid,.formgrid{display:grid!important;grid-template-columns:minmax(0,1fr)!important;width:100%!important;max-width:100%!important;min-width:0!important;gap:10px!important}'+ " +
          "'.stat,.portal,.request{padding:13px!important}'+ " +
          "'.request p{font-size:14px!important;line-height:1.7!important}'+ " +
          "'.request h4{font-size:15px!important;line-height:1.45!important}'+ " +
          "'.section{display:flex!important;flex-direction:column!important;align-items:flex-start!important;max-width:100%!important}'+ " +
          "'.tools,.foot,.portal .actions,.request .actions{display:flex!important;flex-wrap:wrap!important;max-width:100%!important}'+ " +
          "'.search,.filter,.field,input,select,textarea{width:100%!important;max-width:100%!important;min-width:0!important}'+ " +
          "'.modalback{overflow-y:auto!important}.modal{width:100%!important;max-width:100%!important}'+ " +
          "'.state{max-width:100%!important;overflow-wrap:anywhere!important}'+ " +
          "'}';" +
          "})();";
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
