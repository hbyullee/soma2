package com.swmaestro.vocaapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	ListView		 	lvSearch	;
	Button 				btnSearch	;
	EditText			editSearch	;
	SearchAdapter 		adptSearch	;
	ArrayList<String> 	dataList	;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lvSearch  		= (ListView) findViewById(R.id.search_list_item);
		btnSearch		= (Button) 	 findViewById(R.id.item_search_btn)	;
		editSearch		= (EditText) findViewById(R.id.item_search_edit);
		adptSearch		= new SearchAdapter(this);
		dataList 		= getDataList();
		
		adptSearch.setData(dataList);
		lvSearch  .setAdapter(adptSearch);
		btnSearch .setOnClickListener(this);
		
	    
	
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.item_search_btn:
			if(checkValue()){
				String dataText	= editSearch.getText().toString();
				
				dataText += " > ";
				dataText += searchVoca(editSearch.getText().toString());
				setData(dataText);
			}
			break;

		default:
			break;
		}
	}
	
	private ArrayList<String> getDataList(){
		ArrayList<String> 	dataList 	= new ArrayList<String>();
		SharedData 			sharedData  = new SharedData(this);
	    
		for(int i = 0 ; i < sharedData.getInt("dataSize") ; i++){
	    	dataList.add(sharedData.getString("data_"+Integer.toString(i)));
	    }
	    
		return	dataList ;
	}
	
	private void setData(String data){
		SharedData 			sharedData  = new SharedData(this);
		sharedData.setString("data_"+dataList.size(), data);
	    sharedData.setInt("dataSize", dataList.size());
	    
	    adptSearch.addData(data);
	    adptSearch.notifyDataSetChanged();
	    
	}
	
	private Boolean checkValue(){
		if(editSearch.getText().length()>1){
		    return true;
		}else{
			Toast.makeText(this, "단어를 입력해주세요.", Toast.LENGTH_LONG);
			return false;
		}
	}
	
	private String searchVoca(String english){
		String addr 	= "http://dic.daum.net/search.do?q="+english;
		String  doc 	= "";
		try {
			 URL 			   url 	= new URL(addr);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		     
		     if (conn != null) {
			     conn.setConnectTimeout(10000);
			     conn.setUseCaches(false);
			     
			     if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { 
				     BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				     while (true) {
					     
				    	 String line = br.readLine();
					     if (line == null)  break;
					     doc = doc + line + "\n";
				     }
				     br.close();
			     }
			     conn.disconnect();
		     }
		} catch (Exception e) {
		     
		}
		 return parseKorean(doc);
		 
	
	}
	
	private String parseKorean(String html) {
		  int itemtype = 0;
		  String itemText = "";

		  try {
			   XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			   XmlPullParser 		parser 	= factory.newPullParser();
			   parser.setInput(new StringReader(html));
			   
			   int eventType = parser.getEventType();
			   while (eventType != XmlPullParser.END_DOCUMENT) {
				    switch (eventType) {
				    case XmlPullParser.START_DOCUMENT:
				    	break;
				    case XmlPullParser.END_DOCUMENT:
				    	break;
				    case XmlPullParser.START_TAG:
				    	if (parser.getName().indexOf("txt_means_KUEK")>-1) {
				    		itemtype = 1;
				    	}
				    case XmlPullParser.END_TAG:
				    	break;
				    case XmlPullParser.TEXT:
				    	if (itemtype == 1) {
				    		itemText = itemText + parser.getText() + "\n";
				    		itemtype = 0;
				    	}
				     break;
				    }
				    eventType = parser.next();
			   }
		  } catch (Exception e) { }
		  return itemText;
	}


	
	
	
	
	
	
	
	
	











}
