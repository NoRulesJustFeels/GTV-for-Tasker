/*
 * Copyright (C) 2012 ENTERTAILION, LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.entertailion.android.tasker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.entertailion.java.anymote.util.Platform;

public class AndroidPlatform implements Platform {
	private static final String LOG_TAG = "AndroidPlatform";
	private Context context;
	private WifiManager wifiManager;

	public AndroidPlatform(Context context) {
		this.context = context;
		wifiManager = (WifiManager) context.getSystemService(Activity.WIFI_SERVICE);
	}

	/**
	 * Open a file for output
	 * @param name
	 * @param mode
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException { 
		return context.openFileOutput(name, mode);
	}
	
	/**
	 * Open a file for input
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileInputStream openFileInput(String name) throws FileNotFoundException {
		return context.openFileInput(name);
	}
	
	/**
	 * Get the network broadcast address. Used to listen for multi-cast messages to discover Google TV devices
	 * @return
	 */
	public Inet4Address getBroadcastAddress() {
		Inet4Address broadcastAddress;
        if (!isWifiAvailable()) {
            return null;
        }

        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        if (dhcp == null) {
            return null;
        }

        int broadcast = dhcp.ipAddress | ~dhcp.netmask;
        byte[] broadcastOctets;

        if (java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.BIG_ENDIAN) {
            broadcastOctets = new byte[] {
                    (byte) ((broadcast >> 24) & 0xff),
                    (byte) ((broadcast >> 16) & 0xff), (byte) ((broadcast >> 8) & 0xff),
                    (byte) (broadcast & 0xff) };
        } else {
            broadcastOctets = new byte[] {
                    (byte) (broadcast & 0xff),
                    (byte) ((broadcast >> 8) & 0xff), (byte) ((broadcast >> 16) & 0xff),
                    (byte) ((broadcast >> 24) & 0xff) };
        }

        try {
            broadcastAddress = (Inet4Address) InetAddress.getByAddress(broadcastOctets);
        } catch (IOException e) {
            broadcastAddress = null;
        }
        return broadcastAddress;
	}
	
	/**
     * Checks if wifi connectivity is available.
     * 
     * @return boolean indicating if wifi is available.
     */
    public boolean isWifiAvailable() {
        if (wifiManager.isWifiEnabled()) {
            WifiInfo info = wifiManager.getConnectionInfo();
            return (info != null && info.getIpAddress() != 0);
        }
        return false;
    }

    /**
     * Retuns wifi network name.
     * 
     * @return network name.
     */
    private String getNetworkName() {
        if (!isWifiAvailable()) {
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        return (info != null) ? info.getSSID() : null;
    }
	
	/**
     * Get the platform version code
     * @return versionCode
     */
	public int getVersionCode() {
		try {
            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            Log.d(LOG_TAG, "cannot retrieve version number, package name not found");
        }
        return -1;
	}
	
	private String getUniqueId() {
        String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // null ANDROID_ID is possible on emulator
        return id != null ? id : "emulator";
    }
	
	/**
	 * Get platform strings
	 * @param id
	 * @return
	 */
	public String getString(int id) {
		switch (id) {
			case NAME:
				return Build.MANUFACTURER + " " + Build.MODEL;
			case CERTIFICATE_NAME: 
				return Build.PRODUCT + "/" + Build.DEVICE + "/" + Build.MODEL;
			case UNIQUE_ID: 
				return getUniqueId()+"-gtv-for-tasker";  // needs to be unique per app so that multiple Anymote clients can run on the same device
			case NETWORK_NAME: 
				return getNetworkName();
			default:
				return null;
		}
	}
}
