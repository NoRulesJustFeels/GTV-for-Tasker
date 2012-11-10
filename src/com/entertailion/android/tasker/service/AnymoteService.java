/*
 * Copyright 2012 ENTERTAILION LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.entertailion.android.tasker.service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.entertailion.android.tasker.AndroidPlatform;
import com.entertailion.android.tasker.Constants;
import com.entertailion.java.anymote.client.AnymoteClientService;
import com.entertailion.java.anymote.client.AnymoteSender;
import com.entertailion.java.anymote.client.ClientListener;
import com.entertailion.java.anymote.client.DeviceSelectListener;
import com.entertailion.java.anymote.client.InputListener;
import com.entertailion.java.anymote.client.PinListener;
import com.entertailion.java.anymote.connection.TvDevice;
import com.google.anymote.Key.Code;

public class AnymoteService extends Service implements ClientListener, InputListener {
	private static final String LOG_TAG = "AnymoteService";
	public static final String ANYMOTE_INOKE = "com.entertailion.android.tasker.INVOKE";
	public static final String ANYMOTE_DEVICE = "com.entertailion.android.tasker.DEVICE";
	public static final String ANYMOTE_KEYCODE = "com.entertailion.android.tasker.KEYCODE";
	private ArrayList<AnymoteListener> anymoteListeners;
	private AnymoteClientService anymoteClientService;
    private AndroidPlatform platform;
    private AnymoteSender anymoteSender;
    private String keycode; // TODO change to queue
    private String device;
	
	/**
     * All client applications should implement this listener. It provides
     * callbacks when the state of connection to the Anymote service running on
     * Google TV device changes.
     */
    public interface AnymoteListener { 
    	/**
         * This callback method is called when a connection is attempted to a device.
         * 
         * @param device The Google TV device connecting to.
         */
    	public void attemptToConnect(TvDevice device);
        /**
         * This callback method is called when connection to Anymote service has
         * been established.
         * 
         * @param anymoteSender The proxy to send Anymote messages.
         */
        public void onConnected(AnymoteSender anymoteSender);

        /**
         * This callback method is called when connection to Anymote service is
         * lost.
         */
        public void onDisconnected();

        /**
         * This callback method is called when there was a error in establishing
         * connection to the Anymote service.
         */
        public void onConnectionFailed();
        
        /**
    	 * Called before Google TV devices are discovered
    	 */
    	public void onDiscoveringDevices();
    	
    	/**
    	 * Called when a device has to be selected from the list of discovered Google TV devices
    	 * @param devices
    	 * @param listener
    	 */
    	public void onSelectDevice(List<TvDevice> devices, DeviceSelectListener listener);
    	
    	/**
    	 * Called when a PIN is required to pair with a Google TV device
    	 * @param listener
    	 */
    	public void onPinRequired(PinListener listener);

    }
	
	@Override
    public void onCreate() {
		Log.d(LOG_TAG, "onCreate");
        super.onCreate();
        initialize();
    }

	private void initialize() {
		anymoteListeners = new ArrayList<AnymoteListener>();
		platform = new AndroidPlatform(this);
		
		// start in thread since creating the keystore can take time...
		new Thread(new Runnable() {
			public void run() {
				anymoteClientService = AnymoteClientService.getInstance(platform);
				anymoteClientService.attachClientListener(AnymoteService.this);  // client service callback
				anymoteClientService.attachInputListener(AnymoteService.this);  // user interaction callback
				
				// handle keycode that was sent before this thread finished
				if (keycode!=null && device!=null) {
					anymoteClientService.cancelConnection();
					try {
				        Inet4Address address = (Inet4Address) InetAddress.getByName(device);
				        anymoteClientService.connectDevice(new TvDevice(device, address));
				    } catch (UnknownHostException e) {
				    	Log.d(LOG_TAG, "UnknownHostException", e);
				    }
					device = null;
				}
			}
		}).start();
	}
	
	@Override
    public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy");
		if (anymoteClientService!=null) {
    		if (anymoteClientService.getCurrentDevice()!=null) {
				anymoteClientService.cancelConnection();
			}
    		anymoteClientService.detachClientListener(this);
			anymoteClientService.detachInputListener(this);
    	}
		anymoteClientService = null;
        super.onDestroy();
    }
	
	/*
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand");
    	if (intent!=null && intent.getAction()!=null) {
    		if (intent.getAction().equals(ANYMOTE_INOKE)) {
    			String targetDevice = intent.getStringExtra(ANYMOTE_DEVICE);
    			String targetKeycode = intent.getStringExtra(ANYMOTE_KEYCODE);
    			if (anymoteClientService!=null) {
	    			keycode = null;
	    			// connect to device
	    			TvDevice device = getCurrentDevice();
					if (device!=null && device.getAddress().getHostAddress().equals(targetDevice)) {
						if (anymoteSender!=null) {
							sendKeyPress(targetKeycode);
						} else {
							keycode = targetKeycode;
							anymoteClientService.reconnect();
						}
					} else {
						keycode = targetKeycode;
						anymoteClientService.cancelConnection();
						try {
					        Inet4Address address = (Inet4Address) InetAddress.getByName(targetDevice);
					        anymoteClientService.connectDevice(new TvDevice(targetDevice, address));
					    } catch (UnknownHostException e) {
					    	Log.d(LOG_TAG, "UnknownHostException", e);
					    }
					}
    			} else {
    				keycode = targetKeycode;
    				device = targetDevice;
    			}
    		}
    	}
        return START_STICKY;
    }

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
    public IBinder onBind(Intent intent) {
        return new AnymoteServiceBinder();
    }

    public class AnymoteServiceBinder extends Binder {
        /**
         * Local binder to the service.
         * 
         * @return binder to the service.
         */
        public AnymoteService getService() {
            return AnymoteService.this;
        }
    }
    
    /**
     * Adds anymote listeners.
     * 
     * @param listener anymote listener.
     */
    public void attachAnymoteListener(AnymoteListener listener) {
        anymoteListeners.add(listener);
    }

    /**
     * Removes anymote listener.
     * 
     * @param listener anymote listener.
     */
    public void detachAnymoteListener(AnymoteListener listener) {
        anymoteListeners.remove(listener);
    }
    
    /**
     * ClientListener callback when attempting a connecion to a Google TV device
     * @see com.entertailion.java.anymote.client.ClientListener#attemptToConnect(com.entertailion.java.anymote.connection.TvDevice)
     */
    public void attemptToConnect(TvDevice device) {
    	for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.attemptToConnect(device);
        }
	}

    /** 
	 * ClientListener callback when Anymote is conneced to a Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnected(com.entertailion.java.anymote.client.AnymoteSender)
	 */
	public void onConnected(final AnymoteSender anymoteSender) {
		this.anymoteSender = anymoteSender;
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onConnected(anymoteSender);
        }
		
		if (anymoteSender != null) {
	    	Log.d(LOG_TAG, anymoteClientService.getCurrentDevice().toString());
	    	sendKeyPress(keycode);
	    	keycode = null; // reset for the next keycode
		}
	}
	
	/**
	 * Send a keypress to the Google TV device using Anymote
	 * @param keycode
	 */
	private void sendKeyPress(String keycode) {
		if (keycode!=null) {
			LinkedHashMap<String, Code> keycodes = Constants.getKeycodes(this);
	    	for (Map.Entry<String, Code> entry : keycodes.entrySet()) { 
	    		if (entry.getKey().equals(keycode)) {
	    			try {
						anymoteSender.sendKeyPress(entry.getValue());
					} catch (Exception e) {
						Log.e(LOG_TAG, "sendKeyPress", e);
					}
	    			break;
	    		}
	    	}
		}
	}

	/**
	 * ClientListener callback when the Anymote service is disconnected from the Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onDisconnected()
	 */
	public void onDisconnected() { 
		this.anymoteSender = null;
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onDisconnected();
        }
	}

	/**
	 * ClientListener callback when the attempted connection to the Google TV device failed
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnectionFailed()
	 */
	public void onConnectionFailed() {
		this.anymoteSender = null;
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onConnectionFailed();
        }
	}
	
	/** 
	 * InputListener callback for feedback on starting the device discovery process
	 * @see com.entertailion.java.anymote.client.InputListener#onDiscoveringDevices()
	 */
	public void onDiscoveringDevices() {
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onDiscoveringDevices();
        }
	}
	
	/** 
	 * InputListener callback when a Google TV device needs to be selected
	 * @see com.entertailion.java.anymote.client.InputListener#onSelectDevice(java.util.List, com.entertailion.java.anymote.client.DeviceSelectListener)
	 */
	public void onSelectDevice(final List<TvDevice> trackedDevices, final DeviceSelectListener deviceSelectListener) {
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onSelectDevice(trackedDevices, deviceSelectListener);
        }
	}
	
	/**
	 * InputListener callback when PIN required to pair with Google TV device
	 * @see com.entertailion.java.anymote.client.InputListener#onPinRequired(com.entertailion.java.anymote.client.PinListener)
	 */
	public void onPinRequired(final PinListener pinListener) {
		for (AnymoteListener listener : (ArrayList<AnymoteListener>)anymoteListeners.clone()) {
            listener.onPinRequired(pinListener);
        }
	}
	
	// Proxies for AnymoteClientService
	public TvDevice getCurrentDevice() {
		if (anymoteClientService!=null) {
			return anymoteClientService.getCurrentDevice();
		}
		return null;
	}
	
	public void cancelConnection() {
		if (anymoteClientService!=null) {
			anymoteClientService.cancelConnection();
		}
	}
	
	public void connectDevice(TvDevice device) {
		if (anymoteClientService!=null) {
			anymoteClientService.connectDevice(device);
		}
	}
	
	public AnymoteSender getAnymoteSender() {
		if (anymoteClientService!=null) {
			return anymoteSender;
		}
		return null;
	}

}
