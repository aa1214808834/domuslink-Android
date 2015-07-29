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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class FloorPlanItemClickListener implements OnItemClickListener {
	private DomusLink mParent;
	
	public FloorPlanItemClickListener(DomusLink theParent) {
		super();
		mParent = theParent;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		((TextView) arg1).requestFocus();
//		Log.d("FloorplanItemClickListener", "Setting new floorplan to "+mParent.getmFloorPlan()[arg2]);
		mParent.getmDevicesTitleView().setText("Selected Location: "+mParent.getmFloorPlan()[arg2]);
		mParent.getmDeviceAdapter().setNewLocation(mParent.getmFloorPlan()[arg2]);
        mParent.getmDeviceTitleView().setText("");
		mParent.getmDeviceTitleView().setVisibility(View.GONE);
		mParent.getmDeviceDimmer().setVisibility(View.GONE);
		mParent.getmDimmerLevel().setVisibility(View.GONE);
		mParent.getmDeviceSwitch().setVisibility(View.GONE);
		mParent.getmDeviceOn().setVisibility(View.GONE);
		mParent.getmDeviceOff().setVisibility(View.GONE);

	}

}
