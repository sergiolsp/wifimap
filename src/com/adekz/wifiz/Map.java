package com.adekz.wifiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class Map extends Activity {
	
	public String TAG = "WIFIMAP";
	
	WebView webView;
	
	public static HashMap<String, Integer> roteadores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		roteadores = new HashMap<String, Integer>();
		
		webView = (WebView)findViewById(R.id.profile_webView);
		WebSettings s = webView.getSettings();
		webView.setBackgroundColor(0x00FF0000);
		webView.setInitialScale(1);
		s.setBuiltInZoomControls(true);
		// still displays zoom controls on some devices, see
		// http://stackoverflow.com/questions/5125851/enable-disable-zoom-in-android-webview
		//s.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		s.setJavaScriptEnabled(true);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
			s.setDisplayZoomControls(false);
		}
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		
		
	    String url = "http://adekz.p.ht/map/wifi/mapeamento.php";
		
		webView.loadUrl(url);
		
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		
		webView.setWebChromeClient(new WebChromeClient() {
	       @Override
	        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
	                    return super.onJsAlert(view, url, message, result);
	           }
	    });

	}
	
	
	
	@Override
	public void onResume()
	{
	    super.onResume();
	}
	
	@Override
	public void onPause()
	{
	    super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case R.id.sair:
	        // Fecha a aplicacao
	    	finish();
	        return true;
        }
	    
        return false;
    }

}
