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
package com.domuslink.elements;

import org.json.JSONObject;

import android.util.Log;

public class Module extends Element {
	protected String moduleType;
	protected String moduleImage;

	private static final String TAG = "elements.Module";
	public static final String TOGGLE_D = "toggle";
	public static final String ON_OFF_D = "on_off";
	public static final String DIMMABLE_D = "dimmable";
	public static final String HVAC_D = "hvac";
	public static final String STATUS_D = "status";
	public static final String MULTI_D = "multi";
	public static final String RUN_D = "run";

	public Module() {
		super();
	}
	
	public Module(JSONObject jsonElement) throws Exception {
		super();
    	try {
    		setModuleType(jsonElement.getString("moduleType"));
    		setModuleImage(jsonElement.getString("moduleImage"));
    		setElementType(jsonElement.getString("elementType"));
    		setElementLine(jsonElement.getString("elementLine"));
    		setLineNum(jsonElement.getInt("lineNum"));
    		setArrayNum(jsonElement.getInt("arrayNum"));
    		setEnabled(jsonElement.getBoolean("enabled"));
    	}
    	catch(Exception e)
    	{
            Log.e(TAG, "Error getting alias from JSONObject", e);
            throw e;
    	}
		
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleImage() {
		return moduleImage;
	}
	public void setModuleImage(String moduleImage) {
		this.moduleImage = moduleImage;
	}

}
