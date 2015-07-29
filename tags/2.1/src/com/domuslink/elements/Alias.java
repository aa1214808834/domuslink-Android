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

public class Alias extends Element {
	protected String label;
	protected String houseCode;
	protected String devices;
	protected String moduleType;
	protected String moduleOptions;
	protected AliasMap aliasMapElement;
	protected int state;
	protected int dimLevel;
	protected Boolean isScene;
	protected Boolean isMultiAlias;

    private static final String TAG = "elements.Alias";

	
	public Alias() {
		super();
	}

	public Alias(JSONObject jsonElement) throws Exception {
		super();
		setState(0);
		setDimLevel(0);
    	try {
    		setLabel(jsonElement.getString("label"));
    		setAliasMapElement(new AliasMap(jsonElement.getJSONObject("aliasMapElement")));
    		if(!this.aliasMapElement.getElementType().contentEquals("Scene"))
    		{
	    		setHouseCode(jsonElement.getString("houseCode"));
	    		setDevices(jsonElement.getString("devices"));
	    		setModuleType(jsonElement.getString("moduleType"));
	    		setModuleOptions(jsonElement.getString("moduleOptions"));
	    		setElementType(jsonElement.getString("elementType"));
	    		setElementLine(jsonElement.getString("elementLine"));
	    		setLineNum(jsonElement.getInt("lineNum"));
	    		setArrayNum(jsonElement.getInt("arrayNum"));
	    		setEnabled(jsonElement.getBoolean("enabled"));
	    		this.isScene = false;
	    		if(devices.indexOf(",") > 0 || devices.indexOf("-") > 0)
	    			this.isMultiAlias = true;
	    		else
	    			this.isMultiAlias = false;
    		}
    		else
    		{
	    		this.isScene = true;
    			this.isMultiAlias = false;
    		}
    	}
    	catch(Exception e)
    	{
            Log.e(TAG, "Error getting alias from JSONObject", e);
            throw e;
    	}
		
	}

	public String getHouseDevice() {
		if(devices.compareTo("false") == 0)
			return (houseCode);
		else
			return (houseCode+devices);
	}
	
	public boolean isMultiAlias() {
		return this.isMultiAlias;
	}

	public String getLabel() {
		return label;
	}

	public  void setLabel(String label) {
		this.label = label;
	}

	public String getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleOptions() {
		return moduleOptions;
	}

	public void setModuleOptions(String moduleOptions) {
		this.moduleOptions = moduleOptions;
	}

	public AliasMap getAliasMapElement() {
		return aliasMapElement;
	}

	public void setAliasMapElement(AliasMap aliasMapElement) {
		this.aliasMapElement = aliasMapElement;
	}

	public int getState() {
		return state;
	}

	public String getStringState() {
		if(state == 1)
			return "on";
		else
			return "off";
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDimLevel() {
		return dimLevel;
	}

	public void setDimLevel(int dimLevel) {
		this.dimLevel = dimLevel;
	}
	
	public boolean isOn() {
		if(this.state == 1)
			return true;
		else
			return false;
	}
	
	public boolean isScene() {
			return isScene;
	}
}