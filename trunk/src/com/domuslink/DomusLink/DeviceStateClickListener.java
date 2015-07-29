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

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class DeviceStateClickListener implements OnClickListener, DomusAsyncUpdater {
	private DomusLink mParent;
    private Alias mAlias;

	public DeviceStateClickListener(DomusLink theParent, Alias theAlias) {
		super();
		mParent = theParent;
		mAlias = theAlias;
	}

	@Override
	public void onClick(View arg1) {
		int actionMethod;
		if(arg1 == mParent.getmDeviceSwitch())
		{
			if(mAlias.isOn())
				actionMethod = DomusHandler.TURN_OFF_ALIAS;
			else
				actionMethod = DomusHandler.TURN_ON_ALIAS;
		}
		else if(arg1 == mParent.getmDeviceOn())
		{
			actionMethod = DomusHandler.TURN_ON_ALIAS;
		}
		else if(arg1 == mParent.getmDeviceRun())
		{
			actionMethod = DomusHandler.RUN_SCENE;
		}
		else
		{
			actionMethod = DomusHandler.TURN_OFF_ALIAS;
		}

		DomusAsyncTask theTask = new DomusAsyncTask();
    	theTask.setUpdater(this);
    	Alias[] aliasArray = new Alias[1];
    	aliasArray[0] = mAlias;
    	DomusAsyncParams theParams = new DomusAsyncParams(mParent.getDomusHandler(), actionMethod, aliasArray, null, 0);
    	theTask.execute(theParams);		
	}

	@Override
	public void actionComplete(String[] result) {
		mParent.getmDeviceSwitch().setChecked(mAlias.isOn());	
		mParent.getmDeviceDimmer().setProgress(mAlias.getDimLevel());
		mParent.getmDeviceAdapter().refreshImageIds();
		mParent.getmDeviceAdapter().notifyDataSetChanged();
	}

	@Override
	public Context getContext() {
		return mParent;
	}

	@Override
	public void handleAsyncException(Exception e) {
		Log.e("DeviceStateClickListener", "Exception for "+mAlias.getLabel()+" in on/off button view");
		mParent.processUpdateDialog("Error with Domus: "+e.getMessage()+"\nUpdate Settings or Exit");
	}

	@Override
	public void setAliasesResult(Alias[] theAliases) {
		// not used
		
	}

	@Override
	public void setFloorPlanResult(String[] theLocations) {
		// not used
		
	}

	@Override
	public void setModuleTypesResult(Module[] theModules) {
		// not used
		
	}

	@Override
	public void heyuNotRunning() {
		mParent.heyuNotRunning();
	}
}
