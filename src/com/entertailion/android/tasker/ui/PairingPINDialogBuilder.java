/*
 * Copyright (C) 2012 Google Inc.  All rights reserved.
 * Copyright (C) 2012 ENTERTAILION, LLC. All rights reserved.
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

package com.entertailion.android.tasker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.entertailion.android.tasker.R;
import com.entertailion.java.anymote.client.PinListener;

/**
 * This dialog allows the user to enter the secret pairing code during the
 * device pairing phase.
 */
public class PairingPINDialogBuilder {
    private final Context context;
    private PinListener pinListener;
    private AlertDialog alertDialog;

    /**
     * Constructor
     * 
     * @param activity The context of the Activity that owns the Pairing PIN
     *            Dialog.
     */
    public PairingPINDialogBuilder(final Context activity, PinListener pinListener) {
        context = activity;
        this.pinListener = pinListener;
    }

    private AlertDialog createPairingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pairing, null);
        final EditText pinEditText = (EditText) view.findViewById(R.id.pairing_pin_entry);
        
        builder.setPositiveButton(R.string.pairing_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = null;
                pinListener.onSecretEntered(pinEditText.getText().toString());
            }
        }).setNegativeButton(R.string.pairing_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = null;
                pinListener.onCancel();
            }
        }).setCancelable(false).setTitle(R.string.pairing_label).setView(view);
        
        AlertDialog alertDialog =  builder.create();
        // show the keyboard
        alertDialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return alertDialog;
    }

    /**
     * Displays the Pairing PIN Dialog to the user.
     */
    public void show() {
        alertDialog = createPairingDialog();
        alertDialog.show();
    }
}
