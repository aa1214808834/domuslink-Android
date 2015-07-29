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
package com.domuslink.api;

import com.domuslink.elements.Alias;

public class DomusAsyncParams {
	public DomusHandler mHandler;
	public int mHandlerMethod;
	public Alias[] mAliases;
	public String mLocation;
	public int mRequestLevel;

	public DomusAsyncParams(DomusHandler theHandler, int theMethod, Alias[] theAliases, String theLocation, int theRequestLevel) {
		super();
		this.mHandler = theHandler;
		this.mHandlerMethod = theMethod;
		this.mAliases = theAliases;
		this.mLocation = theLocation;
		this.mRequestLevel = theRequestLevel;
	}

	public DomusHandler getTheHandler() {
		return mHandler;
	}

	public void setTheHandler(DomusHandler theHandler) {
		this.mHandler = theHandler;
	}

	public int getHandlerMethod() {
		return mHandlerMethod;
	}

	public void setHandlerMethod(int theMethod) {
		this.mHandlerMethod = theMethod;
	}

	public Alias[] getTheAliases() {
		return mAliases;
	}

	public void setTheAliases(Alias[] theAliases) {
		this.mAliases = theAliases;
	}

	public String getTheLocation() {
		return mLocation;
	}

	public void setTheLocation(String theLocation) {
		this.mLocation = theLocation;
	}

	public int getTheRequestLevel() {
		return mRequestLevel;
	}

	public void setTheRequestLevel(int theRequestLevel) {
		this.mRequestLevel = theRequestLevel;
	}
	
}
