/*
 * domus.Link api handler
 * 
 * Source derived from com.example.android.simplewiktionary example code.
 * 
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
package com.domuslink.communication;

import com.domuslink.DomusLink.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Helper methods to simplify talking with and parsing responses from a
 * lightweight domus.Link API. Before making any requests, you should call
 * {@link #prepareUserAgent(Context)} to generate a User-Agent string based on
 * your application package name and version.
 */
public class ApiHandler {
    private static final String TAG = "ApiHandler";

    /**
     * Partial URL to use when requesting a specific function
     * in domus.Link. Use {@link String#format(String, Object...)} to insert
     * the desired function and parameters.
     */
    private static final String DOMUSLINKAPI_PARAM = "//%s/api.php/%s/%s";
    private static final String DOMUSLINKAPI_NOPARAM = "//%s/api.php/%s";

    private static final int GET_TYPE = 1;
    private static final int POST_TYPE = 2;
    
    /**
     * {@link StatusLine} HTTP status code when no server error has occurred.
     */
    private static final int HTTP_STATUS_OK = 200;

    /**
     * Shared buffer used by {@link #getUrlContent(String)} when reading results
     * from an API request.
     */
    private static byte[] sBuffer = new byte[512];

    /**
     * User-agent string to use when making requests. Should be filled using
     * {@link #prepareUserAgent(Context)} before making any other calls.
     */
    private static String sUserAgent = null;

    private static String sPassword = null;
    private static String sHost = null;
    
    /**
     * Thrown when there were problems contacting the remote API server, either
     * because of a network error, or the server returned a bad status code.
     */
    public static class ApiException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -3033076470970576123L;

