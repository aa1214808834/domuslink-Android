/*
 * domus.Link :: PHP Web-based frontend for Heyu (X10 Home Automation)
 * Copyright (c) 2007, Istvan Hubay Cebrian (istvan.cebrian@domus.link.co.pt)
 * Project's homepage: http://domus.link.co.pt
 * Project's dev. homepage: http://domuslink.googlecode.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope's that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. You should have 
 * received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.domuslink.DomusLink;

import com.domuslink.api.DomusAsyncParams;
import com.domuslink.api.DomusAsyncTask;
import com.domuslink.api.DomusAsyncUpdater;
import com.domuslink.api.DomusHandler;
import com.domuslink.elements.Alias;
import com.domuslink.elements.Module;
import com.domuslink.elements.ModuleHome;
import com.domuslink.util.LabelHandler;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DevicesAdapter extends BaseAdapter implements DomusAsyncUpdater {
    int mGalleryItemBackground;
    private DomusLink mContext;
    private DomusHandler myHandler;
    private Alias[] mAliases;
    private ModuleHome mModuleTypes;

    private String[] mLabelIds;
    
    private Integer[] mImageIds;

    public DevicesAdapter(DomusLink c, DomusHandler theHandler, Alias[] theAliases, ModuleHome theModuleTypes) {
        mContext = c;
        mModuleTypes = theModuleTypes;
        setData(theHandler, theAliases);
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.DomusDevices);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.DomusDevices_android_galleryItemBackground, 0);
        a.recycle();
    }
    
    public void setData(DomusHandler theHandler, Alias[] theAliases) {
        myHandler = theHandler;
        setAliasesResult(theAliases);
    }
    
    public void setNewLocation(String theLocation) {
    	DomusAsyncTask theTask = new DomusAsyncTask();
    	theTask.setUpdater(this);
    	DomusAsyncParams theParams = new DomusAsyncParams(myHandler, DomusHandler.GET_ALIASES_BY_LOCATION, null, theLocation, 0);
    	theTask.execute(theParams);
   }
    
    public void refreshImageIds()
    {
        mImageIds = new Integer[mAliases.length];
        mLabelIds = new String[mAliases.length];

        for(int i = 0; i < mAliases.length; i++) {
        	mLabelIds[i] = LabelHandler.labelParse(mAliases[i].getLabel(), false);
        	String imagePartial = mModuleTypes.find(mAliases[i].getAliasMapElement().getElementType()).getModuleImage();
	       	if(mAliases[i].isScene()) {
        		mImageIds[i] = R.drawable.menu_scene_off;
    		}
	       	else if(mAliases[i].isMultiAlias()) {
        		mImageIds[i] = R.drawable.module_multi;
    		}
	       	else
	    	{
	    		if(mAliases[i].getState() == 0)
	    			mImageIds[i] = mContext.getResources().getIdentifier("menu_"+imagePartial+"_off", "drawable", "com.domuslink.DomusLink");
	    		else
	    			mImageIds[i] = mContext.getResources().getIdentifier("menu_"+imagePartial+"_on", "drawable", "com.domuslink.DomusLink");
	    	}
        }
    }
    public int getCount() {
        return mAliases.length;
    }

    public Alias getItem(int position) {
        return mAliases[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	LinearLayout l = new LinearLayout(mContext);
    	ImageView i = new ImageView(mContext);
        TextView t = new TextView(mContext);

        i.setImageResource(mImageIds[position]);
//        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
//        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setBackgroundResource(mGalleryItemBackground);
        
        t.setText(mLabelIds[position]);
        t.setGravity(Gravity.CENTER);
        t.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(5, 5, 5, 5);
        l.addView(i);
        l.addView(t);
        return l;
    }

	@Override
	public Context getContext() {
		return mContext;
	}

	@Override
	public void handleAsyncException(Exception e) {
		mContext.processUpdateDialog("Failed getting aliases for floorplan");
		
	}

	@Override
	public void setAliasesResult(Alias[] theAliases) {
		mAliases = theAliases;
        refreshImageIds();
        notifyDataSetChanged();
	}

	@Override
	public void setFloorPlanResult(String[] theLocations) {
		// not used
		
	}

	@Override
	public void actionComplete(String[] result) {
		// not used
		
	}

	@Override
	public void setModuleTypesResult(Module[] theModules) {
		// not used
		
	}

	@Override
	public void heyuNotRunning() {
		mContext.heyuNotRunning();
	}
}
