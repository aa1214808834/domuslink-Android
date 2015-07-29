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
import android.widget.SeekBar;

public class DimmerControl implements SeekBar.OnSeekBarChangeListener, DomusAsyncUpdater {
	    private DomusLink mParent;
	    private Alias mAlias;
	    
	    public DimmerControl(DomusLink theParent, Alias theAlias) {
			super();
			mAlias = theAlias;
			mParent = theParent;
		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
	        mParent.getmDimmerLevel().setText(progress + "%");
	    }

	    public void onStartTrackingTouch(SeekBar seekBar) {
	    }

	    public void onStopTrackingTouch(SeekBar seekBar) {
	    	int theProgress = seekBar.getProgress();
	    	DomusAsyncTask theTask = new DomusAsyncTask();
	    	theTask.setUpdater(this);
	    	Alias[] aliasArray = new Alias[1];
	    	aliasArray[0] = mAlias;
	    	DomusAsyncParams theParams = new DomusAsyncParams(mParent.getDomusHandler(), DomusHandler.DIM_ALIAS, aliasArray, null, theProgress);
	    	theTask.execute(theParams);		
	    }

		@Override
		public void actionComplete(String[] result) {
			mParent.getmDeviceSwitch().setChecked(mAlias.isOn());
			mParent.getmDeviceAdapter().refreshImageIds();
			mParent.getmDeviceAdapter().notifyDataSetChanged();
		}

		@Override
		public Context getContext() {
			return mParent;
		}

		@Override
		public void handleAsyncException(Exception e) {
			Log.e("DimmerControl", "Exception for "+mAlias.getLabel()+" in dimmer view");
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