		public ApiException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }
    }

    /**
     * Thrown when there were problems parsing the response to an API call,
     * either because the response was empty, or it was malformed.
     */
    public static class ParseException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -7466356578011545491L;

		public ParseException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }

    /**
     * Prepare the internal User-Agent string for use. This requires a
     * {@link Context} to pull the package name and version number for this
     * application.
     */
    public static void prepareUserAgent(Context context, String thePassword, String theHost) {
    	sPassword = thePassword;
    	sHost = theHost;
        try {
            // Read package name and version number from manifest
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            sUserAgent = String.format(context.getString(R.string.template_user_agent),
                    info.packageName, info.versionName);

        } catch(NameNotFoundException e) {
            Log.e(TAG, "Couldn't find package information in PackageManager", e);
        }
    }

    /**
     * Read and return the content for a specific domus.Link function. This makes a
     * lightweight API call, and trims out just the page content returned.
     * Because this call blocks until results are available, it should not be
     * run from a UI thread.
     *
     * @param baseUrl The base url location of domus.Link
     * @param function The function of the domus.Link api to call
     * @param param The extra parameters to supply to the function
     * @return the JSON object.
     * @throws ApiException If any connection or server error occurs.
     * @throws ParseException If there are problems parsing the response.
     */
    public static JSONObject getPageContent(ApiCookieHandler cookieHandler, String function, String param)
    	throws ApiException, ParseException {
    	return(pageContent(GET_TYPE, sHost, cookieHandler, function, param));
    }
    
    public static JSONObject postPageContent(ApiCookieHandler cookieHandler, String function, String param)
    throws ApiException, ParseException {
    	return(pageContent(POST_TYPE, sHost, cookieHandler, function, param));
    }

    /**
     * Read and return the content for a specific domus.Link function. This makes a
     * lightweight API call, and trims out just the page content returned.
     * Because this call blocks until results are available, it should not be
     * run from a UI thread.
     *
     * @param type The type of call for either GET or POST
     * @param baseUrl The base url location of domus.Link
     * @param function The function of the domus.Link api to call
     * @param param The extra parameters to supply to the function
     * @return the JSON object.
     * @throws ApiException If any connection or server error occurs.
     * @throws ParseException If there are problems parsing the response.
     */

    protected static JSONObject pageContent(int type, String baseUrl, ApiCookieHandler cookieHandler, String function, String param)
        throws ApiException, ParseException {
    	String content;
    	URI commandURI = null;
    	String uriPath;
    	
   		if(param == null)
   			uriPath = String.format(DOMUSLINKAPI_NOPARAM, sHost, function);
   		else
   			uriPath = String.format(DOMUSLINKAPI_PARAM, sHost, function, param);

   		// Query the API for content
    	try {
   			commandURI = new URI("http", uriPath, null);
    	}
    	catch(Exception e)
    	{
        	Log.e(TAG, "build URI: Url components: ["+baseUrl+"] ["+function+"] ["+param+"] and uriPath of ["+uriPath+"]");
    		throw new ParseException("Problem parsing for URI", e);
    	}

    	content = urlContent(type, commandURI, cookieHandler);
    	
        try {
            // Drill into the JSON response to find the content body
//        	Log.d("DomusLink", "ApiHandler: Content <"+content+">");
            JSONObject response = new JSONObject(content);
            return response;
        } catch (JSONException e) {
            throw new ParseException("Problem parsing API response", e);
        }
    }

    /**
     * Pull the raw text content of the given URL. This call blocks until the
     * operation has completed, and is synchronized because it uses a shared
     * buffer {@link #sBuffer}.
     *
     * @param type The type of either a GET or POST for the request
     * @param commandURI The constructed URI for the path
     * @return The raw content returned by the server.
     * @throws ApiException If any connection or server error occurs.
     */
    protected static synchronized String urlContent(int type, URI commandURI, ApiCookieHandler cookieHandler) throws ApiException {
    	HttpResponse response;
    	HttpRequestBase request;
    	
        if (sUserAgent == null) {
            throw new ApiException("User-Agent string must be prepared");
        }

        // Create client and set our specific user-agent string
        DefaultHttpClient client = new DefaultHttpClient();
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("", sPassword);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds); 
        client.setCredentialsProvider(credsProvider);
        CookieStore cookieStore = cookieHandler.getCookieStore();
    	if(cookieStore != null)
    	{
    		boolean expiredCookies = false;
    		Date nowTime = new Date();
            for(Cookie theCookie : cookieStore.getCookies())
            {
            	if(theCookie.isExpired(nowTime))
            		expiredCookies = true;
            }
            if(!expiredCookies)
            	client.setCookieStore(cookieStore);
            else
            {
            	cookieHandler.setCookieStore(null);
            	cookieStore = null;
            }
    	}

        try {
        	if(type == POST_TYPE)
                request = new HttpPost(commandURI);
        	else
                request = new HttpGet(commandURI);

            request.setHeader("User-Agent", sUserAgent);
            response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
            	Log.e(TAG, "urlContent: Url issue: "+commandURI.toString()+" with status: "+status.toString());
                throw new ApiException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            if(cookieStore == null)
            {
	            List<Cookie> realCookies = client.getCookieStore().getCookies();
	            if (!realCookies.isEmpty()) {
	            	BasicCookieStore newCookies = new BasicCookieStore();
	                for (int i = 0; i < realCookies.size(); i++) {
	                	newCookies.addCookie(realCookies.get(i));
//	                	Log.d(TAG, "aCookie - " + realCookies.get(i).toString());
	                }
	                cookieHandler.setCookieStore(newCookies);
	            }
            }
            
            // Return result from buffered stream
            return content.toString();
        }
        catch (IOException e) {
        	Log.e(TAG, "urlContent: client execute: "+commandURI.toString());
            throw new ApiException("Problem communicating with API", e);
        }
        catch(IllegalArgumentException e)
        {
        	Log.e(TAG, "urlContent: client execute: "+commandURI.toString());
            throw new ApiException("Problem communicating with API", e);       	
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            client.getConnectionManager().shutdown();
        }
    }
}