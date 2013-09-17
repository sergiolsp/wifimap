package com.adekz.wifiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public String TAG = "WIFIMAP";
	
	WifiManager wifi;       
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;
    
    boolean pause=false;

    public static ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textStatus = (TextView) findViewById(R.id.info);
        buttonScan = (Button) findViewById(R.id.scan_button);
        buttonScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pause){
					pause=false;     
					buttonScan.setText("Pause");
				}else{
					pause=true;     
					buttonScan.setText("Play");
				}
			}
		});
		
        lv = (ListView)findViewById(R.id.list_wifi);
        
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox box = (CheckBox) view.findViewById(R.id.checkbox);

                if (box.isChecked()){
                    box.setChecked(false);
                    
                }else{
                    box.setChecked(true);
                    
                };
                
                configuraRoteador(position);
            }
        });

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }   

	}
	
	public void configuraRoteador(int position){
		if(Map.roteadores!=null && Map.roteadores.containsKey(arraylist.get(position).get("bssid"))){
			Map.roteadores.remove(arraylist.get(position).get("bssid"));
		}else{
			Map.roteadores.put(arraylist.get(position).get("bssid"), position);
		}
	}
	
	public void showList(){
		if(!pause){
			arraylist.clear();          
	        wifi.startScan();
	
	        Toast.makeText(MainActivity.this, "Atualizado: " + size, Toast.LENGTH_SHORT).show();
	        try 
	        {
	            size = size - 1;
	            while (size >= 0) 
	            {   
	                HashMap<String, String> item = new HashMap<String, String>();                       
	                item.put("ssid", results.get(size).SSID);
	                item.put("level", String.valueOf(results.get(size).level));
	                item.put("freq", "Freq: "+String.valueOf(results.get(size).frequency));
	                item.put("bssid", String.valueOf(results.get(size).BSSID));
	                
	                Log.v(TAG, "  BSSID       =" + results.get(size).BSSID);
	                Log.v(TAG, "  SSID        =" + results.get(size).SSID);
	                Log.v(TAG, "  Capabilities=" + results.get(size).capabilities);
	                Log.v(TAG, "  Frequency   =" + results.get(size).frequency);
	                Log.v(TAG, "  Level       =" + results.get(size).level);
	                Log.v(TAG, "---------------");
	                
	                arraylist.add(item);
	                size--;
	                refreshList();
	                //adapter.notifyDataSetChanged();                 
	            } 
	        }
	        catch (Exception e)
	        { }
		}
	}
	
	private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver()
	{
		@Override
        public void onReceive(Context c, Intent intent) 
        {
           Log.i(TAG, "disparou!!!");
           results = wifi.getScanResults();
           size = results.size();
           if(size>0){
        	   textStatus.setText("Disponíveis: "+size);
        	   showList();
           }
        }
	};
	
	public void refreshList(){
		
		Collections.sort(arraylist, new Comparator<HashMap< String,String >>() {

	        @Override
	        public int compare(HashMap<String, String> first,
	                HashMap<String, String> second) {
	            
	        	String firstValue = first.get("level");
	            String secondValue = second.get("level");
	        	
	        	return firstValue.compareTo(secondValue);
	        }
	    });
		
		SimpleAdapter sa = new SimpleAdapter(MainActivity.this, arraylist, R.layout.grid_item, new String[] { "ssid", "level", "freq" }, new int[] { R.id.item1, R.id.item2, R.id.item3 });
        lv.setAdapter(sa);
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();
	    registerReceiver(mNetworkReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); //network info
	}
	
	@Override
	public void onPause()
	{
	    super.onPause();
	    unregisterReceiver(mNetworkReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void abreMapa(){
		 Intent intent = new Intent(this, Map.class);
		 startActivity(intent);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case R.id.map:
	        
	    	abreMapa();
	        return true;
        
        case R.id.sair:
	        // Fecha a aplicacao
	    	System.exit(0);
	        return true;
        }
	    
        return false;
    }

}
