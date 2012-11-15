/*
 * Copyright 2012 two forty four a.m. LLC <http://www.twofortyfouram.com>
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

package com.entertailion.android.tasker.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.entertailion.android.tasker.AndroidPlatform;
import com.entertailion.android.tasker.Constants;
import com.entertailion.android.tasker.R;
import com.entertailion.android.tasker.bundle.BundleScrubber;
import com.entertailion.android.tasker.bundle.PluginBundleManager;
import com.entertailion.android.tasker.service.AnymoteService;
import com.entertailion.android.tasker.service.AnymoteService.AnymoteListener;
import com.entertailion.java.anymote.client.AnymoteSender;
import com.entertailion.java.anymote.client.DeviceSelectListener;
import com.entertailion.java.anymote.client.PinListener;
import com.entertailion.java.anymote.connection.TvDevice;
import com.entertailion.java.anymote.connection.TvDiscoveryService;
import com.twofortyfouram.locale.BreadCrumber;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 * @see http://www.twofortyfouram.com/developer.html
 */
public final class EditActivity extends Activity implements AnymoteListener
{
	private static final String LOG_TAG = "EditActivity";

    /**
     * Help URL, used for the {@link R.id#twofortyfouram_locale_menu_help} menu item.
     */
    private static final String HELP_URL = "https://github.com/entertailion/GTV-for-Tasker"; //$NON-NLS-1$

    /**
     * Flag boolean that can only be set to true via the "Don't Save"
     * {@link R.id#twofortyfouram_locale_menu_dontsave} menu item in
     * {@link #onMenuItemSelected(int, MenuItem)}.
     * <p>
     * If true, then this {@code Activity} should return {@link Activity#RESULT_CANCELED} in {@link #finish()}.
     * <p>
     * If false, then this {@code Activity} should generally return {@link Activity#RESULT_OK} with extras
     * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} and
     * {@link com.twofortyfouram.locale.Intent#EXTRA_STRING_BLURB}.
     * <p>
     * There is no need to save/restore this field's state when the {@code Activity} is paused.
     */
    private boolean mIsCancelled = false;
    
