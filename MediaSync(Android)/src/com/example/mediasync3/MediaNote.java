package com.example.mediasync3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MediaNote extends Fragment{


MediaActivity mActivity;	
private final String NOTES_DIR = "/Notes/";
private String LOCAL_NOTES_DIR = "";
private ArrayAdapter <CharSequence> adapterList;

//Button mUpMusic;	
FileDialog fileDialog;
File selectedUpFile;
Integer pos, clickCounter = 0, getListNumber = -2, notesPos;
String[] fnames, localnames = null;
String[] extensions = new String[2];
//ArrayAdapter<String> ad = null;

// widgets
ListView lv;
public View v1;
Spinner spinList;
EditText et;

	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	    		Bundle savedInstanceState) {
	    
		   v1 = inflater.inflate(R.layout.page3, container,false);
	       Button mNewNotes = (Button)v1.findViewById(R.id.upnotes_button);
	       Button mGetNotes = (Button)v1.findViewById(R.id.getnotes_button);
	       Button mSaveNotes = (Button)v1.findViewById(R.id.save_button);
	       spinList = (Spinner)v1.findViewById(R.id.spinner1);
	       et = (EditText)v1.findViewById(R.id.editText1);
	       
	       mActivity = (MediaActivity)getActivity();
	       LOCAL_NOTES_DIR = mActivity.getLocalMusicDir();
		   
	       File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
           fileDialog = new FileDialog(getActivity(), mPath);
           //spinList.setBackgroundColor(Color.);
        
           extensions[0] = "txt";
		      //extensions[1] = "wma";
		      //extensions[2] = "bmp";
		   
		      mNewNotes.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // upload file
		        	  UploadFile();
		        	 // Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		        	  //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
		        		//	    .getPackage().getName());
		          }
		      });
		      
		      mSaveNotes.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // upload file
		        	 // showList();
		        	  SaveNote();
		          }
		      });
		  
		      mGetNotes.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // get file
		  			DownloadFile(getListNumber);
			      }
		      });
		      
		      
		      spinList.setEnabled(false);
		      spinList.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	    public void onItemSelected(AdapterView<?> parent, View view, int pos,
		    	            long id) {
		    	        clickCounter++;
		    	        
		    	        if ((spinList.isEnabled()) && (clickCounter > 1)) {
		    	        DownloadFile(pos);     
		    	        }
		    	    }

		    	    public void onNothingSelected(AdapterView<?> parent) {

		    	    }
		    	});

		      adapterList =
		  			  new ArrayAdapter <CharSequence> (getActivity(), android.R.layout.simple_spinner_item );
		  			adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		   
		   return v1;
	 }
	 
     private void DownloadFile(Integer pos) {
		 
		 DownloadNote download = new DownloadNote(pos, v1, this, mActivity.mApi, NOTES_DIR);
         download.execute();
         notesPos = pos;
        // showList();
		 		 
	 }

     
     private void SaveNote() {
			// TODO Auto-generated method stub
  
  fnames = mActivity.getNotesNames();
  writeToFile(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + fnames[notesPos], et.getText().toString()); 
  
  
  selectedUpFile = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + fnames[notesPos]);
  Upload upload = new Upload(getActivity(), mActivity.mApi, NOTES_DIR, 
		              selectedUpFile);
  upload.execute();  	
	
     }

     private void UploadFile() {
			// TODO Auto-generated method stub
	
  }
     
     /*
     @Override
 	public void setUserVisibleHint(boolean isVisibleToUser) {
 	super.setUserVisibleHint(isVisibleToUser);

 	if (isVisibleToUser == true) {
 	
 		//clickCounter++;
 		//TextView tv = (TextView)v1.findViewById(R.id.textView2);
	    //tv.setText(Integer.toString(clickCounter));
	    
 		if (mActivity.getNotesNames() != null) {
		 		fnames = mActivity.getNotesNames();
		 		
		 		//clickCounter++;
		 		//TextView tv = (TextView)v1.findViewById(R.id.textView2);
		 		//tv.setText(Integer.toString(clickCounter));
			     
		 		adapterList.clear();
		 		for (int i = 0; i < fnames.length; i++) {
		        	adapterList.add(fnames[i]);
		        }
		 	   Spinner listSpin = (Spinner) v1.findViewById(R.id.spinner1);
			      listSpin.setAdapter(adapterList);
		       listSpin.setEnabled(true);
		 		}
 		
 	}
 	else if (isVisibleToUser == false) {  }

 	}
     
     */
 
     
     private void writeToFile(String FILENAME, String data) {
         try {
        	 
        	 FileOutputStream fOut = new FileOutputStream(FILENAME);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
             
             //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput(FILENAME, getActivity().MODE_PRIVATE));
             //outputStreamWriter.append(data);
             outputStreamWriter.write(data);
             outputStreamWriter.close();
         }
         catch (IOException e) {
             Log.e("MediaNote", "File write failed: " + e.toString());
         } 
          
     }
}
