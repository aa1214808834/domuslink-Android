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

import java.util.StringTokenizer;

public class LabelHandler {
	/**
	 * Label Parse
	 *
	 * Description: Parses labels so that underscores (_) are either removed or added in substituition of spaces.
	 * Letters are also converted to lowercase or first letter is capitilized.
	 *
	 * @param aLabel represent string to parse
	 * @param add boolean if true add "_" and change case to lower case, if false remove "_" and capitalize first letter of each word)
	 */
	public static String labelParse(String aLabel, boolean add) {
		String strf1 = aLabel.trim();
		if (add == true) {
			strf1 = strf1.replace(' ', '_');
			strf1 = strf1.toLowerCase();
		}
		else {
			strf1 = strf1.replace('_', ' ');
			strf1 = capitalizeFirstLettersTokenizer(strf1);
		}
		
		return strf1;
	}
	
	public static String capitalizeFirstLettersTokenizer ( String s ) {
        
	    final StringTokenizer st = new StringTokenizer( s, " ", true );
	    final StringBuilder sb = new StringBuilder();
	     
	    while ( st.hasMoreTokens() ) {
	        String token = st.nextToken();
	        token = String.format( "%s%s",
	                                Character.toUpperCase(token.charAt(0)),
	                                token.substring(1) );
	        sb.append( token );
	    }
	        
	    return sb.toString();
	                
	}
}

