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

package com.entertailion.android.tasker;

import java.util.LinkedHashMap;

import com.google.anymote.Key.Code;

import android.content.Context;

/**
 * Class of constants used by this Locale plug-in.
 */
public final class Constants
{
    /**
     * Private constructor prevents instantiation
     * 
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Constants()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }

    /**
     * Log tag for logcat messages
     */
    // TODO: Change this to your application's own log tag
    public static final String LOG_TAG = "ToastSetting"; //$NON-NLS-1$

    /**
     * Flag to enable logcat messages.
     */
    public static final boolean IS_LOGGABLE = false;

    /**
     * Flag to enable runtime checking of method parameters
     */
    public static final boolean IS_PARAMETER_CHECKING_ENABLED = false;

    /**
     * Flag to enable runtime checking of whether a method is called on the correct thread
     */
    public static final boolean IS_CORRECT_THREAD_CHECKING_ENABLED = false;

    /**
     * Determines the "versionCode" in the {@code AndroidManifest}.
     * 
     * @param context to read the versionCode. Cannot be null.
     * @return versionCode of the app.
     * @throws IllegalArgumentException if {@code context} is null.
     */
    public static int getVersionCode(final Context context)
    {
        if (Constants.IS_PARAMETER_CHECKING_ENABLED)
        {
            if (null == context)
            {
                throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
            }
        }

        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (final UnsupportedOperationException e)
        {
            /*
             * This exception is thrown by test contexts
             */
            return 1;
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Return all the key codes supported by Anymote protocol
     * @see https://code.google.com/p/anymote-protocol/source/browse/proto/keycodes.proto
     * @param context
     * @return
     */
    public static LinkedHashMap<String, Code> getKeycodes(Context context) {
    	LinkedHashMap<String, Code> keycodes = new LinkedHashMap<String, Code>();
    	keycodes.put(context.getString(R.string.KEYCODE_SOFT_LEFT), Code.KEYCODE_SOFT_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_SOFT_RIGHT), Code.KEYCODE_SOFT_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_HOME), Code.KEYCODE_HOME);
    	keycodes.put(context.getString(R.string.KEYCODE_BACK), Code.KEYCODE_BACK);
    	keycodes.put(context.getString(R.string.KEYCODE_CALL), Code.KEYCODE_CALL);
    	keycodes.put(context.getString(R.string.KEYCODE_0), Code.KEYCODE_0);
    	keycodes.put(context.getString(R.string.KEYCODE_1), Code.KEYCODE_1);
    	keycodes.put(context.getString(R.string.KEYCODE_2), Code.KEYCODE_2);
    	keycodes.put(context.getString(R.string.KEYCODE_3), Code.KEYCODE_3);
    	keycodes.put(context.getString(R.string.KEYCODE_4), Code.KEYCODE_4);
    	keycodes.put(context.getString(R.string.KEYCODE_5), Code.KEYCODE_5);
    	keycodes.put(context.getString(R.string.KEYCODE_6), Code.KEYCODE_6);
    	keycodes.put(context.getString(R.string.KEYCODE_7), Code.KEYCODE_7);
    	keycodes.put(context.getString(R.string.KEYCODE_8), Code.KEYCODE_8);
    	keycodes.put(context.getString(R.string.KEYCODE_9), Code.KEYCODE_9);
    	keycodes.put(context.getString(R.string.KEYCODE_STAR), Code.KEYCODE_STAR);
    	keycodes.put(context.getString(R.string.KEYCODE_POUND), Code.KEYCODE_POUND);
    	keycodes.put(context.getString(R.string.KEYCODE_DPAD_UP), Code.KEYCODE_DPAD_UP);
    	keycodes.put(context.getString(R.string.KEYCODE_DPAD_DOWN), Code.KEYCODE_DPAD_DOWN);
    	keycodes.put(context.getString(R.string.KEYCODE_DPAD_LEFT), Code.KEYCODE_DPAD_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_DPAD_RIGHT), Code.KEYCODE_DPAD_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_DPAD_CENTER), Code.KEYCODE_DPAD_CENTER);
    	keycodes.put(context.getString(R.string.KEYCODE_VOLUME_UP), Code.KEYCODE_VOLUME_UP);
    	keycodes.put(context.getString(R.string.KEYCODE_VOLUME_DOWN), Code.KEYCODE_VOLUME_DOWN);
    	keycodes.put(context.getString(R.string.KEYCODE_POWER), Code.KEYCODE_POWER);
    	keycodes.put(context.getString(R.string.KEYCODE_CAMERA), Code.KEYCODE_CAMERA);
    	keycodes.put(context.getString(R.string.KEYCODE_A), Code.KEYCODE_A);
    	keycodes.put(context.getString(R.string.KEYCODE_B), Code.KEYCODE_B);
    	keycodes.put(context.getString(R.string.KEYCODE_C), Code.KEYCODE_C);
    	keycodes.put(context.getString(R.string.KEYCODE_D), Code.KEYCODE_D);
    	keycodes.put(context.getString(R.string.KEYCODE_E), Code.KEYCODE_E);
    	keycodes.put(context.getString(R.string.KEYCODE_F), Code.KEYCODE_F);
    	keycodes.put(context.getString(R.string.KEYCODE_G), Code.KEYCODE_G);
    	keycodes.put(context.getString(R.string.KEYCODE_H), Code.KEYCODE_H);
    	keycodes.put(context.getString(R.string.KEYCODE_I), Code.KEYCODE_I);
    	keycodes.put(context.getString(R.string.KEYCODE_J), Code.KEYCODE_J);
    	keycodes.put(context.getString(R.string.KEYCODE_K), Code.KEYCODE_K);
    	keycodes.put(context.getString(R.string.KEYCODE_L), Code.KEYCODE_L);
    	keycodes.put(context.getString(R.string.KEYCODE_M), Code.KEYCODE_M);
    	keycodes.put(context.getString(R.string.KEYCODE_N), Code.KEYCODE_N);
    	keycodes.put(context.getString(R.string.KEYCODE_O), Code.KEYCODE_O);
    	keycodes.put(context.getString(R.string.KEYCODE_P), Code.KEYCODE_P);
    	keycodes.put(context.getString(R.string.KEYCODE_Q), Code.KEYCODE_Q);
    	keycodes.put(context.getString(R.string.KEYCODE_R), Code.KEYCODE_R);
    	keycodes.put(context.getString(R.string.KEYCODE_S), Code.KEYCODE_S);
    	keycodes.put(context.getString(R.string.KEYCODE_T), Code.KEYCODE_T);
    	keycodes.put(context.getString(R.string.KEYCODE_U), Code.KEYCODE_U);
    	keycodes.put(context.getString(R.string.KEYCODE_V), Code.KEYCODE_V);
    	keycodes.put(context.getString(R.string.KEYCODE_W), Code.KEYCODE_W);
    	keycodes.put(context.getString(R.string.KEYCODE_X), Code.KEYCODE_X);
    	keycodes.put(context.getString(R.string.KEYCODE_Y), Code.KEYCODE_Y);
    	keycodes.put(context.getString(R.string.KEYCODE_Z), Code.KEYCODE_Z);
    	keycodes.put(context.getString(R.string.KEYCODE_COMMA), Code.KEYCODE_COMMA);
    	keycodes.put(context.getString(R.string.KEYCODE_PERIOD), Code.KEYCODE_PERIOD);
    	keycodes.put(context.getString(R.string.KEYCODE_ALT_LEFT), Code.KEYCODE_ALT_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_ALT_RIGHT), Code.KEYCODE_ALT_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_SHIFT_LEFT), Code.KEYCODE_SHIFT_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_SHIFT_RIGHT), Code.KEYCODE_SHIFT_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_TAB), Code.KEYCODE_TAB);
    	keycodes.put(context.getString(R.string.KEYCODE_SPACE), Code.KEYCODE_SPACE);
    	keycodes.put(context.getString(R.string.KEYCODE_EXPLORER), Code.KEYCODE_EXPLORER);
    	keycodes.put(context.getString(R.string.KEYCODE_ENTER), Code.KEYCODE_ENTER);
    	keycodes.put(context.getString(R.string.KEYCODE_DEL), Code.KEYCODE_DEL);
    	keycodes.put(context.getString(R.string.KEYCODE_GRAVE), Code.KEYCODE_GRAVE);
    	keycodes.put(context.getString(R.string.KEYCODE_MINUS), Code.KEYCODE_MINUS);
    	keycodes.put(context.getString(R.string.KEYCODE_EQUALS), Code.KEYCODE_EQUALS);
    	keycodes.put(context.getString(R.string.KEYCODE_LEFT_BRACKET), Code.KEYCODE_LEFT_BRACKET);
    	keycodes.put(context.getString(R.string.KEYCODE_RIGHT_BRACKET), Code.KEYCODE_RIGHT_BRACKET);
    	keycodes.put(context.getString(R.string.KEYCODE_BACKSLASH), Code.KEYCODE_BACKSLASH);
    	keycodes.put(context.getString(R.string.KEYCODE_SEMICOLON), Code.KEYCODE_SEMICOLON);
    	keycodes.put(context.getString(R.string.KEYCODE_APOSTROPHE), Code.KEYCODE_APOSTROPHE);
    	keycodes.put(context.getString(R.string.KEYCODE_SLASH), Code.KEYCODE_SLASH);
    	keycodes.put(context.getString(R.string.KEYCODE_AT), Code.KEYCODE_AT);
    	keycodes.put(context.getString(R.string.KEYCODE_FOCUS), Code.KEYCODE_FOCUS);
    	keycodes.put(context.getString(R.string.KEYCODE_PLUS), Code.KEYCODE_PLUS);
    	keycodes.put(context.getString(R.string.KEYCODE_MENU), Code.KEYCODE_MENU);
    	keycodes.put(context.getString(R.string.KEYCODE_SEARCH), Code.KEYCODE_SEARCH);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_PLAY_PAUSE), Code.KEYCODE_MEDIA_PLAY_PAUSE);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_STOP), Code.KEYCODE_MEDIA_STOP);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_NEXT), Code.KEYCODE_MEDIA_NEXT);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_PREVIOUS), Code.KEYCODE_MEDIA_PREVIOUS);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_REWIND), Code.KEYCODE_MEDIA_REWIND);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_FAST_FORWARD), Code.KEYCODE_MEDIA_FAST_FORWARD);
    	keycodes.put(context.getString(R.string.KEYCODE_MUTE), Code.KEYCODE_MUTE);
    	keycodes.put(context.getString(R.string.KEYCODE_CTRL_LEFT), Code.KEYCODE_CTRL_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_CTRL_RIGHT), Code.KEYCODE_CTRL_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_INSERT), Code.KEYCODE_INSERT);
    	keycodes.put(context.getString(R.string.KEYCODE_PAUSE), Code.KEYCODE_PAUSE);
    	keycodes.put(context.getString(R.string.KEYCODE_PAGE_UP), Code.KEYCODE_PAGE_UP);
    	keycodes.put(context.getString(R.string.KEYCODE_PAGE_DOWN), Code.KEYCODE_PAGE_DOWN);
    	keycodes.put(context.getString(R.string.KEYCODE_PRINT_SCREEN), Code.KEYCODE_PRINT_SCREEN);
    	keycodes.put(context.getString(R.string.KEYCODE_INFO), Code.KEYCODE_INFO);
    	keycodes.put(context.getString(R.string.KEYCODE_WINDOW), Code.KEYCODE_WINDOW);
    	keycodes.put(context.getString(R.string.KEYCODE_BOOKMARK), Code.KEYCODE_BOOKMARK);
    	keycodes.put(context.getString(R.string.KEYCODE_CAPS_LOCK), Code.KEYCODE_CAPS_LOCK);
    	keycodes.put(context.getString(R.string.KEYCODE_ESCAPE), Code.KEYCODE_ESCAPE);
    	keycodes.put(context.getString(R.string.KEYCODE_META_LEFT), Code.KEYCODE_META_LEFT);
    	keycodes.put(context.getString(R.string.KEYCODE_META_RIGHT), Code.KEYCODE_META_RIGHT);
    	keycodes.put(context.getString(R.string.KEYCODE_ZOOM_IN), Code.KEYCODE_ZOOM_IN);
    	keycodes.put(context.getString(R.string.KEYCODE_ZOOM_OUT), Code.KEYCODE_ZOOM_OUT);
    	keycodes.put(context.getString(R.string.KEYCODE_CHANNEL_UP), Code.KEYCODE_CHANNEL_UP);
    	keycodes.put(context.getString(R.string.KEYCODE_CHANNEL_DOWN), Code.KEYCODE_CHANNEL_DOWN);
    	keycodes.put(context.getString(R.string.KEYCODE_LIVE), Code.KEYCODE_LIVE);
    	keycodes.put(context.getString(R.string.KEYCODE_DVR), Code.KEYCODE_DVR);
    	keycodes.put(context.getString(R.string.KEYCODE_GUIDE), Code.KEYCODE_GUIDE);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_SKIP_BACK), Code.KEYCODE_MEDIA_SKIP_BACK);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_SKIP_FORWARD), Code.KEYCODE_MEDIA_SKIP_FORWARD);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_RECORD), Code.KEYCODE_MEDIA_RECORD);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_PLAY), Code.KEYCODE_MEDIA_PLAY);
    	keycodes.put(context.getString(R.string.KEYCODE_PROG_RED), Code.KEYCODE_PROG_RED);
    	keycodes.put(context.getString(R.string.KEYCODE_PROG_GREEN), Code.KEYCODE_PROG_GREEN);
    	keycodes.put(context.getString(R.string.KEYCODE_PROG_YELLOW), Code.KEYCODE_PROG_YELLOW);
    	keycodes.put(context.getString(R.string.KEYCODE_PROG_BLUE), Code.KEYCODE_PROG_BLUE);
    	keycodes.put(context.getString(R.string.KEYCODE_BD_POWER), Code.KEYCODE_BD_POWER);
    	keycodes.put(context.getString(R.string.KEYCODE_BD_INPUT), Code.KEYCODE_BD_INPUT);
    	keycodes.put(context.getString(R.string.KEYCODE_STB_POWER), Code.KEYCODE_STB_POWER);
    	keycodes.put(context.getString(R.string.KEYCODE_STB_INPUT), Code.KEYCODE_STB_INPUT);
    	keycodes.put(context.getString(R.string.KEYCODE_STB_MENU), Code.KEYCODE_STB_MENU);
    	keycodes.put(context.getString(R.string.KEYCODE_TV_POWER), Code.KEYCODE_TV_POWER);
    	keycodes.put(context.getString(R.string.KEYCODE_TV_INPUT), Code.KEYCODE_TV_INPUT);
    	keycodes.put(context.getString(R.string.KEYCODE_AVR_POWER), Code.KEYCODE_AVR_POWER);
    	keycodes.put(context.getString(R.string.KEYCODE_AVR_INPUT), Code.KEYCODE_AVR_INPUT);
    	keycodes.put(context.getString(R.string.KEYCODE_AUDIO), Code.KEYCODE_AUDIO);
    	keycodes.put(context.getString(R.string.KEYCODE_EJECT), Code.KEYCODE_EJECT);
    	keycodes.put(context.getString(R.string.KEYCODE_BD_POPUP_MENU), Code.KEYCODE_BD_POPUP_MENU);
    	keycodes.put(context.getString(R.string.KEYCODE_BD_TOP_MENU), Code.KEYCODE_BD_TOP_MENU);
    	keycodes.put(context.getString(R.string.KEYCODE_SETTINGS), Code.KEYCODE_SETTINGS);
    	keycodes.put(context.getString(R.string.KEYCODE_SETUP), Code.KEYCODE_SETUP);
    	keycodes.put(context.getString(R.string.KEYCODE_MICROPHONE_MUTE), Code.KEYCODE_MICROPHONE_MUTE);
    	keycodes.put(context.getString(R.string.KEYCODE_PICTSYMBOLS), Code.KEYCODE_PICTSYMBOLS);
    	keycodes.put(context.getString(R.string.KEYCODE_SWITCH_CHARSET), Code.KEYCODE_SWITCH_CHARSET);
    	keycodes.put(context.getString(R.string.KEYCODE_FORWARD_DEL), Code.KEYCODE_FORWARD_DEL);
    	keycodes.put(context.getString(R.string.KEYCODE_SCROLL_LOCK), Code.KEYCODE_SCROLL_LOCK);
    	keycodes.put(context.getString(R.string.KEYCODE_FUNCTION), Code.KEYCODE_FUNCTION);
    	keycodes.put(context.getString(R.string.KEYCODE_SYSRQ), Code.KEYCODE_SYSRQ);
    	keycodes.put(context.getString(R.string.KEYCODE_BREAK), Code.KEYCODE_BREAK);
    	keycodes.put(context.getString(R.string.KEYCODE_MOVE_HOME), Code.KEYCODE_MOVE_HOME);
    	keycodes.put(context.getString(R.string.KEYCODE_MOVE_END), Code.KEYCODE_MOVE_END);
    	keycodes.put(context.getString(R.string.KEYCODE_FORWARD), Code.KEYCODE_FORWARD);
    	keycodes.put(context.getString(R.string.KEYCODE_MEDIA_CLOSE), Code.KEYCODE_MEDIA_CLOSE);
    	keycodes.put(context.getString(R.string.KEYCODE_F1), Code.KEYCODE_F1);
    	keycodes.put(context.getString(R.string.KEYCODE_F2), Code.KEYCODE_F2);
    	keycodes.put(context.getString(R.string.KEYCODE_F3), Code.KEYCODE_F3);
    	keycodes.put(context.getString(R.string.KEYCODE_F4), Code.KEYCODE_F4);
    	keycodes.put(context.getString(R.string.KEYCODE_F5), Code.KEYCODE_F5);
    	keycodes.put(context.getString(R.string.KEYCODE_F6), Code.KEYCODE_F6);
    	keycodes.put(context.getString(R.string.KEYCODE_F7), Code.KEYCODE_F7);
    	keycodes.put(context.getString(R.string.KEYCODE_F8), Code.KEYCODE_F8);
    	keycodes.put(context.getString(R.string.KEYCODE_F9), Code.KEYCODE_F9);
    	keycodes.put(context.getString(R.string.KEYCODE_F10), Code.KEYCODE_F10);
    	keycodes.put(context.getString(R.string.KEYCODE_F11), Code.KEYCODE_F11);
    	keycodes.put(context.getString(R.string.KEYCODE_F12), Code.KEYCODE_F12);
    	keycodes.put(context.getString(R.string.KEYCODE_NUM_LOCK), Code.KEYCODE_NUM_LOCK);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_0), Code.KEYCODE_NUMPAD_0);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_1), Code.KEYCODE_NUMPAD_1);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_2), Code.KEYCODE_NUMPAD_2);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_3), Code.KEYCODE_NUMPAD_3);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_4), Code.KEYCODE_NUMPAD_4);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_5), Code.KEYCODE_NUMPAD_5);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_6), Code.KEYCODE_NUMPAD_6);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_7), Code.KEYCODE_NUMPAD_7);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_8), Code.KEYCODE_NUMPAD_8);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_9), Code.KEYCODE_NUMPAD_9);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_DIVIDE), Code.KEYCODE_NUMPAD_DIVIDE);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_MULTIPLY), Code.KEYCODE_NUMPAD_MULTIPLY);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_SUBTRACT), Code.KEYCODE_NUMPAD_SUBTRACT);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_ADD), Code.KEYCODE_NUMPAD_ADD);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_DOT), Code.KEYCODE_NUMPAD_DOT);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_COMMA), Code.KEYCODE_NUMPAD_COMMA);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_ENTER), Code.KEYCODE_NUMPAD_ENTER);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_EQUALS), Code.KEYCODE_NUMPAD_EQUALS);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_LEFT_PAREN), Code.KEYCODE_NUMPAD_LEFT_PAREN);
    	keycodes.put(context.getString(R.string.KEYCODE_NUMPAD_RIGHT_PAREN), Code.KEYCODE_NUMPAD_RIGHT_PAREN);
    	keycodes.put(context.getString(R.string.KEYCODE_APP_SWITCH), Code.KEYCODE_APP_SWITCH);
    	keycodes.put(context.getString(R.string.BTN_FIRST), Code.BTN_FIRST);
    	keycodes.put(context.getString(R.string.BTN_MISC), Code.BTN_MISC);
    	keycodes.put(context.getString(R.string.BTN_0), Code.BTN_0);
    	keycodes.put(context.getString(R.string.BTN_1), Code.BTN_1);
    	keycodes.put(context.getString(R.string.BTN_2), Code.BTN_2);
    	keycodes.put(context.getString(R.string.BTN_3), Code.BTN_3);
    	keycodes.put(context.getString(R.string.BTN_4), Code.BTN_4);
    	keycodes.put(context.getString(R.string.BTN_5), Code.BTN_5);
    	keycodes.put(context.getString(R.string.BTN_6), Code.BTN_6);
    	keycodes.put(context.getString(R.string.BTN_7), Code.BTN_7);
    	keycodes.put(context.getString(R.string.BTN_8), Code.BTN_8);
    	keycodes.put(context.getString(R.string.BTN_9), Code.BTN_9);
    	keycodes.put(context.getString(R.string.BTN_MOUSE), Code.BTN_MOUSE);
    	keycodes.put(context.getString(R.string.BTN_LEFT), Code.BTN_LEFT);
    	keycodes.put(context.getString(R.string.BTN_RIGHT), Code.BTN_RIGHT);
    	keycodes.put(context.getString(R.string.BTN_MIDDLE), Code.BTN_MIDDLE);
    	keycodes.put(context.getString(R.string.BTN_SIDE), Code.BTN_SIDE);
    	keycodes.put(context.getString(R.string.BTN_EXTRA), Code.BTN_EXTRA);
    	keycodes.put(context.getString(R.string.BTN_FORWARD), Code.BTN_FORWARD);
    	keycodes.put(context.getString(R.string.BTN_BACK), Code.BTN_BACK);
    	keycodes.put(context.getString(R.string.BTN_TASK), Code.BTN_TASK);
    	keycodes.put(context.getString(R.string.BTN_GAME_A), Code.BTN_GAME_A);
    	keycodes.put(context.getString(R.string.BTN_GAME_B), Code.BTN_GAME_B);
    	keycodes.put(context.getString(R.string.BTN_GAME_C), Code.BTN_GAME_C);
    	keycodes.put(context.getString(R.string.BTN_GAME_X), Code.BTN_GAME_X);
    	keycodes.put(context.getString(R.string.BTN_GAME_Y), Code.BTN_GAME_Y);
    	keycodes.put(context.getString(R.string.BTN_GAME_Z), Code.BTN_GAME_Z);
    	keycodes.put(context.getString(R.string.BTN_GAME_L1), Code.BTN_GAME_L1);
    	keycodes.put(context.getString(R.string.BTN_GAME_R1), Code.BTN_GAME_R1);
    	keycodes.put(context.getString(R.string.BTN_GAME_L2), Code.BTN_GAME_L2);
    	keycodes.put(context.getString(R.string.BTN_GAME_R2), Code.BTN_GAME_R2);
    	keycodes.put(context.getString(R.string.BTN_GAME_THUMBL), Code.BTN_GAME_THUMBL);
    	keycodes.put(context.getString(R.string.BTN_GAME_THUMBR), Code.BTN_GAME_THUMBR);
    	keycodes.put(context.getString(R.string.BTN_GAME_START), Code.BTN_GAME_START);
    	keycodes.put(context.getString(R.string.BTN_GAME_SELECT), Code.BTN_GAME_SELECT);
    	keycodes.put(context.getString(R.string.BTN_GAME_MODE), Code.BTN_GAME_MODE);
    	keycodes.put(context.getString(R.string.BTN_GAME_1), Code.BTN_GAME_1);
    	keycodes.put(context.getString(R.string.BTN_GAME_2), Code.BTN_GAME_2);
    	keycodes.put(context.getString(R.string.BTN_GAME_3), Code.BTN_GAME_3);
    	keycodes.put(context.getString(R.string.BTN_GAME_4), Code.BTN_GAME_4);
    	keycodes.put(context.getString(R.string.BTN_GAME_5), Code.BTN_GAME_5);
    	keycodes.put(context.getString(R.string.BTN_GAME_6), Code.BTN_GAME_6);
    	keycodes.put(context.getString(R.string.BTN_GAME_7), Code.BTN_GAME_7);
    	keycodes.put(context.getString(R.string.BTN_GAME_8), Code.BTN_GAME_8);
    	keycodes.put(context.getString(R.string.BTN_GAME_9), Code.BTN_GAME_9);
    	keycodes.put(context.getString(R.string.BTN_GAME_10), Code.BTN_GAME_10);
    	keycodes.put(context.getString(R.string.BTN_GAME_11), Code.BTN_GAME_11);
    	keycodes.put(context.getString(R.string.BTN_GAME_12), Code.BTN_GAME_12);
    	keycodes.put(context.getString(R.string.BTN_GAME_13), Code.BTN_GAME_13);
    	keycodes.put(context.getString(R.string.BTN_GAME_14), Code.BTN_GAME_14);
    	keycodes.put(context.getString(R.string.BTN_GAME_15), Code.BTN_GAME_15);
    	keycodes.put(context.getString(R.string.BTN_GAME_16), Code.BTN_GAME_16);
    	return keycodes;
    }
}