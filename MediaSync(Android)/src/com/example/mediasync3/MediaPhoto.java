package com.example.mediasync3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;





public class MediaPhoto extends Fragment{

	private final String PHOTO_DIR = "/Photo/";
	private String LOCAL_PHOTO_DIR = null;
	private Integer clickCounter;
	String[] fnames, localnames;
	Integer imagePos = 0, maxNumber = -1;
	String mCameraFileName;
	String[] extensions = new String[3];
	private ArrayAdapter <CharSequence> adapterList;


	String SRC_IMAGE = "/sdcard/Gallary/2.png";
    String CONVERTED_IMAGE = "/sdcard/Gallary/Converted/1.png";
    boolean isConvertedImage;
	
	Spinner spinList;
	View v1;
	//ImageView iv;
	ImageButton prevButton, nextButton;
	MediaActivity mActivity;
	FileDialog fileDialog;
	File selectedUpFile;
	final static private int NEW_PICTURE = 1;

	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	    		Bundle savedInstanceState) {
	    
		   v1 = inflater.inflate(R.layout.page2, container,false);
		   v1 = inflater.inflate(R.layout.page2, container,false);
	       Button mUpPhoto = (Button)v1.findViewById(R.id.upphoto_button);
	       Button mGetPhoto = (Button)v1.findViewById(R.id.getphoto_button);
	       Button mTakePhoto = (Button)v1.findViewById(R.id.cameraphoto_button);
	       prevButton = (ImageButton)v1.findViewById(R.id.prev);
	       nextButton = (ImageButton)v1.findViewById(R.id.next);
	        
	       spinList = (Spinner)v1.findViewById(R.id.spinner1);
	       
	       mActivity = (MediaActivity)getActivity();
	       LOCAL_PHOTO_DIR = mActivity.getLocalPhotoDir();
	       
	       File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
           fileDialog = new FileDialog(getActivity(), mPath);
          
           
		      mUpPhoto.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // upload file
		        	  UploadFile();
		          }
		      });
		      
		      mGetPhoto.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // get file
		  		
		  			DownloadFile(-2);
		  			
		          }
		      });
		      
		      
		      mTakePhoto.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	
		        	  takePhoto();
		  			
		          }
		      });
		      
		      prevButton.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // get file
		        	  
		        	  if (maxNumber != -1) {
		        	  if (imagePos == 0) {
		        		  showImage(maxNumber - 1); //commented out
		        	  }
		        	  else {
		        	  showImage(--imagePos % maxNumber); //commented out
		        	  }
		        	  }
        	  
		          }
		      });
		      
		      nextButton.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  // get file
		          if (maxNumber != -1) {  
		  		showImage(++imagePos % maxNumber); //commented out
		        	  }
		          }
		      });
		      
		      if (savedInstanceState != null) {
		            mCameraFileName = savedInstanceState.getString("mCameraFileName");
		      }
 		      
		      clickCounter = 0;
		      spinList.setEnabled(false);
		      spinList.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	    public void onItemSelected(AdapterView<?> parent, View view, int pos,
		    	            long id) {

		    	        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
		    	        //((TextView) parent.getChildAt(0)).set
		    	        clickCounter++;
		    	        
		    	        if ((spinList.isEnabled()) && (clickCounter > 1)) {
		    	        DownloadFile(pos);
		    	        imagePos = 0;
		    	        }
		   		    	    }
		    	    public void onNothingSelected(AdapterView<?> parent) {
		    	    }
		    	});
		
		      adapterList =
		  			  new ArrayAdapter <CharSequence> (getActivity(), android.R.layout.simple_spinner_item );
		  			adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		   
		      extensions[0] = "jpg";
		      extensions[1] = "png";
		      extensions[2] = "bmp";
		    		  
		      
		 /*commented here */    showImage(0);
		 	   
		   return v1;
		}

	 private void UploadFile() {
		 
		 fileDialog.setFileEndsWith(".jpg");
         fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
             public void fileSelected(File file) {
                 Log.d(getClass().getName(), "selected file " + file.toString());
                 selectedUpFile = file;
                 
                 Upload upload = new Upload(getActivity(), mActivity.mApi, PHOTO_DIR, selectedUpFile);
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
	 
	
	 /*
     @Override
 	public void setUserVisibleHint(boolean isVisibleToUser) {
 	super.setUserVisibleHint(isVisibleToUser);

 	if (isVisibleToUser == true) {
 	
 		//clickCounter++;
 		//TextView tv = (TextView)v1.findViewById(R.id.textView2);
	    //tv.setText(Integer.toString(clickCounter));
	    
 		if (mActivity.getPhotoNames() != null) {
		 		fnames = mActivity.getPhotoNames();
		 		
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
	 
	 
	 
	 
	 
	 
	 
	 
	 private void DownloadFile(int pos) {
		  DownloadPhoto download = new DownloadPhoto(pos, v1, this, mActivity.mApi, PHOTO_DIR);
          download.execute();
        
		}
	 
	 
	 public void showImage(int pos) {
		 
		 if (!LOCAL_PHOTO_DIR.equals(mActivity.getLocalPhotoDir()))
		 {
			 pos = 0;
			 imagePos = 0;
			 LOCAL_PHOTO_DIR = mActivity.getLocalPhotoDir();
		     
			imagePos = pos; // was commented before
		 }
		 else {
			 	 
		 }
		  
		 ImageView iv = (ImageView) v1.findViewById(R.id.image_view);
		 //TextView tv = (TextView) v1.findViewById(R.id.textView2);
		 //tv.setText(Integer.toString(imagePos++));
         
		 int counter = 0;
		 boolean check = false;
		 ArrayList<String> dir=new ArrayList<String>();
	     //File sdCardRoot = Environment.getExternalStorageDirectory();
		 File PhotoDir = new File(LOCAL_PHOTO_DIR);
		 dir.clear();
		 for (File f : PhotoDir.listFiles()) {
		     if (f.isFile() && accept(f))
		     {
		    	 check = true;
		         counter++;
		    	 dir.add(f.getName());
		    	 // Do your stuff
		     }
		 }
		 
		 if (check) {
			 
		 maxNumber = counter;
		//null pointer exep
		  localnames = null;
	//	  localnames = dir.toArray(new String[dir.size()])
	
		//null pointer
		 localnames = dir.toArray(new String[counter]);
		 
		 //tv.setText(localnames[pos]);
		 imagePos = pos;
		 
		 Bitmap d = new BitmapDrawable(getActivity().getResources() , LOCAL_PHOTO_DIR + "/" + localnames[pos]).getBitmap();
		 Bitmap d2 = new BitmapDrawable(getActivity().getResources() , LOCAL_PHOTO_DIR + "/Converted/" + localnames[pos]).getBitmap();
			
		 SRC_IMAGE = LOCAL_PHOTO_DIR + "/" + localnames[pos];
		 File bitFile = new File(LOCAL_PHOTO_DIR + "/Converted");
		 if (!bitFile.exists()) {	
	        	if (bitFile.mkdir()) {}
	        		        	}
		 
		 CONVERTED_IMAGE = LOCAL_PHOTO_DIR + "/Converted/" + localnames[pos];
		 //setImage(SRC_IMAGE);
		 
		 try {
		 if ((d.getHeight() > 1024) || (d.getWidth() > 1024)) {
		
			 File cf = new File(LOCAL_PHOTO_DIR + "/Converted/" + localnames[pos]);
			 if (!cf.exists()) {
				 ImageUtils.getCompressedImagePath(SRC_IMAGE, CONVERTED_IMAGE);
				 setImage(CONVERTED_IMAGE);
				 
			 }
			 else {
				 iv.setImageBitmap(d2);
				 
			 }
		     			 			 
		 }
		 else {
			 iv.setImageBitmap(d);
			 
		 }
		 }
		 catch (Exception e) {
			 
		 }
         
		 }
	 }

    public void takePhoto() {
	
    	 Intent intent = new Intent();
         // Picture from camera
         intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

         // This is not the right way to do this, but for some reason, having
         // it store it in
         // MediaStore.Images.Media.EXTERNAL_CONTENT_URI isn't working right.

         Date date = new Date();
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss");

         String newPicFile = df.format(date) + ".jpg";
         String outPath = LOCAL_PHOTO_DIR + "/" + newPicFile;
         File outFile = new File(outPath);

         mCameraFileName = outFile.toString();
         Uri outuri = Uri.fromFile(outFile);
         
         //Upload upload = new Upload(getActivity(), mActivity.mApi, PHOTO_DIR, outFile);
         //upload.execute();
         
         
         
         intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
         //Log.i(TAG, "Importing New Picture: " + mCameraFileName);
         try {
             super.startActivityForResult(intent, NEW_PICTURE);
         } catch (ActivityNotFoundException e) {
             showToast("There doesn't seem to be a camera.");
         }
    }
         
         @Override
         public void onActivityResult(int requestCode, int resultCode, Intent data) {
             
        	 if (requestCode == NEW_PICTURE) {
        	 
        	 if (resultCode == Activity.RESULT_OK) {
                 
        	 Uri uri = null;
             if (data != null) {
                 uri = data.getData();
             }
             if (uri == null && mCameraFileName != null) {
                 uri = Uri.fromFile(new File(mCameraFileName));
             }
             File file = new File(mCameraFileName);

             if (uri != null) {
                 //UploadMedia upload = new UploadMedia(this, mApi, PHOTO_DIR, file);
                 //upload.execute();
               Upload upload = new Upload(getActivity(), mActivity.mApi, PHOTO_DIR, file);
               upload.execute();
             }
              }
        	 }
         
         
         
         }
        private void showToast(String msg) {
        Toast error = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        error.show();
    }
   
        public void setImage(String imagePath) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
                        .getAbsolutePath());

                ImageView myImage = (ImageView) v1.findViewById(R.id.image_view);
                myImage.setImageBitmap(myBitmap);

            }
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
