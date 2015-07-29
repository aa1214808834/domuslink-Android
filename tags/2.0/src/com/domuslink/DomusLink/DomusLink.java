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
import com.domuslink.util.VersionHandler;
import com.domuslink.DomusLink.DevicesAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DomusLink extends Activity implements DomusAsyncUpdater {
	
    private ListView mFloorPlanView;
    private Gallery mDevicesView;
    private TextView mDeviceTitleView;
    private TextView mDevicesTitleView;
    private DomusHandler domusHandler;
    private ArrayAdapter<String> mFloorPlanArrayAdapter;
    private DevicesAdapter mDeviceAdapter;
    private FloorPlanItemClickListener mFloorPlanClickListener;
    private DevicesItemClickListener mDevicesClickListener;
    private ToggleButton mDeviceSwitch;
    private Button mDeviceOn;
    private Button mDeviceOff;
    private SeekBar mDeviceDimmer;
    private TextView mDimmerLevel;
    private String[] mFloorPlan;
    private VersionHandler mVersion;
    private ModuleHome mModuleTypes;

    public String updateDialogText;
    public String infoDialogText;

    public final int UPDATE_SETTINGS_DIALOG = 1;
    public final int INFO_DIALOG = 2;
    public final int INFO_EXIT_DIALOG = 3;
    public final String TAG = "DomusLink";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
        mFloorPlanClickListener = new FloorPlanItemClickListener(this);
        mDevicesClickListener = new DevicesItemClickListener(this);
        mFloorPlanView = (ListView) findViewById(R.id.floorplan);
	    mDevicesTitleView = (TextView) findViewById(R.id.devices_title);
	    mDevicesView = (Gallery) findViewById(R.id.devices);
	    mDeviceTitleView = (TextView) findViewById(R.id.device);
	    mDeviceSwitch = (ToggleButton) findViewById(R.id.device_toggle);
	    mDeviceOn = (Button) findViewById(R.id.device_on);
	    mDeviceOff = (Button) findViewById(R.id.device_off);
	    mDeviceDimmer = (SeekBar) findViewById(R.id.dimmer);
	    mDimmerLevel = (TextView) findViewById(R.id.dimmer_level);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mDeviceTitleView.setText("");
        mDeviceTitleView.setVisibility(View.GONE);
		mDeviceDimmer.setVisibility(View.GONE);
		mDimmerLevel.setVisibility(View.GONE);
		mDeviceSwitch.setVisibility(View.GONE);
		mDeviceOn.setVisibility(View.GONE);
		mDeviceOff.setVisibility(View.GONE);
		validateVersion();
    }
    
    public void initializeFloorPlan() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
//        Log.d(TAG, "This host is ["+prefs.getString("hostPref", "")+"] and password ["+prefs.getString("passPref", "")+"]" );
        if (prefs.getString("hostPref", "").length() == 0 || prefs.getString("passPref", "").length() == 0) {
//        	Log.d(TAG, "Showing settings dialog");
        	processUpdateDialog("Prefs not found - update your Domus Settings.");
            return;
        }
        
        domusHandler = new DomusHandler(this, prefs.getString("hostPref", ""), prefs.getString("passPref", ""), prefs.getBoolean("visiblePref", true));
       	DomusAsyncTask theTask = new DomusAsyncTask();
       	theTask.setUpdater(this);
       	DomusAsyncParams theParams = new DomusAsyncParams(domusHandler, DomusHandler.GET_INITIAL, null, null, 0);
       	theTask.execute(theParams);
    }

    public void validateVersion() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
