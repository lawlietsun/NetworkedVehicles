package com.example.mediasync3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class MediaFragmentPagerAdapter extends FragmentPagerAdapter{
 
    final int PAGE_COUNT = 3;
    MediaMusic fragment1;
	MediaPhoto fragment2;
	MediaNote fragment3;
   
	// constructor, used in activity
    public MediaFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new MediaMusic();
    	fragment2 = new MediaPhoto();
    	fragment3 = new MediaNote();
    	
    }
 
    // when a page is requested to create, do following
    @Override
    public Fragment getItem(int arg0) {
        
    	if (arg0 == 1) {
        	return fragment1;
    	}
    	
    	else if (arg0 == 0) {
    		return fragment2;
    	}
    	
    	else {
    		return fragment3;
    	}
    	
    }
 
    // returns the number of pages
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
   
}