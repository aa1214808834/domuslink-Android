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

package com.domuslink.util;

import android.util.Log;

public class VersionHandler {
	private int expectedApiVersion;
	private int minApiVersion;
	private int domusApiVersion;
	private String domusVersionName;
	
	public VersionHandler(int theExpectedApi, int theMinApi, int theDomusApi, String theDomusVersion) {
		expectedApiVersion = theExpectedApi;
		domusApiVersion = theDomusApi;
		minApiVersion = theMinApi;
		domusVersionName = theDomusVersion;
//		Log.d("VersionHandler", "constructed with name:"+domusVersionName+",value:"+Integer.toString(domusApiVersion)+" expected:"+Integer.toString(expectedApiVersion)+" min:"+Integer.toString(minApiVersion));
	}
	
	public boolean validateVersion() {
		// If at all possible, we should be compatible with previous versions
		if(domusApiVersion >= minApiVersion && domusApiVersion <= expectedApiVersion)
			return true;
	
		return false;
	}
	
	public String compatibilityLevel() {
		if(domusApiVersion >= minApiVersion && domusApiVersion <= expectedApiVersion)
			return "true:Fully Compatible";
		else if(domusApiVersion >= expectedApiVersion)
			return "true:Some Features may not be available";		
		
		return "false:Not Compatible";
	}
	
	public String getDomusVersionName() {
		return domusVersionName;
	}
	
	public boolean getCompatibilty() {
		if(compatibilityLevel().contains("true"))
			return true;
		return false;
	}
	
	public String getCompatibilityString() {
		return (compatibilityLevel().substring(compatibilityLevel().lastIndexOf(":")+1));
	}

	public int getExpectedApiVersion() {
		return expectedApiVersion;
	}

	public int getMinApiVersion() {
		return minApiVersion;
	}

	public int getDomusApiVersion() {
		return domusApiVersion;
	}
	
	
}