//      Log.d(TAG, "This host is ["+prefs.getString("hostPref", "")+"] and password ["+prefs.getString("passPref", "")+"]" );
      if (prefs.getString("hostPref", "").length() == 0 || prefs.getString("passPref", "").length() == 0) {
//      	Log.d(TAG, "Showing settings dialog");
    	  processUpdateDialog("Prefs not found - update your Domus Settings.");
          return;
      }
      
      domusHandler = new DomusHandler(this, prefs.getString("hostPref", ""), prefs.getString("passPref", ""), prefs.getBoolean("visiblePref", true));
     	DomusAsyncTask theTask = new DomusAsyncTask();
     	theTask.setUpdater(this);
     	DomusAsyncParams theParams = new DomusAsyncParams(domusHandler, DomusHandler.GET_VERSION, null, null, 0);
     	theTask.execute(theParams);
   	
    }
    
	protected String getUpdateDialogText() {
		return updateDialogText;
	}

	protected void setUpdateDialogText(String updateDialogText) {
		this.updateDialogText = updateDialogText;
	}

	public String getInfoDialogText() {
		return infoDialogText;
	}

	public void setInfoDialogText(String infoDialogText) {
		this.infoDialogText = infoDialogText;
	}

	protected String[] getmFloorPlan() {
		return mFloorPlan;
	}

	protected void setmFloorPlan(String[] mFloorPlan) {
		this.mFloorPlan = mFloorPlan;
	}

	protected Gallery getmDevicesView() {
		return mDevicesView;
	}

	protected void setmDevicesView(Gallery mDevicesView) {
		this.mDevicesView = mDevicesView;
	}

	protected DevicesAdapter getmDeviceAdapter() {
		return mDeviceAdapter;
	}

	protected void setmDeviceAdapter(DevicesAdapter mDeviceAdapter) {
		this.mDeviceAdapter = mDeviceAdapter;
	}

	protected TextView getmDeviceTitleView() {
		return mDeviceTitleView;
	}

	protected void setmDeviceTitleView(TextView mDeviceTitleView) {
		this.mDeviceTitleView = mDeviceTitleView;
	}

	protected TextView getmDevicesTitleView() {
		return mDevicesTitleView;
	}

	protected void setmDevicesTitleView(TextView mDevicesTitleView) {
		this.mDevicesTitleView = mDevicesTitleView;
	}

	protected ToggleButton getmDeviceSwitch() {
		return mDeviceSwitch;
	}

	protected void setmDeviceSwitch(ToggleButton mDeviceSwitch) {
		this.mDeviceSwitch = mDeviceSwitch;
	}

	public Button getmDeviceOn() {
		return mDeviceOn;
	}

	public void setmDeviceOn(Button mDeviceOn) {
		this.mDeviceOn = mDeviceOn;
	}

	public Button getmDeviceOff() {
		return mDeviceOff;
	}

	public void setmDeviceOff(Button mDeviceOff) {
		this.mDeviceOff = mDeviceOff;
	}

	protected SeekBar getmDeviceDimmer() {
		return mDeviceDimmer;
	}

	protected void setmDeviceDimmer(SeekBar mDeviceDimmer) {
		this.mDeviceDimmer = mDeviceDimmer;
	}

	protected TextView getmDimmerLevel() {
		return mDimmerLevel;
	}

	protected void setmDimmerLevel(TextView mDimmerLevel) {
		this.mDimmerLevel = mDimmerLevel;
	}

	protected DomusHandler getDomusHandler() {
		return domusHandler;
	}

	protected void setDomusHandler(DomusHandler domusHandler) {
		this.domusHandler = domusHandler;
	}

	public ModuleHome getmModuleTypes() {
		return mModuleTypes;
	}

	public void setmModuleTypes(ModuleHome mModuleTypes) {
		this.mModuleTypes = mModuleTypes;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.systems_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prefs:
                updatePreferences();
                break;
            case R.id.about:
            	String versionInfo;
            	try {
            		PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            		versionInfo = pInfo.versionName;
            	}
            	catch(Exception e)
            	{
            		versionInfo = "Unknown";
            	}
            	processInfoDialog(this.getResources().getString(R.string.app_name)+" v"+versionInfo+"\nvalid RESTAPI levels are ("+Integer.toString(mVersion.getMinApiVersion())+":"+Integer.toString(mVersion.getExpectedApiVersion())+")");
                break;
            case R.id.exit:
            	this.finish();
                break;
        }
        return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    	AlertDialog alert = null;
        switch (id) {
            case UPDATE_SETTINGS_DIALOG:
//            	Log.d(TAG, "entering onCreateDialog for settings with text: "+updateDialogText);
            	alertBuilder.setMessage(updateDialogText)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                            	Log.d(TAG, "onClick in prefs alert - positive");
                                updatePreferences();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(
                               R.string.exit, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                    	Log.d(TAG, "onClick in prefs alert - negative");
                                        dialog.dismiss();
                                    	DomusLink.this.finish();
                                    }
                        });
            	break;
            case INFO_DIALOG:
