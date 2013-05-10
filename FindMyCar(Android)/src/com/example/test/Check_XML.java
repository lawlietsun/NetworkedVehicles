package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class Check_XML extends AsyncTask<Void,Void,String>{
String str = null;
	
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://networkedvehicles.azurewebsites.net/getLastLocation.php");
		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("idVehicle", "11"));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
	            HttpEntity entity = response.getEntity();
	            InputStream is = entity.getContent();
	            
	            StringBuffer buffer = new StringBuffer();
	            byte[] b = new byte[4096];
	            for (int n; (n = is.read(b)) != -1;) {
	              buffer.append(new String(b, 0, n));
	            }
	            str = buffer.toString();
	           // System.out.println(str);
	            return (String)str;
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	return null;
		    } catch (IOException e) {
		    	return null;
		        // TODO Auto-generated catch block
		    }
	}
}
