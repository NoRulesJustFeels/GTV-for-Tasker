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

package com.entertailion.android.tasker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.entertailion.android.tasker.Constants;
import com.entertailion.android.tasker.bundle.BundleScrubber;
import com.entertailion.android.tasker.bundle.PluginBundleManager;
import com.entertailion.android.tasker.service.AnymoteService;
import com.entertailion.android.tasker.ui.EditActivity;

/**
 * This is the "fire" BroadcastReceiver for a Locale Plug-in setting.
 * @see http://www.twofortyfouram.com/developer.html
 */
public final class FireReceiver extends BroadcastReceiver
{
	private static final String LOG_TAG = "FireReceiver";
	
    /**
     * @param context {@inheritDoc}.
     * @param intent the incoming {@link com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING} Intent. This
     *            should contain the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} that was saved by
     *            {@link EditActivity} and later broadcast by Locale.
     */
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be sure to be strict on input parameters! A malicious third-party app could always send an
         * empty or otherwise malformed Intent. And since Locale applies settings in the background, the
         * plug-in definitely shouldn't crash in the background.
         */

        /*
         * Locale guarantees that the Intent action will be ACTION_FIRE_SETTING
         */
        if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(intent);
        BundleScrubber.scrub(intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

        /*
         * Final verification of the plug-in Bundle before firing the setting.
         */
        if (PluginBundleManager.isBundleValid(bundle))
        {
        	String value = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE);
        	if (value!=null) {
	            //Toast.makeText(context, value, Toast.LENGTH_LONG).show();
	            final String[] values = value.split("\\n");
	            if (values.length==2) {
	            	// Bind to the AnymoteService
	                Intent serviceIntent = new Intent(context, AnymoteService.class);
	                serviceIntent.setAction(AnymoteService.ANYMOTE_INOKE);
	                serviceIntent.putExtra(AnymoteService.ANYMOTE_DEVICE, values[0]);
	                serviceIntent.putExtra(AnymoteService.ANYMOTE_KEYCODE, values[1]);
	                context.startService(serviceIntent);
	            }
        	}
        }
    }
}