    private Spinner deviceSpinner, keycodeSpinner, typeSpinner;
    private EditText urlEditText, dataEditText, appPackageEditText, appActivityEditText;
    private TextView keycodeTextView, urlTextView, dataTextView, appPackageTextView, appActivityTextView;
    private ProgressBar progressBar;
    private TvDiscoveryService tvDiscoveryService;
    private Handler handler = new Handler();
    private List<TvDevice> devices;
    private AnymoteService anymoteService;
    private AndroidPlatform platform;
    private String savedDevice;
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {
        /*
         * ServiceConnection listener methods.
         */
        public void onServiceConnected(ComponentName name, IBinder service) {
        	Log.d(LOG_TAG, "onServiceConnected");
        	anymoteService = ((AnymoteService.AnymoteServiceBinder) service)
                    .getService();
        	anymoteService.attachAnymoteListener(EditActivity.this);
        }

        public void onServiceDisconnected(ComponentName name) {
        	Log.d(LOG_TAG, "onServiceDisconnected");
        	anymoteService.detachAnymoteListener(EditActivity.this);
        	anymoteService = null;
        }
    };
    
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(getIntent());
        BundleScrubber.scrub(getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setupTitleApi11();
        }
        else
        {
            setTitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        
        deviceSpinner = (Spinner) findViewById(R.id.spinner1);
        deviceSpinner.setEnabled(false);
        typeSpinner = (Spinner) findViewById(R.id.spinner3);
        typeSpinner.setEnabled(false);
        List<String> typeList = new ArrayList<String>();
        typeList.add(getString(R.string.field_type_select));
        typeList.add(getString(R.string.field_keycode));
        typeList.add(getString(R.string.field_url));
        typeList.add(getString(R.string.field_data));
        typeList.add(getString(R.string.field_app));
    	ArrayAdapter<String> typeDataAdapter = new ArrayAdapter<String>(EditActivity.this,
    		android.R.layout.simple_spinner_item, typeList);
    	typeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	typeSpinner.setAdapter(typeDataAdapter);
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				switch (pos) {
				case 0:  // nothing
					keycodeTextView.setVisibility(View.GONE);
					keycodeSpinner.setVisibility(View.GONE);
					urlEditText.setVisibility(View.GONE);
					urlTextView.setVisibility(View.GONE);
					dataTextView.setVisibility(View.GONE);
					dataEditText.setVisibility(View.GONE);
					appPackageTextView.setVisibility(View.GONE);
					appPackageEditText.setVisibility(View.GONE);
					appActivityTextView.setVisibility(View.GONE);
					appActivityEditText.setVisibility(View.GONE);
					break;
				case 1: // keycode
					keycodeTextView.setVisibility(View.VISIBLE);
					keycodeSpinner.setVisibility(View.VISIBLE);
					urlEditText.setVisibility(View.GONE);
					urlTextView.setVisibility(View.GONE);
					dataTextView.setVisibility(View.GONE);
					dataEditText.setVisibility(View.GONE);
					appPackageTextView.setVisibility(View.GONE);
					appPackageEditText.setVisibility(View.GONE);
					appActivityTextView.setVisibility(View.GONE);
					appActivityEditText.setVisibility(View.GONE);
					keycodeSpinner.requestFocus();
					break;
				case 2: // url
					keycodeTextView.setVisibility(View.GONE);
					keycodeSpinner.setVisibility(View.GONE);
					urlEditText.setVisibility(View.VISIBLE);
					urlTextView.setVisibility(View.VISIBLE);
					dataTextView.setVisibility(View.GONE);
					dataEditText.setVisibility(View.GONE);
					appPackageTextView.setVisibility(View.GONE);
					appPackageEditText.setVisibility(View.GONE);
					appActivityTextView.setVisibility(View.GONE);
					appActivityEditText.setVisibility(View.GONE);
					urlTextView.requestFocus();
					break;
				case 3: // data
					keycodeTextView.setVisibility(View.GONE);
					keycodeSpinner.setVisibility(View.GONE);
					urlEditText.setVisibility(View.GONE);
					urlTextView.setVisibility(View.GONE);
					dataTextView.setVisibility(View.VISIBLE);
					dataEditText.setVisibility(View.VISIBLE);
					appPackageTextView.setVisibility(View.GONE);
					appPackageEditText.setVisibility(View.GONE);
					appActivityTextView.setVisibility(View.GONE);
					appActivityEditText.setVisibility(View.GONE);
					dataEditText.requestFocus();
					break;
				case 4: // app
					keycodeTextView.setVisibility(View.GONE);
					keycodeSpinner.setVisibility(View.GONE);
					urlEditText.setVisibility(View.GONE);
					urlTextView.setVisibility(View.GONE);
					dataTextView.setVisibility(View.GONE);
					dataEditText.setVisibility(View.GONE);
					appPackageTextView.setVisibility(View.VISIBLE);
					appPackageEditText.setVisibility(View.VISIBLE);
					appActivityTextView.setVisibility(View.VISIBLE);
					appActivityEditText.setVisibility(View.VISIBLE);
					appPackageEditText.requestFocus();
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
        keycodeTextView = (TextView) findViewById(R.id.keycodeText);
    	keycodeSpinner = (Spinner) findViewById(R.id.spinner2);
    	urlTextView = (TextView) findViewById(R.id.urlText);
    	urlEditText = (EditText) findViewById(R.id.url);
    	urlEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keycodeSpinner.setSelection(0); // can only specify keycode OR URL, not both
			}
    		
    	});
    	dataTextView = (TextView) findViewById(R.id.dataText);
    	dataEditText = (EditText) findViewById(R.id.data);
    	appPackageTextView = (TextView) findViewById(R.id.appPackageText);
    	appPackageEditText = (EditText) findViewById(R.id.appPackage);
    	appActivityTextView = (TextView) findViewById(R.id.appActivityText);
    	appActivityEditText = (EditText) findViewById(R.id.appActivity);
    	
    	List<String> keycodeList = new ArrayList<String>();
		for(String name:Constants.getKeycodes(this).keySet()) {
			keycodeList.add(name);
		}
		Collections.sort(keycodeList);
		keycodeList.add(0, getString(R.string.field_keycode_select));  // no selection option
    	ArrayAdapter<String> keycodeDataAdapter = new ArrayAdapter<String>(EditActivity.this,
    		android.R.layout.simple_spinner_item, keycodeList);
    	keycodeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	keycodeSpinner.setAdapter(keycodeDataAdapter);
    	
    	platform = new AndroidPlatform(this);
        tvDiscoveryService = TvDiscoveryService.getInstance(platform);
        // discovering devices can take time, so do it in a thread
        new Thread(new Runnable() {
        	public void run() {
        		devices = tvDiscoveryService.discoverTvs();
                List<String> deviceList = new ArrayList<String>();
                deviceList.add(getString(R.string.field_device_select));  // no selection option
        		for(TvDevice device:devices) {
        			deviceList.add(device.getName()+" / "+device.getAddress().getHostName());
        		}
            	final ArrayAdapter<String> deviceDataAdapter = new ArrayAdapter<String>(EditActivity.this,
            		android.R.layout.simple_spinner_item, deviceList);
            	deviceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            	handler.post(new Runnable() {
        			public void run() {
        				progressBar.setVisibility(View.INVISIBLE);
        				deviceSpinner.setAdapter(deviceDataAdapter);
                    	deviceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                			@Override
                			public void onItemSelected(AdapterView<?> parent, View view,
                					int pos, long id) {
                				// when device is selected, attempt to connect
                				// if device hasn't been paired before, the PIN pairing dialog will be displayed
                				if (anymoteService!=null && devices!=null && pos!=0) {
                					final TvDevice device = devices.get(pos-1);
                					if (anymoteService.getCurrentDevice()!=null) {
                						anymoteService.cancelConnection();
                					}
                					anymoteService.connectDevice(device);
                				}
                			}

                			@Override
                			public void onNothingSelected(AdapterView<?> arg0) {
                				// TODO Auto-generated method stub
                				
                			}
                    		
                    	});
                    	deviceSpinner.setEnabled(true);
                    	deviceSpinner.requestFocus();
                    	typeSpinner.setEnabled(true);
                    	if (savedDevice!=null) {
                    		int pos = 1;
                    		for(TvDevice device:devices) {
                    			if (device.getAddress().getHostAddress().equals(savedDevice)) {
                    				deviceSpinner.setSelection(pos);
                    				break;
                    			}
                    			pos++;
                    		}
                    	}
        			}
        		});
        	}
        }).start();
        
        /*
         * if savedInstanceState is null, then then this is a new Activity instance and a check for
         * EXTRA_BUNDLE is needed
         */
        if (null == savedInstanceState)
        {
            final Bundle forwardedBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

            if (PluginBundleManager.isBundleValid(forwardedBundle))
            {
            	String value = forwardedBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE);
            	if (value!=null) {
            		HashMap<String, String> map = AnymoteService.parseData(this, value);
            		if (map!=null) {
            			savedDevice = map.get(AnymoteService.ANYMOTE_DEVICE);
            			String type = map.get(AnymoteService.ANYMOTE_TYPE);
        	    		String keycode = map.get(AnymoteService.ANYMOTE_KEYCODE);
        	    		String uri = map.get(AnymoteService.ANYMOTE_URI);
        	    		String data = map.get(AnymoteService.ANYMOTE_DATA);
        	    		String appPackage = map.get(AnymoteService.ANYMOTE_APP_PACKAGE);
        	    		String appActivity = map.get(AnymoteService.ANYMOTE_APP_ACTIVITY);
            			int pos = 0;
            			for(String name:keycodeList) {
            				if (name.equals(keycode)) {
            					keycodeSpinner.setSelection(pos);
            					break;
            				}
            				pos++;
            			}
            			if (type!=null) {
            				if (type.equals(getString(R.string.field_keycode))) {
            					typeSpinner.setSelection(1);
            				} else if (type.equals(getString(R.string.field_url))) {
            					typeSpinner.setSelection(2);
            				} else if (type.equals(getString(R.string.field_data))) {
            					typeSpinner.setSelection(3);
            				} else if (type.equals(getString(R.string.field_app))) {
            					typeSpinner.setSelection(4);
            				} else {
            					typeSpinner.setSelection(0);
            				}
            			}
            			if (uri!=null) {
            				urlEditText.setText(uri);
            			}
            			if (data!=null) {
            				dataEditText.setText(data);
            			}
            			if (appPackage!=null) {
            				appPackageEditText.setText(appPackage);
            			}
            			if (appActivity!=null) {
            				appActivityEditText.setText(appActivity);
            			}
            		}
            	}
            }
        }
        /*
         * if savedInstanceState isn't null, there is no need to restore any Activity state directly via
         * onSaveInstanceState(), as the EditText object handles that automatically
         */
    }
    
    @Override
    public void onResume() {
    	Log.d(LOG_TAG, "onResume");
    	// Bind to the AnymoteService
        Intent intent = new Intent(EditActivity.this, AnymoteService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	Log.d(LOG_TAG, "onPause");
    	if (anymoteService!=null) {
			anymoteService.detachAnymoteListener(this);
		}
		unbindService(connection);
    	super.onPause();
    }
    
    @TargetApi(11)
    private void setupTitleApi11()
    {
        CharSequence callingApplicationLabel = null;
        try
        {
            callingApplicationLabel = getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getCallingPackage(), 0));
        }
        catch (final NameNotFoundException e)
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, "Calling package couldn't be found", e); //$NON-NLS-1$
            }
        }
        if (null != callingApplicationLabel)
        {
            setTitle(callingApplicationLabel);
        }
    }

    @Override
    public void finish()
    {
        if (mIsCancelled)
        {
            setResult(RESULT_CANCELED);
        }
        else
        {
        	int pos1 = deviceSpinner.getSelectedItemPosition();
        	int pos2 = typeSpinner.getSelectedItemPosition();
        	if (devices==null || devices.size()==0 || pos1==0 || pos2==0) {
        		setResult(RESULT_CANCELED);
        	}
            else
            {
            	TvDevice device = devices.get(pos1-1);
            	String type = String.valueOf(typeSpinner.getSelectedItem());
                String keycode = String.valueOf(keycodeSpinner.getSelectedItem());
                String uri = String.valueOf(urlEditText.getText()).trim().replaceAll("\\n", "");
                if (uri.toLowerCase().startsWith("http")) {
                	uri = uri.toLowerCase();
                }
                String data = String.valueOf(dataEditText.getText()).replaceAll("\\n", "");
                String appPackage = String.valueOf(appPackageEditText.getText()).trim().replaceAll("\\n", "");
                String appActivity = String.valueOf(appActivityEditText.getText()).trim().replaceAll("\\n", "");
                String value = device.getAddress().getHostAddress()+"\n"+type+"\n"+keycode+"\n"+uri+"\n"+data+"\n"+appPackage+"\n"+appActivity;
                
                /*
                 * This is the result Intent to Locale
                 */
                final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
                 * that anything placed in this Bundle must be available to Locale's class loader. So storing
                 * String, int, and other standard objects will work just fine. However Parcelable objects
                 * must also be Serializable. And Serializable objects must be standard Java objects (e.g. a
                 * private subclass to this plug-in cannot be stored in the Bundle, as Locale's classloader
                 * will not recognize it).
                 */
                final Bundle resultBundle = new Bundle();
                resultBundle.putInt(PluginBundleManager.BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(this));
                resultBundle.putString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE, value);

                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                /*
                 * This is the blurb concisely describing what your setting's state is. This is simply used
                 * for display in the UI.
                 */
                if (type!=null) {
    				if (type.equals(getString(R.string.field_keycode))) {
    					resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, keycode);
    				} else if (type.equals(getString(R.string.field_url))) {
    					if (uri.startsWith("http")) {  // web site URL
                    		resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, getBaseDomain(uri));
                    	} else { // intent URI
                    		resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, uri);
                    	}
    				} else if (type.equals(getString(R.string.field_data))) {
    					resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, data);
    				} else if (type.equals(getString(R.string.field_app))) {
    					resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, appPackage);
    				}
    			}

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }
    
    // http://stackoverflow.com/questions/4826061/what-is-the-fastest-way-to-get-the-domain-host-name-from-a-url
    /**
     * Will take a url such as http://www.stackoverflow.com and return www.stackoverflow.com
     * 
     * @param url
     * @return
     */
    private static String getHost(String url){
        if(url == null || url.length() == 0)
            return "";

        int doubleslash = url.indexOf("//");
        if(doubleslash == -1)
            doubleslash = 0;
        else
            doubleslash += 2;

        int end = url.indexOf('/', doubleslash);
        end = end >= 0 ? end : url.length();

        return url.substring(doubleslash, end);
    }

    /**  Based on : http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/webkit/CookieManager.java#CookieManager.getBaseDomain%28java.lang.String%29
     * Get the base domain for a given host or url. E.g. mail.google.com will return google.com
     * @param host 
     * @return 
     */
    private static String getBaseDomain(String url) {
        String host = getHost(url);

        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            return host.substring(startIndex);
        } else {
            return host;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        /*
         * inflate the default menu layout from XML
         */
        getMenuInflater().inflate(R.menu.twofortyfouram_locale_help_save_dontsave, menu);

        /*
         * Set up the breadcrumbs for the ActionBar
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setupActionBarApi11();
        }
        /*
         * Dynamically load the home icon from the host package for Ice Cream Sandwich or later. Note that
         * this leaves Honeycomb devices without the host's icon in the ActionBar, but eventually all
         * Honeycomb devices should receive an OTA to Ice Cream Sandwich so this problem will go away.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            setupActionBarApi14();
        }

        return true;
    }

    @TargetApi(11)
    private void setupActionBarApi11()
    {
        getActionBar().setSubtitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
    }

    @TargetApi(14)
    private void setupActionBarApi14()
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * Note: There is a small TOCTOU error here, in that the host could be uninstalled right after
         * launching the plug-in. That would cause getApplicationIcon() to return the default application
         * icon. It won't fail, but it will return an incorrect icon.
         * 
         * In practice, the chances that the host will be uninstalled while the plug-in UI is running are very
         * slim.
         */
        try
        {
            getActionBar().setIcon(getPackageManager().getApplicationIcon(getCallingPackage()));
        }
        catch (final NameNotFoundException e)
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.w(Constants.LOG_TAG, "An error occurred loading the host's icon", e); //$NON-NLS-1$
            }
        }
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item)
    {
        final int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        else if (id == R.id.twofortyfouram_locale_menu_help)
        {
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(HELP_URL)));
            }
            catch (final Exception e)
            {
                if (Constants.IS_LOGGABLE)
                {
                    Log.e(Constants.LOG_TAG, "Couldn't start Activity", e);
                }
            }

            return true;
        }
        else if (id == R.id.twofortyfouram_locale_menu_dontsave)
        {
            mIsCancelled = true;
            finish();
            return true;
        }
        else if (id == R.id.twofortyfouram_locale_menu_save)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    /**
     * ClientListener callback when attempting a connecion to a Google TV device
     * @see com.entertailion.java.anymote.client.ClientListener#attemptToConnect(com.entertailion.java.anymote.connection.TvDevice)
     */
    public void attemptToConnect(TvDevice device) {
    	Log.d(LOG_TAG, "attemptToConnect");
    	handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
	}

    /** 
	 * ClientListener callback when Anymote is conneced to a Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnected(com.entertailion.java.anymote.client.AnymoteSender)
	 */
	public void onConnected(final AnymoteSender anymoteSender) {
		Log.d(LOG_TAG, "onConnected");
		handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	    if (anymoteSender != null) {
	    	Log.d(LOG_TAG, anymoteService.getCurrentDevice().toString());
	    } else {
	    	Log.d(LOG_TAG, "Connection failed");
	    }
	}

	/**
	 * ClientListener callback when the Anymote service is disconnected from the Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onDisconnected()
	 */
	public void onDisconnected() { 
		Log.d(LOG_TAG, "onDisconnected");
		handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	    
	    if (!platform.isWifiAvailable()) {
	    	// run on the main UI thread
			handler.post(new Runnable() {
				public void run() {
					AlertDialog alertDialog = buildNoWifiDialog();
					alertDialog.show();
				}
			});
			return;
		}
	}

	/**
	 * ClientListener callback when the attempted connection to the Google TV device failed
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnectionFailed()
	 */
	public void onConnectionFailed() {
		Log.d(LOG_TAG, "onConnectionFailed");
		handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	    
	    if (!platform.isWifiAvailable()) {
	    	// run on the main UI thread
			handler.post(new Runnable() {
				public void run() {
					AlertDialog alertDialog = buildNoWifiDialog();
					alertDialog.show();
				}
			});
			return;
		}
	}
	
	/** 
	 * InputListener callback for feedback on starting the device discovery process
	 * @see com.entertailion.java.anymote.client.InputListener#onDiscoveringDevices()
	 */
	public void onDiscoveringDevices() {
		Log.d(LOG_TAG, "onDiscoveringDevices");
		handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/** 
	 * InputListener callback when a Google TV device needs to be selected
	 * @see com.entertailion.java.anymote.client.InputListener#onSelectDevice(java.util.List, com.entertailion.java.anymote.client.DeviceSelectListener)
	 */
	public void onSelectDevice(final List<TvDevice> trackedDevices, final DeviceSelectListener listener) {
		Log.d(LOG_TAG, "onSelectDevice");
		handler.post(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
		// nothing to do; devices are selected in spinner
	}
	
	/**
	 * InputListener callback when PIN required to pair with Google TV device
	 * @see com.entertailion.java.anymote.client.InputListener#onPinRequired(com.entertailion.java.anymote.client.PinListener)
	 */
	public void onPinRequired(final PinListener listener) {
		Log.d(LOG_TAG, "onPinRequired");
		// run on the main UI thread
		handler.post(new Runnable() {
			public void run() {
				PairingPINDialogBuilder pairingPINDialogBuilder = new PairingPINDialogBuilder(EditActivity.this, listener);
				pairingPINDialogBuilder.show();
			}
		});
	}
	
	/**
     * Construct a no-wifi dialog.
     * 
     * @return AlertDialog asking user to turn on WIFI.
     */
    private AlertDialog buildNoWifiDialog() { 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.finder_wifi_not_available);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.finder_configure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.finder_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
            }
        });
        return builder.create();
    }
    
}