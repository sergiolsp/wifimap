package com.adekz.wifiz;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public String setaPosicao(String x, String y) {
    	String ret="";
    	
    	try {
			JSONObject jo = montaRetorno2(x,y);
			if(jo!=null){
				ret = jo.toString();
			}else{
				ret = "Roteadores nao configurados corretamente.";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Toast.makeText(mContext, "X="+x+",Y="+y+"\nres: "+ret, Toast.LENGTH_SHORT).show();
        
        return ret;
    }
    
    public JSONObject montaRetorno() throws JSONException{
		JSONObject jo= new JSONObject();
		
		if(Map.roteadores!=null && Map.roteadores.size()<3){
			Toast.makeText(mContext, "Escolha pelo menos 3 roteadores.", Toast.LENGTH_SHORT).show();
			jo=null;
		}else{
			JSONArray ja = new JSONArray();
			
			for (String r : Map.roteadores.keySet()) {
				JSONObject jj = new JSONObject();
				jj.put("roteador", r);
				jj.put("level", MainActivity.arraylist.get(Map.roteadores.get(r)).get("level"));
				ja.put(jj);
			}
			jo.put("roteadores", ja);
		}
		
		return jo;
	}
    
    public JSONObject montaRetorno2(String x, String y) throws JSONException{
		JSONObject jo= new JSONObject();
		
		Log.d("WIFIMAP", "Rots:"+MainActivity.arraylist.size());
		
		if(MainActivity.arraylist.size()>0){
			JSONArray ja = new JSONArray();
			
			for (HashMap<String,String> r : MainActivity.arraylist) {
				JSONObject jj = new JSONObject();
				
				if(Integer.valueOf(r.get("level"))>-80){
					jj.put("roteador", r.get("bssid"));
					jj.put("level", r.get("level"));
					ja.put(jj);
					Log.d("WIFIMAP", "bssid:"+r.get("bssid"));
				}
			}
			//monta posicao
			JSONObject jop = new JSONObject();
			jop.put("x", x);
			jop.put("y", y);
			
			jo.put("posicao", jop);
			jo.put("roteadores", ja);
		}else{
			jo=null;
		}
		
		return jo;
	}
}