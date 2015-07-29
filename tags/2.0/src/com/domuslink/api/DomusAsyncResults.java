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
import com.domuslink.elements.Module;

public class DomusAsyncResults {
	public String[] locations;
	public Alias[] aliases;
	public Module[] moduleTypes;
	public String[] versionInfo;
	public Exception theException;
	public DomusAsyncParams calledParams;
	
	public DomusAsyncResults() {
		super();
		this.locations = null;
		this.aliases = null;
		this.moduleTypes = null;
		this.theException = null;
		this.calledParams = null;
	}

	public String[] getLocations() {
		return locations;
	}
	public void setLocations(String[] locations) {
		this.locations = locations;
	}
	public Alias[] getAliases() {
		return aliases;
	}
	public void setAliases(Alias[] theAliases) {
		aliases = theAliases;
	}

	public Module[] getModuleTypes() {
		return moduleTypes;
	}

	public void setModuleTypes(Module[] moduleTypes) {
		this.moduleTypes = moduleTypes;
	}

	public Exception getTheException() {
		return theException;
	}
	public void setTheException(Exception theException) {
		this.theException = theException;
	}

	public DomusAsyncParams getCalledParams() {
		return calledParams;
	}

	public void setCalledParams(DomusAsyncParams calledParams) {
		this.calledParams = calledParams;
	}

	public String[] getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String[] versionInfo) {
		this.versionInfo = versionInfo;
	}

}
