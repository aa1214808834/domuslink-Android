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

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class DomusAsyncTask extends AsyncTask<DomusAsyncParams, Void, DomusAsyncResults> {
	private ProgressDialog theDomusAsyncDialog;
	private DomusAsyncUpdater pUpdater;

	public void setUpdater(DomusAsyncUpdater theUpdater) {
		pUpdater = theUpdater;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		theDomusAsyncDialog = ProgressDialog.show(pUpdater.getContext(), "", "Communicating with Domus...");
	}

	@Override
	protected DomusAsyncResults doInBackground(DomusAsyncParams... arg0) {
		DomusAsyncParams theParams = arg0[0];
		DomusAsyncResults theResults = new DomusAsyncResults();
		theResults.setCalledParams(theParams);
		switch(theParams.getHandlerMethod())
		{
		case DomusHandler.GET_FLOOR_PLAN:
			try {
				theResults.setLocations(theParams.getTheHandler().getFloorPlan());
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;

		case DomusHandler.GET_ALIAS_STATE:
			try {
				for(Alias anAlias : theParams.getTheAliases()) { 
					theParams.getTheHandler().getAliasState(anAlias);
				}
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		case DomusHandler.GET_ALIASES_BY_LOCATION:
			try {
				theResults.setAliases(theParams.getTheHandler().getAliasesByLocation(theParams.getTheLocation()));
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		case DomusHandler.DIM_ALIAS:
			try {
				for(Alias anAlias : theParams.getTheAliases()) { 
					theParams.getTheHandler().dimAlias(anAlias, theParams.getTheRequestLevel());
				}
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		case DomusHandler.TURN_ON_ALIAS:
			try {
				for(Alias anAlias : theParams.getTheAliases()) { 
					theParams.getTheHandler().turnOnAlias(anAlias);
				}
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		case DomusHandler.TURN_OFF_ALIAS:
			try {
				for(Alias anAlias : theParams.getTheAliases()) { 
					theParams.getTheHandler().turnOffAlias(anAlias);
				}
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;			
		case DomusHandler.GET_VERSION:
			try {
				theResults.setVersionInfo(theParams.getTheHandler().getDomusApiVersion());
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		case DomusHandler.GET_INITIAL:
			try {
				theResults.setLocations(theParams.getTheHandler().getFloorPlan());
				theResults.setAliases(theParams.getTheHandler().getAliasesByLocation(theResults.getLocations()[0]));
				theResults.setModuleTypes(theParams.getTheHandler().getModuleTypes());
			}
			catch(Exception e) {
				theResults.setTheException(e);
			}
			break;
		}

		return theResults;
	}

	@Override
	protected void onCancelled() {
		theDomusAsyncDialog.dismiss();
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(DomusAsyncResults result) {
		super.onPostExecute(result);
		
		theDomusAsyncDialog.dismiss();

		if(result.getTheException() != null)
		{
			pUpdater.handleAsyncException(result.getTheException());
			return;        		
		}

		switch(result.getCalledParams().getHandlerMethod())
		{
		case DomusHandler.GET_FLOOR_PLAN:
			pUpdater.setFloorPlanResult(result.getLocations());
			break;
		case DomusHandler.GET_ALIAS_STATE:
			pUpdater.actionComplete(new String[] {"ALIAS_STATE"});
			break;
		case DomusHandler.GET_ALIASES_BY_LOCATION:
			pUpdater.setAliasesResult(result.getAliases());
			break;
		case DomusHandler.DIM_ALIAS:
			pUpdater.actionComplete(new String[] {"DIM_ALIAS"});
			break;
		case DomusHandler.TURN_ON_ALIAS:
			pUpdater.actionComplete(new String[] {"ON_ALIAS"});
			break;
		case DomusHandler.TURN_OFF_ALIAS:
			pUpdater.actionComplete(new String[] {"OFF_ALIAS"});
			break;			
		case DomusHandler.GET_INITIAL:
			pUpdater.setModuleTypesResult(result.getModuleTypes());
			pUpdater.setFloorPlanResult(result.getLocations());
			pUpdater.setAliasesResult(result.getAliases());
			break;
		case DomusHandler.GET_VERSION:
			pUpdater.actionComplete(result.getVersionInfo());
			break;

		}
	}

}