//            	Log.d(TAG, "entering onCreateDialog for settings with text: "+updateDialogText);
            	alertBuilder.setMessage(infoDialogText)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                            	Log.d(TAG, "onClick in info msg - positive");
                                dialog.dismiss();
                            }
                        });
                break;
            case INFO_EXIT_DIALOG:
//            	Log.d(TAG, "entering onCreateDialog for settings with text: "+updateDialogText);
            	alertBuilder.setMessage(infoDialogText)
                        .setNeutralButton(
                               R.string.exit, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                    	Log.d(TAG, "onClick in info msg - negative");
                                        dialog.dismiss();
                                    	DomusLink.this.finish();
                                    }
                        });
                break;
        }
        
        if(alertBuilder != null)
        	alert = alertBuilder.create();
        return alert;
    }
    
    public void updatePreferences() {
  //  	Log.d(TAG, "Starting updatePreferences");
        Intent settingsActivity = new Intent(getBaseContext(), DomusSettings.class);
        startActivity(settingsActivity);
    }

    public void processUpdateDialog(String dialogText) {
    	setUpdateDialogText(dialogText);
    	removeDialog(UPDATE_SETTINGS_DIALOG);
        showDialog(UPDATE_SETTINGS_DIALOG);
    }

    public void processInfoDialog(String dialogText) {
    	setInfoDialogText(dialogText);
    	removeDialog(INFO_DIALOG);
        showDialog(INFO_DIALOG);
    }

    public void processInfoExitDialog(String dialogText) {
    	setInfoDialogText(dialogText);
    	removeDialog(INFO_EXIT_DIALOG);
        showDialog(INFO_EXIT_DIALOG);
    }

    @Override
	public Context getContext() {
		return this;
	}

	@Override
	public void setAliasesResult(Alias[] theAliases) {
	    mDeviceAdapter = new DevicesAdapter(this, domusHandler, theAliases, mModuleTypes);
	    mDevicesView.setAdapter(mDeviceAdapter);
	    mDevicesView.setOnItemClickListener(mDevicesClickListener);
	}

	@Override
	public void setFloorPlanResult(String[] theLocations) {
    	mFloorPlan = theLocations;
        mFloorPlanArrayAdapter = new ArrayAdapter<String>(this, R.layout.floorplan, mFloorPlan);
        mFloorPlanView.setAdapter(mFloorPlanArrayAdapter);
        mFloorPlanView.setOnItemClickListener(mFloorPlanClickListener);
        mFloorPlanView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	    mDevicesTitleView.setText("Selected Location: "+mFloorPlan[0]);		
	}

	@Override
	public void handleAsyncException(Exception e) {
    	Log.e(TAG, "InitializeFloorPlan - Exception - showing settings dialog", e);
    	processUpdateDialog("Error communicating with Domus: "+e.getMessage()+"\nUpdate Settings or Exit");
	}
    
	@Override
	public void actionComplete(String[] result) {
		mVersion = new VersionHandler(this.getResources().getInteger(R.integer.expected_api_version), this.getResources().getInteger(R.integer.min_api_version), Integer.parseInt(result[1]), result[0]);
		if(!mVersion.validateVersion()) {
			if(!mVersion.getCompatibilty()) {
				processInfoExitDialog("Invalid server API Level ["+mVersion.getDomusVersionName()+"] valid API levels are ("+Integer.toString(mVersion.getMinApiVersion())+":"+Integer.toString(mVersion.getExpectedApiVersion())+")");
				return;
			}
			else {
				processInfoDialog("Versions are different but compatible, "+mVersion.getCompatibilityString());
			}
		}
		initializeFloorPlan();
	}

	@Override
	public void setModuleTypesResult(Module[] theModules) {
		mModuleTypes = new ModuleHome(theModules);
		
	}
}