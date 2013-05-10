package com.example.mediasync3;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MediaMusic extends Fragment {

//DropboxAPI<AndroidAuthSession> mApi;
//private AndroidAuthSession session;
MediaActivity mActivity;	
private final String MUSIC_DIR = "/Music/";
private String LOCAL_MUSIC_DIR = "";

//Button mUpMusic;	
FileDialog fileDialog;
File selectedUpFile;
Integer pos, clickCounter = 0, getListNumber = -2;
String[] fnames, localnames = null;
String[] extensions = new String[2];
//ArrayAdapter<String> ad = null;

// widgets
ListView lv;
public View v1;
Spinner spinList;

//private MediaCursorAdapter mediaAdapter = null;
private static final int UPDATE_FREQUENCY = 500;
private static final int STEP_VALUE = 4000;

private TextView selectedFile, testtext;
private SeekBar seekbar = null;
private MediaPlayer player = null;
private ImageButton playButton = null;
private ImageButton prevButton = null;
private ImageButton nextButton = null;


private boolean isStarted = true;
private String currentFile = "";
private boolean isMoveingSeekBar = false;
 
private final Handler handler = new Handler();

private String[] mFileList;
private File mPath;
private String mChosenFile;
private static final String FTYPE = ".mp3";    
private static final int DIALOG_LOAD_FILE = 1000;
final int ACTIVITY_CHOOSE_FILE = 1;


private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
                updatePosition();
        }
};

	 public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	    		Bundle savedInstanceState) {
	    
		   v1 = inflater.inflate(R.layout.page1, container,false);
	       Button mUpMusic = (Button)v1.findViewById(R.id.upmusic_button);
	       Button mGetMusic = (Button)v1.findViewById(R.id.getmusic_button);
	       Button mPlaylist = (Button)v1.findViewById(R.id.playlist_button);
	       lv = (ListView)v1.findViewById(R.id.listView1);
	       spinList = (Spinner)v1.findViewById(R.id.spinner1);
	       
	       mActivity = (MediaActivity)getActivity();
	       LOCAL_MUSIC_DIR = mActivity.getLocalMusicDir();
		   
	       File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
           fileDialog = new FileDialog(getActivity(), mPath);
           //spinList.setBackgroundColor(Color.);
           
		      mUpMusic.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // upload file
		        	  UploadFile();
		          }
		      });
		      
		      mPlaylist.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // upload file
		        	  showList();
		          }
		      });
		  
		  
		      mGetMusic.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // get file
		  			DownloadFile(getListNumber);
			      }
		      });
		      
		      spinList.setEnabled(false);
		      spinList.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	    public void onItemSelected(AdapterView<?> parent, View view, int pos,
		    	            long id) {

		    	        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
		    	        //((TextView) parent.getChildAt(0)).set
		    	        clickCounter++;
		    	        
		    	        if ((spinList.isEnabled()) && (clickCounter > 1)) {
		    	        DownloadFile(pos);     
		    	        }


		    	    }

		    	    public void onNothingSelected(AdapterView<?> parent) {

		    	    }
		    	});
		
		   
		      extensions[0] = "mp3";
		      extensions[1] = "wma";
		      //extensions[2] = "bmp";
		   
		      showList();
		      

		     
		  
		   
		   
		   
		   
		   
		   
		   selectedFile = (TextView)v1.findViewById(R.id.selectedfile);
	        //testtext = (TextView)findViewById(R.id.textView1);
	        
	        seekbar = (SeekBar)v1.findViewById(R.id.seekbar);
	        playButton = (ImageButton)v1.findViewById(R.id.play);
	        prevButton = (ImageButton)v1.findViewById(R.id.prev);
	        nextButton = (ImageButton)v1.findViewById(R.id.next);
	         
	        player = new MediaPlayer();
	         
	        player.setOnCompletionListener(onCompletion);
	        player.setOnErrorListener(onError);
	        seekbar.setOnSeekBarChangeListener(seekBarChanged);
	        
	        playButton.setOnClickListener(onButtonClick);
            nextButton.setOnClickListener(onButtonClick);
            prevButton.setOnClickListener(onButtonClick);

	        
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   return v1;
	 }

	 
	 private void UploadFile() {
		 
		 fileDialog.setFileEndsWith(".mp3");
         fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
             public void fileSelected(File file) {
                 Log.d(getClass().getName(), "selected file " + file.toString());
                 selectedUpFile = file;
                 
                 Upload upload = new Upload(getActivity(), mActivity.mApi, MUSIC_DIR, selectedUpFile);
                 upload.execute();
	             
             }
         });
         //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
         //  public void directorySelected(File directory) {
         //      Log.d(getClass().getName(), "selected dir " + directory.toString());
         //  }
         //});
         //fileDialog.setSelectDirectoryOption(false);
         fileDialog.showDialog();
   	 }
	 
	 
	 private void DownloadFile(Integer pos) {
		 
		 DownloadMusic download = new DownloadMusic(pos, v1, this, mActivity.mApi, MUSIC_DIR);
         download.execute();
         showList();
		 		 
	 }
	 
	 public void showList() {
		 
		 ArrayList<String> dir=new ArrayList<String>();
		 LOCAL_MUSIC_DIR = mActivity.getLocalMusicDir();
		
		 if (LOCAL_MUSIC_DIR != null) {
		 //selectedFile.setText("asd");
		 
		 }
		 TextView testtext = (TextView)v1.findViewById(R.id.textView3);
		    
		 boolean check = false;
		 dir.clear();
		 //File sdCardRoot = Environment.getExternalStorageDirectory();
		 final File MusicDir = new File(LOCAL_MUSIC_DIR);
		 
		 if (MusicDir.isDirectory()) {
		 for (File f : MusicDir.listFiles()) {
			 
		     if (f.isFile() && accept(f)){
		    	 dir.add(f.getName());
		    	 check = true;
		    	 // Do your stuff

		     }
		 }
		 }
		 
		 if (check) {
			 localnames = null;
			 localnames = dir.toArray(new String[dir.size()]);
			 ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,localnames);
			 
			 ad.notifyDataSetChanged();
			 lv.setAdapter(ad);
			 lv.setEnabled(true);
			 lv.setVisibility(View.VISIBLE);
			 testtext.setText(" Local Playlist");
			 testtext.setTextColor(Color.BLUE);
		     
		 }
		 else {
			 localnames = null;
			 lv.setVisibility(View.INVISIBLE);
			 testtext.setText(" No music in folder");
			 testtext.setTextColor(Color.RED);
			 
		 }
		 
         lv.setOnItemClickListener(new OnItemClickListener() {
                             public void onItemClick(AdapterView<?> arg0, View arg1,
                                     int arg2, long arg3) {
                                 // TODO Auto-generated method stub
                                // Toast.makeText(mContext,gv.getItemAtPosition(arg2).toString(),Toast.LENGTH_SHORT).show();
                            	 
                            	 selectedFile.setText(lv.getItemAtPosition(arg2).toString());
                                 //currentFile = MusicDir.getPath() + "/" + lv.getItemAtPosition(arg2).toString();
                            	 currentFile = LOCAL_MUSIC_DIR + "/" + lv.getItemAtPosition(arg2).toString();
                                 
                                 startPlay(currentFile);
                                 //temp.setData(fnames,gv.getItemAtPosition(arg2).toString());

                                 return;
                             }

                             });
         
		 
	 }
	 
	 private OnClickListener onButtonClick = new View.OnClickListener() {
         
         @Override
         public void onClick(View v) {
                 switch(v.getId())
                 {
                         case R.id.play:
                         {
                                 if(player.isPlaying())
                                 {
                                         handler.removeCallbacks(updatePositionRunnable);
                                         player.pause();
                                         playButton.setImageResource(android.R.drawable.ic_media_play);
                                 }
                                 else
                                 {
                                         if(isStarted)
                                         {
                                                 player.start();
                                                 playButton.setImageResource(android.R.drawable.ic_media_pause);
                                                  
                                                 updatePosition();
                                         }
                                         else
                                         {
                                                 startPlay(currentFile);
                                         }
                                 }
                                  
                                 break;
                         }
                         case R.id.next:
                         {
                                 int seekto = player.getCurrentPosition() + STEP_VALUE;
                                  
                                 if(seekto > player.getDuration())
                                         seekto = player.getDuration();
                                  
                                 player.pause();
                                 player.seekTo(seekto);
                                 player.start();
                                  
                                 break;
                         }
                         case R.id.prev:
                         {
                                 int seekto = player.getCurrentPosition() - STEP_VALUE;
                                  
                                 if(seekto < 0)
                                         seekto = 0;
                                  
                                 player.pause();
                                 player.seekTo(seekto);
                                 player.start();
                                  
                                 break;
                         }
                 }
         }
 };
 
 private void startPlay(String file) {
     Log.i("Selected: ", file);
      
     //selelctedFile.setText(file);
     //testtext.setText(file);
     seekbar.setProgress(0);
                      
     player.stop();
     player.reset();
      
     try {
             player.setDataSource(file);
             player.prepare();
             player.start();
     } catch (IllegalArgumentException e) {
             e.printStackTrace();
     } catch (IllegalStateException e) {
             e.printStackTrace();
     } catch (IOException e) {
             e.printStackTrace();
     }
      
     seekbar.setMax(player.getDuration());
     playButton.setImageResource(android.R.drawable.ic_media_pause);
      
     updatePosition();
      
     isStarted = true;
}

 
 
	 
	 
	 
	 
	 
	 
	 
	 
	 private void stopPlay() {
         player.stop();
         player.reset();
         playButton.setImageResource(android.R.drawable.ic_media_play);
         handler.removeCallbacks(updatePositionRunnable);
         seekbar.setProgress(0);
          
         isStarted = false;
 }
 	 
	 private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
         
         @Override
         public void onCompletion(MediaPlayer mp) {
                 stopPlay();
         }
     };
  
     private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
          
         @Override
         public boolean onError(MediaPlayer mp, int what, int extra) {
                 // returning false will call the OnCompletionListener
                 return false;
         }
     };
  
     private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {
                 isMoveingSeekBar = false;
         }
          
         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {
                 isMoveingSeekBar = true;
         }
          
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                 if(isMoveingSeekBar)
                 {
                         player.seekTo(progress);
                  
                         Log.i("OnSeekBarChangeListener","onProgressChanged");
                 }
         }
      };
 	  
	    private void updatePosition(){
	            handler.removeCallbacks(updatePositionRunnable);
	             
	            seekbar.setProgress(player.getCurrentPosition());
	             
	            handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	    }
	
	    

        public boolean accept(File file) {
            if (file.isDirectory()) {
              return false;
            } else {
              String path = file.getAbsolutePath().toLowerCase();
              
              for (int i = 0, n = extensions.length; i < n; i++) {
                String extension = extensions[i];
                if ((path.endsWith(extension) && (path.charAt(path.length() 
                          - extension.length() - 1)) == '.')) {
                  return true;
                }
              }
              
            }
            return false;
        }
        
	    
	    
	    
	 

}
