package com.example.mediasync3;



import java.io.File;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.dropbox.client2.session.Session.AccessType;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MediaActivity extends FragmentActivity {

///////////////////////////////////////////////////////////////////////////
//                      Your app-specific settings.                      //
///////////////////////////////////////////////////////////////////////////

// Replace this with your app key and secret assigned by Dropbox.
// Note that this is a really insecure way to do this, and you shouldn't
// ship code which contains your key & secret in such an obvious way.
// Obfuscation is good.
final static private String APP_KEY = "a13czjpmaj4rmx2";
final static private String APP_SECRET = "2h61rwje3oz31bs";

// If you'd like to change the access type to the full Dropbox instead of
// an app folder, change this value.
final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

///////////////////////////////////////////////////////////////////////////
//                      End app-specific settings.                       //
///////////////////////////////////////////////////////////////////////////

// You don't need to change these, leave them alone.
final static private String ACCOUNT_PREFS_NAME = "prefs";
final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
private static final String TAG = "MediaSync";

private int position = 0;
private String[] musicnames, photonames = null, notesnames = null;
private String localMusicDir = null;
private String localPhotoDir = null;
private String md, pd;
private ViewPager pager;
private MediaFragmentPagerAdapter pagerAdapter;

DropboxAPI<AndroidAuthSession> mApi;
private boolean mLoggedIn;
private AndroidAuthSession session;

// Android widgets
private Button mSubmit;
private LinearLayout mDisplay;
private FileDialog fileDialog;

static SharedPreferences settings;
static SharedPreferences.Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
	
      pager = (ViewPager) findViewById(R.id.pager);
      FragmentManager fm = getSupportFragmentManager();
      pagerAdapter = new MediaFragmentPagerAdapter(fm);
      // Setting the pagerAdapter to the ViewPager
      pager.setAdapter(pagerAdapter);
      pager.setCurrentItem(1);
      
      mDisplay = (LinearLayout)findViewById(R.id.logged_in_display);
      //mDisplay.setVisibility(View.GONE);
      
      File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
      fileDialog = new FileDialog(this, mPath);
      
      // We create a new AuthSession so that we can use the Dropbox API.
      session = buildSession();
      mApi = new DropboxAPI<AndroidAuthSession>(session);
      
      checkAppKeySetup();
      
      mSubmit = (Button)findViewById(R.id.auth_button);

      mSubmit.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              // This logs you out if you're logged in, or vice versa
              if (mLoggedIn) {
                  logOut();
              } else {
                  // Start the remote authentication
                  mApi.getSession().startAuthentication(MediaActivity.this);
              }
          }
      });
      
      
   // Display the proper UI state if logged in or not
      setLoggedIn(mApi.getSession().isLinked());
      
      localMusicDir = Environment.getExternalStorageDirectory().toString() 
    		          + "/" + "media/dropmedia/Music";
      localPhotoDir = Environment.getExternalStorageDirectory().toString() 
	                  + "/" + "media/dropmedia/Photo";



      settings = this.getPreferences(MODE_WORLD_WRITEABLE);
      //editor = settings.edit();
      //store value
      //editor.putString("Preference_name_1", "1");
      //get value
      //eill return "0" if preference not exists, else return stored value
 
      pd = settings.getString("localPhotoDir", "0");
      md = settings.getString("localMusicDir", "0");
      
      if (md != "0") {
    	localMusicDir = md;
      }
      if (pd != "0") {
      	localPhotoDir = pd;
        }
 
    }	


    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }
    
    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
    	mLoggedIn = loggedIn;
    	if (loggedIn) {
    		mSubmit.setText("Unlink from Dropbox");
            mDisplay.setVisibility(View.GONE);
    	} else {
    		mSubmit.setText("Link with Dropbox");
            mDisplay.setVisibility(View.VISIBLE);
           
    	}
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }

	public AndroidAuthSession getSession() {
		return session;
	}
	
	
	public void setMusicNames(String[] mn) {
		musicnames = null;
		musicnames = mn;
	}
	
	public String[] getMusicNames() {
		return musicnames;
	}
	
	public void setPhotoNames(String[] pn) {
		photonames = null;
		photonames = pn;
	}
	
	public String[] getPhotoNames() {
		return photonames;
	}
		
	public void setNotesNames(String[] pn) {
		notesnames = null;
		notesnames = pn;
	}
	
	public String[] getNotesNames() {
		return notesnames;
	}
	
	
	public String getLocalMusicDir() {
		return localMusicDir;
	}
		
	public void setLocalMusicDir(String lmd) {
		localMusicDir = null;
		localMusicDir = lmd;
		editor = settings.edit();
	      //store value
	    editor.putString("localMusicDir", lmd);
	    editor.commit();
	    
	}
	
	public String getLocalPhotoDir() {
		return localPhotoDir;
	}
	
	public void setLocalPhotoDir(String lpd) {
		localPhotoDir = null;
		localPhotoDir = lpd;
		editor = settings.edit();
	      //store value
	    editor.putString("localPhotoDir", lpd);
	    editor.commit();
	      
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_media, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.photo_local:
                photoLocal();
                return true;
            case R.id.music_local:
            	musicLocal();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


	private void musicLocal() {
		// TODO Auto-generated method stub
		
		fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
	          public void directorySelected(File directory) {
	              Log.d(getClass().getName(), "selected dir " + directory.toString());
	              //localMusicDir = directory.toString();
	              setLocalMusicDir(directory.toString());
	          }
	        });
	        fileDialog.setSelectDirectoryOption(true);
	        fileDialog.showDialog();
		
	}

	private void photoLocal() {
		// TODO Auto-generated method stub
	
		fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
          public void directorySelected(File directory) {
              Log.d(getClass().getName(), "selected dir " + directory.toString());
              //localPhotoDir = directory.toString();
              setLocalPhotoDir(directory.toString());
          }
        });
        fileDialog.setSelectDirectoryOption(true);
        
        fileDialog.showDialog();
	}

	
	
}
