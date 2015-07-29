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

public class AliasMap extends Element {
	protected String aliasLabel;
	protected String floorPlanLabel;
	protected String hiddenFromHome;

	private static final String TAG = "elements.AliasMap";

	public AliasMap() {
		super();
	}

	public AliasMap(JSONObject jsonElement) throws Exception {
		super();
    	try {
    		setAliasLabel(jsonElement.getString("aliasLabel"));
    		setFloorPlanLabel(jsonElement.getString("floorPlanLabel"));
    		setHiddenFromHome(jsonElement.getString("hiddenFromHome"));
    		setElementType(jsonElement.getString("elementType"));
    		setElementLine(jsonElement.getString("elementLine"));
    		setLineNum(jsonElement.getInt("lineNum"));
    		setArrayNum(jsonElement.getInt("arrayNum"));
    		setEnabled(jsonElement.getBoolean("enabled"));
    	}
    	catch(Exception e)
    	{
            Log.e(TAG, "Error getting AliasMap from JSONObject", e);
            throw e;
    	}
	}

	public String getAliasLabel() {
		return aliasLabel;
	}

	public void setAliasLabel(String aliasLabel) {
		this.aliasLabel = aliasLabel;
	}

	public String getFloorPlanLabel() {
		return floorPlanLabel;
	}

	public void setFloorPlanLabel(String floorPlanLabel) {
		this.floorPlanLabel = floorPlanLabel;
	}

	public String getHiddenFromHome() {
		return hiddenFromHome;
	}

	public void setHiddenFromHome(String hiddenFromHome) {
		this.hiddenFromHome = hiddenFromHome;
	}
	
}
