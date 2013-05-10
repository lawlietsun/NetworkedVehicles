/*
 * Copyright (c) 2010-11 Dropbox, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */


package com.example.mediasync3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

/**
 * Here we show getting metadata for a directory and downloading a file in a
 * background thread, trying to show typical exception handling and flow of
 * control for an app that downloads a file from Dropbox.
 */

@TargetApi(Build.VERSION_CODES.FROYO)
public class DownloadNote extends AsyncTask<Void, Long, Boolean> {

	private String LOCAL_NOTES_DIR = null;
	private String DROPBOX_NOTES_DIR = "/Notes/";
	private Context mContext;
    private final ProgressDialog mDialog;
    private DropboxAPI<?> mApi;
    private String mPath;
    private String[] fnames;
    private FileOutputStream mFos;
    private Integer pos;
    MediaActivity mActivity;
    String TAG = "DownloadNote";

    
    private boolean mCanceled;
    private Long mFileLen;
    private String mErrorMsg;
    Cursor mCursor;
    public Fragment mFrag;
    public View rootView;
    public ArrayAdapter<String> ad;
    private ArrayAdapter <CharSequence> adapterList;
    
    public DownloadNote(Integer pos, View rootView, Fragment frag, DropboxAPI<?> api,
            String dropboxPath) {
        // We set the context this way so we don't accidentally leak activities
       // mContext = context.getApplicationContext();
        this.mFrag = frag;
        this.rootView = rootView;
        this.pos = pos;

        mApi = api;
        mPath = dropboxPath;
        //mView = view;
        //mCursor = cursor;
        
        mActivity = (MediaActivity)mFrag.getActivity();
        //file = new File(mFrag.getActivity().getExternalFilesDir(null), "livecyclehireupdates.xml");
    	
        LOCAL_NOTES_DIR = mFrag.getActivity().getExternalFilesDir(null).getAbsolutePath();

        
        adapterList =
  			  new ArrayAdapter <CharSequence> (mFrag.getActivity(), android.R.layout.simple_spinner_item );
  			adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        mDialog = new ProgressDialog(mFrag.getActivity());
        mDialog.setMessage("Updating Media");
        mDialog.setButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mCanceled = true;
                mErrorMsg = "Canceled";

                if (mFos != null) {
                    try {
                        mFos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (mCanceled) {
                return false;
            }

            if (pos == -2) {
            int i = 0;
            fnames = null;
            Entry dirent = mApi.metadata(mPath, 1000, null, true, null);
            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                mErrorMsg = "File or empty directory";
                return false;
            }

            ArrayList<Entry> files = new ArrayList<Entry>();
            ArrayList<String> dir=new ArrayList<String>();
            for (Entry ent: dirent.contents) 
            {
                files.add(ent);// Add it to the list of thumbs we can choose from                       
                //dir = new ArrayList<String>();
                dir.add(new String(files.get(i++).fileName()));
            }
            i=0;
            fnames=dir.toArray(new String[dir.size()]);
            
            adapterList.clear();
            for (i = 0; i < fnames.length; i++) {
            	adapterList.add(fnames[i]);
            }
           // mFrag.getActivity()
            mActivity.setNotesNames(fnames);
            }
            
            
            
            else {
            	
            fnames = null;
         	fnames = mActivity.getNotesNames();
         	   
         	if (mCanceled) {
                return false;
            }
         	
            File	dir = new File(LOCAL_NOTES_DIR);
        	if (!dir.exists()) {	
        	if (dir.mkdir()) {}
        		else {}
        	}
        	
        	String notesPath = LOCAL_NOTES_DIR + "/" + fnames[pos];
            
    		File file = new File(notesPath);
            if (!file.exists())
            {
                if (mCanceled) {
                    return false;
                }
        try {
            mFos = new FileOutputStream(notesPath);
        } catch (FileNotFoundException e) {
            mErrorMsg = "Couldn't create a local file to store the music";
            return false;
        }

        mApi.getFile(DROPBOX_NOTES_DIR + fnames[pos], null, mFos, null);

        }
	        }
            
                      
            return true;

        } catch (DropboxUnlinkedException e) {
            // The AuthSession wasn't properly authenticated or user unlinked.
        } catch (DropboxPartialFileException e) {
            // We canceled the operation
            mErrorMsg = "Download canceled";
        } catch (DropboxServerException e) {
            // Server-side exception.  These are examples of what could happen,
            // but we don't do anything special with them here.
            if (e.error == DropboxServerException._304_NOT_MODIFIED) {
                // won't happen since we don't pass in revision with metadata
            } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                // Unauthorized, so we should unlink them.  You may want to
                // automatically log the user out in this case.
            } else if (e.error == DropboxServerException._403_FORBIDDEN) {
                // Not allowed to access this
            } else if (e.error == DropboxServerException._404_NOT_FOUND) {
                // path not found (or if it was the thumbnail, can't be
                // thumbnailed)
            } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
                // too many entries to return
            } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
                // can't be thumbnailed
            } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                // user is over quota
            } else {
                // Something else
            }
            // This gets the Dropbox error, translated into the user's language
            mErrorMsg = e.body.userError;
            if (mErrorMsg == null) {
                mErrorMsg = e.body.error;
            }
        } catch (DropboxIOException e) {
            // Happens all the time, probably want to retry automatically.
            mErrorMsg = "Network error.  Try again.";
        } catch (DropboxParseException e) {
            // Probably due to Dropbox server restarting, should retry
            mErrorMsg = "Dropbox error.  Try again.";
        } catch (DropboxException e) {
            // Unknown error
            mErrorMsg = "Unknown error.  Try again.";
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Long... progress) {
        int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
        mDialog.setProgress(percent);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mDialog.dismiss();
        
       if (pos == -2) { 
    	   Spinner listSpin = (Spinner) rootView.findViewById(R.id.spinner1);
  	      listSpin.setAdapter(adapterList);
          listSpin.setEnabled(true);
       
       }
       else {
           
    	   
    	  EditText et = (EditText)rootView.findViewById(R.id.editText1);
    	  File fl = new File(LOCAL_NOTES_DIR + "/" + fnames[pos]);
    	  et.setText(readFromFile(LOCAL_NOTES_DIR + "/" + fnames[pos]));
 	      
    	  /*
    	   ListView lv = (ListView)rootView.findViewById(R.id.listView1);
    	   ArrayList<String> dir=new ArrayList<String>();
  	     
  		 //File sdCardRoot = Environment.getExternalStorageDirectory();
  		 final File MusicDir = new File(LOCAL_NOTES_DIR);
  		 for (File f : MusicDir.listFiles()) {
  		     if (f.isFile())
  		    	 dir.add(f.getName());
  		    	 // Do your stuff
  		 }
  		 String[] localnames = dir.toArray(new String[dir.size()]);
  		 
  		 ArrayAdapter<String> ad = new ArrayAdapter<String>(mFrag.getActivity(), android.R.layout.simple_list_item_1,localnames);
         lv.setAdapter(ad);  
    	   
    	   
    	   //TextView tv = (TextView) rootView.findViewById(R.id.textView1);
    	   //tv.setText(fnames[pos]);
    	*/   
       }
        
    	   
        if (result) {
            // Set the image now that we have it
        //    mView.setImageDrawable(mDrawable);
        } else {
            // Couldn't download it, so show an error
        //    showToast(mErrorMsg);
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(mFrag.getActivity(), msg, Toast.LENGTH_LONG);
        error.show();
    }

    
    private String readFromFile(String FILENAME) {
        
        String ret = "";
         
        try {
            //InputStream inputStream = mFrag.getActivity().openFileInput(FILENAME);
            //InputStream inputStream = mFrag.getActivity()
            
            
            FileInputStream fIn = new FileInputStream(FILENAME);
           
            if ( fIn != null ) {
                
            //if ( inputStream != null ) {
                //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(fIn));
                
                
                
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                 
                fIn.close();
                //inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
 
        return ret;
    }
    
    
    
    

}
