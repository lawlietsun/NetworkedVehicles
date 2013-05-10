package com.example.test;

import java.io.IOException;


import java.io.StringReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.example.test.HelloItemizedOverlay;
import com.example.test.R;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;

public class WhereAmI extends MapActivity{

	MapController mControl;
	GeoPoint GeoP;
	MapView mapV;
	Location location;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_location);
		
		Check_XML xml = new Check_XML();
		try {
			xml.execute();
			String x = xml.get();
			double lat = getLatitude(x);
			double longi = getLongitude(x);
			System.out.println(lat +","+longi);
			//GeoP = new GeoPoint ((int)(51.539536*1E6), (int)(-0.1392989*1E6));
			GeoP = new GeoPoint ((int)(lat*1E6), (int)(longi*1E6));
			mapV = (MapView) findViewById(R.id.mapView);
			mapV.displayZoomControls(true);
			mapV.setBuiltInZoomControls(true);
			List<Overlay> mapOverlays = mapV.getOverlays();
			Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
			HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
			OverlayItem overlayitem = new OverlayItem(GeoP, null, null);
		    itemizedoverlay.addOverlay(overlayitem);
		    mapOverlays.add(itemizedoverlay);
			mControl = mapV.getController();
			mControl.animateTo(GeoP);
			mControl.setZoom(18);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//ArrayList<Double> location = new ArrayList<Double>();
		//location = getXML.getLocationInfo(xml)		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public double getLatitude(String xml){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        double finalLat =0.0;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList lat = doc.getElementsByTagName("latitude");

            Element latitude = (Element) lat.item(0);
            
            finalLat = Double.parseDouble(getCharacterDataFromElement(latitude));
            //System.out.println(finalLat + ","+ finalLong);
            
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
        return finalLat;
    }
	
	public double getLongitude(String xml){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        double finalLong =0.0;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList lat = doc.getElementsByTagName("longitude");
            Element longitude = (Element) lat.item(0);
            finalLong = Double.parseDouble(getCharacterDataFromElement(longitude));
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
        return finalLong;
    }
	
	public String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	
}
