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
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DevicesItemClickListener implements OnItemClickListener, DomusAsyncUpdater {
	private DomusLink mParent;
	private Alias theSelectedDevice;

	public DevicesItemClickListener(DomusLink theParent) {
		super();
		mParent = theParent;
		theSelectedDevice = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		processDevicesControlSetup(arg2);

	}

	public void processDevicesControlSetup(int selectPosition) {
		theSelectedDevice = mParent.getmDeviceAdapter().getItem(selectPosition);
		ModuleHome moduleTypes = mParent.getmModuleTypes();
		Module aModule = moduleTypes.find(theSelectedDevice.getAliasMapElement().getElementType());
		if(aModule.getModuleType().compareTo(Module.STATUS_D) == 0) {
			mParent.getmDeviceDimmer().setVisibility(View.GONE);
			mParent.getmDimmerLevel().setVisibility(View.GONE);
			mParent.getmDeviceSwitch().setVisibility(View.GONE);
			mParent.getmDeviceOn().setVisibility(View.GONE);
			mParent.getmDeviceOff().setVisibility(View.GONE);
			mParent.getmDeviceOff().setOnClickListener(null);
			mParent.getmDeviceOn().setOnClickListener(null);
			mParent.getmDeviceTitleView().setVisibility(View.INVISIBLE);
			String statusText = "Device is "+(theSelectedDevice.getState() == 0 ? "Off" : "On");
			Toast deviceStatus = Toast.makeText(mParent, statusText, Toast.LENGTH_SHORT);
			deviceStatus.show();
		}
		else {
			DomusAsyncTask theTask = new DomusAsyncTask();
			theTask.setUpdater(this);
			Alias[] aliasArray = new Alias[1];
			aliasArray[0] = theSelectedDevice;
			DomusAsyncParams theParams = new DomusAsyncParams(mParent.getDomusHandler(), DomusHandler.GET_ALIAS_STATE, aliasArray, null, 0);
			theTask.execute(theParams);
		}
	}

	@Override
	public void actionComplete(String[] result) {
		mParent.getmDeviceTitleView().setText("Device: "+theSelectedDevice.getLabel());
		mParent.getmDeviceSwitch().setChecked(theSelectedDevice.isOn());
		DeviceStateClickListener aDeviceStateClickListener = new DeviceStateClickListener(mParent, theSelectedDevice);
		ModuleHome moduleTypes = mParent.getmModuleTypes();
		Module aModule = moduleTypes.find(theSelectedDevice.getAliasMapElement().getElementType());
		if(aModule.getModuleType().compareTo(Module.DIMMABLE_D) == 0)
		{
			mParent.getmDeviceDimmer().setVisibility(View.VISIBLE);
			mParent.getmDimmerLevel().setVisibility(View.VISIBLE);
			mParent.getmDeviceSwitch().setVisibility(View.VISIBLE);
			mParent.getmDeviceOn().setVisibility(View.INVISIBLE);
			mParent.getmDeviceOff().setVisibility(View.INVISIBLE);
			mParent.getmDeviceSwitch().setOnClickListener(aDeviceStateClickListener);
			mParent.getmDeviceDimmer().setOnSeekBarChangeListener(new DimmerControl(mParent, theSelectedDevice));
			mParent.getmDeviceDimmer().setProgress(theSelectedDevice.getDimLevel());
			mParent.getmDimmerLevel().setText(theSelectedDevice.getDimLevel()+"%");
			mParent.getmDeviceTitleView().setVisibility(View.VISIBLE);
		}
		else if(aModule.getModuleType().compareTo(Module.TOGGLE_D) == 0)
		{
			mParent.getmDeviceDimmer().setVisibility(View.GONE);
			mParent.getmDimmerLevel().setVisibility(View.GONE);
			mParent.getmDeviceSwitch().setVisibility(View.VISIBLE);
			mParent.getmDeviceOn().setVisibility(View.INVISIBLE);
			mParent.getmDeviceOff().setVisibility(View.INVISIBLE);
			mParent.getmDeviceSwitch().setOnClickListener(aDeviceStateClickListener);
			mParent.getmDeviceTitleView().setVisibility(View.VISIBLE);
		}
		else if(aModule.getModuleType().compareTo(Module.STATUS_D) == 0)
		{
			mParent.getmDeviceDimmer().setVisibility(View.GONE);
			mParent.getmDimmerLevel().setVisibility(View.GONE);
			mParent.getmDeviceSwitch().setVisibility(View.GONE);
			mParent.getmDeviceOn().setVisibility(View.GONE);
			mParent.getmDeviceOff().setVisibility(View.GONE);
			mParent.getmDeviceOff().setOnClickListener(null);
			mParent.getmDeviceOn().setOnClickListener(null);
			mParent.getmDeviceTitleView().setVisibility(View.INVISIBLE);
		}
		else
		{
			mParent.getmDeviceDimmer().setVisibility(View.GONE);
			mParent.getmDimmerLevel().setVisibility(View.GONE);
			mParent.getmDeviceSwitch().setVisibility(View.INVISIBLE);
			mParent.getmDeviceOn().setVisibility(View.VISIBLE);
			mParent.getmDeviceOff().setVisibility(View.VISIBLE);
			mParent.getmDeviceOff().setOnClickListener(aDeviceStateClickListener);
			mParent.getmDeviceOn().setOnClickListener(aDeviceStateClickListener);
			mParent.getmDeviceTitleView().setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Context getContext() {
		return mParent;
	}

	@Override
	public void handleAsyncException(Exception e) {
		Log.e("DevicesItemClickListener", "Exception for device view: "+e.getMessage());
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
	
}
