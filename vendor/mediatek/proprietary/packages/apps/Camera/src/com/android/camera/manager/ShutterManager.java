/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2014. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */
package com.android.camera.manager;

import android.view.View;
import android.view.View.OnClickListener;

import com.android.camera.CameraActivity;
import com.android.camera.Log;
import com.android.camera.ModeChecker;
import com.android.camera.R;
import com.android.camera.ui.ShutterButton;
import com.android.camera.ui.ShutterButton.OnShutterButtonListener;

import com.mediatek.camera.ISettingCtrl;
import com.mediatek.camera.setting.SettingConstants;

public class ShutterManager extends ViewManager {
    private static final String TAG = "ShutterManager";

    public static final int SHUTTER_TYPE_PHOTO_VIDEO = 0;
    public static final int SHUTTER_TYPE_PHOTO = 1;
    public static final int SHUTTER_TYPE_VIDEO = 2;
    public static final int SHUTTER_TYPE_OK_CANCEL = 3;
    public static final int SHUTTER_TYPE_CANCEL = 4;
    public static final int SHUTTER_TYPE_CANCEL_VIDEO = 5;
    public static final int SHUTTER_TYPE_SLOW_VIDEO = 6;

    private int mShutterType = SHUTTER_TYPE_PHOTO_VIDEO;
    private ShutterButton mPhotoShutter;
    private ShutterButton mPhotoShutter1;
    private ShutterButton mVideoShutter;
    private View mOkButton;
    private View mCancelButton;
    private OnShutterButtonListener mPhotoListener;
    private OnShutterButtonListener mVideoListener;
    private OnClickListener mOklistener;
    private OnClickListener mCancelListener;
    private boolean mPhotoShutterEnabled = true;
    private boolean mVideoShutterEnabled = true;
    private boolean mCancelButtonEnabled = true;
    private boolean mOkButtonEnabled = true;
    private boolean mVideoShutterMasked;
    private boolean mFullScreen = true;
    private ISettingCtrl mISettingController;
    // dengjianzhang@20150728 add start
    private ShutterButton mInvisibleShutter;
    private ShutterButton mVideoStateShutter;
    private ShutterButton mVideoStateShutter1;

    // dengjianzhang@20150728 add end
    
    public ShutterManager(CameraActivity context) {
        super(context, VIEW_LAYER_SHUTTER);
        setFileter(false);
    }

    @Override
    protected View getView() {
        View view = null;
        int layoutId = R.layout.camera_shutter_photo_video;
        switch (mShutterType) {
        case SHUTTER_TYPE_PHOTO_VIDEO:
            // dengjianzhang@20150728 add start
            /**
             * layoutId = R.layout.camera_shutter_photo_video;
             */
            layoutId = R.layout.magcomm_camera_shutter_photo_video;
            // dengjianzhang@20150728 add end
            break;
        case SHUTTER_TYPE_PHOTO:
            // dengjianzhang@20150728 add start
            /**
             * layoutId = R.layout.camera_shutter_photo;
             */
            layoutId = R.layout.magcomm_camera_shutter_photo;
            // dengjianzhang@20150728 add end
            break;
        case SHUTTER_TYPE_VIDEO:
            layoutId = R.layout.camera_shutter_video;
            break;
        case SHUTTER_TYPE_OK_CANCEL:
            //dengjianzhang@20150728 add start
            /**
             * layoutId = R.layout.camera_shutter_ok_cancel;
             */
            layoutId = R.layout.magcomm_camera_shutter_ok_cancel;
            // dengjianzhang@20150728 add end
            break;
        case SHUTTER_TYPE_CANCEL:
            layoutId = R.layout.camera_shutter_cancel;
            break;
        case SHUTTER_TYPE_CANCEL_VIDEO:
            layoutId = R.layout.camera_shutter_cancel_video;
            break;
        case SHUTTER_TYPE_SLOW_VIDEO:
            layoutId = R.layout.camera_shutter_slow_video;
        default:
            break;
        }
        view = inflate(layoutId);
        mPhotoShutter = (ShutterButton) view.findViewById(R.id.shutter_button_photo);
        if (mShutterType == SHUTTER_TYPE_SLOW_VIDEO) {
            mVideoShutter = (ShutterButton) view.findViewById(R.id.shutter_button_slow_video);
        } else {
            mVideoShutter = (ShutterButton) view.findViewById(R.id.shutter_button_video);
        }
        mOkButton = view.findViewById(R.id.btn_done);
        mCancelButton = view.findViewById(R.id.btn_cancel);
        
        // dengjianzhang@20150728 add start
        mInvisibleShutter = (ShutterButton) view.findViewById(R.id.shutter_invisible);
        mVideoStateShutter = (ShutterButton) view.findViewById(R.id.shutter_button_video_state);
        mVideoStateShutter1 = (ShutterButton) view.findViewById(R.id.shutter_button_video1);
        mPhotoShutter1 = (ShutterButton) view.findViewById(R.id.shutter_button_photo1);
        
         // dengjianzhang@20150728 add end
        
        applyListener();
        return view;
    }

    @Override
    protected void onRelease() {
        if (mPhotoShutter != null) {
            mPhotoShutter.setOnShutterButtonListener(null);
        }
        if (mVideoShutter != null) {
            mVideoShutter.setOnShutterButtonListener(null);
        }


        //dengjianzhang   20160618
        if(mVideoStateShutter1 !=null){
        	mVideoStateShutter1.setOnShutterButtonListener(null);
        }
        
       //dengjianzhang  20160618
        if (mOkButton != null) {
            mOkButton.setOnClickListener(null);
        }
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(null);
        }
        mPhotoShutter = null;
        mVideoShutter = null;
        mOkButton = null;
        mCancelButton = null;
    }

    public void setSettingController(ISettingCtrl settingController) {
        mISettingController = settingController;
    }

    private void applyListener() {
        if (mPhotoShutter != null) {
            mPhotoShutter.setOnShutterButtonListener(mPhotoListener);
        }
        if (mVideoShutter != null) {
            mVideoShutter.setOnShutterButtonListener(mVideoListener);
        }
       // dengjianzhang  20160618
        if (mPhotoShutter1 != null) {
            mPhotoShutter1.setOnShutterButtonListener(mPhotoListener);
        }
        if(mVideoStateShutter1 !=null){
        	mVideoStateShutter1.setOnShutterButtonListener(mVideoListener);
        }
     // dengjianzhang 20160618
        if (mOkButton != null) {
            mOkButton.setOnClickListener(mOklistener);
        }
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(mCancelListener);
        }
        Log.d(TAG, "applyListener() mPhotoShutter=(" + mPhotoShutter + ", " + mPhotoListener
                + "), mVideoShutter=(" + mVideoShutter + ", " + mVideoListener + "), mOkButton=("
                + mOkButton + ", " + mOklistener + "), mCancelButton=(" + mCancelButton + ", "
                + mCancelListener + ")");
    }

    public void setShutterListener(OnShutterButtonListener photoListener,
            OnShutterButtonListener videoListener, OnClickListener okListener,
            OnClickListener cancelListener) {
        mPhotoListener = photoListener;
        mVideoListener = videoListener;
        mOklistener = okListener;
        mCancelListener = cancelListener;
        applyListener();
    }

    public void switchShutter(int type) {
        Log.i(TAG, "switchShutterType(" + type + ") mShutterType=" + mShutterType);
        if (mShutterType != type) {
            mShutterType = type;
            reInflate();
        }
    }

    public int getShutterType() {
        return mShutterType;
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh() mPhotoShutterEnabled=" + mPhotoShutterEnabled + ", mFullScreen="
                + mFullScreen + ", isEnabled()=" + isEnabled());
        if (mVideoShutter != null) {
            boolean visible = ModeChecker.getModePickerVisible(getContext(), getContext()
                    .getCameraId(), ModePicker.MODE_VIDEO);
            boolean enabled = mVideoShutterEnabled && isEnabled() && mFullScreen && visible;
                    //&& !getContext().getWfdManagerLocal().isWfdEnabled();
            mVideoShutter.setEnabled(enabled);
            mVideoShutter.setClickable(enabled);
            boolean isSlowMotionOn = false;
            if (mISettingController != null) {
                isSlowMotionOn = "on".equals(mISettingController
                        .getSettingValue(SettingConstants.KEY_SLOW_MOTION));
            }
            
            // dengjianzhang@20150728 add start
			/**
            if (mVideoShutterMasked) {
                if (isSlowMotionOn) {
                    mVideoShutter.setImageResource(R.drawable.btn_slow_video_mask);
                } else {
                    mVideoShutter.setImageResource(R.drawable.btn_video_mask);
                }
            } else {
                if (isSlowMotionOn) {
                    mVideoShutter.setImageResource(R.drawable.btn_slow_video);
                } else {
                    mVideoShutter.setImageResource(R.drawable.btn_video);
                }
            }
			**/
            updateShutter(mVideoShutterMasked);
            // dengjianzhang@20150728 add end
        }
        if (mPhotoShutter != null) {
            boolean enabled = mPhotoShutterEnabled && isEnabled() && mFullScreen;
            mPhotoShutter.setEnabled(enabled);
            mPhotoShutter.setClickable(enabled);
        }
        
        if (mPhotoShutter1 != null) {//add 0620 fixed video shutter error
            boolean enabled = mPhotoShutterEnabled && isEnabled() && mFullScreen;
            mPhotoShutter1.setEnabled(enabled);
            mPhotoShutter1.setClickable(enabled);
        }
        
        if (mOkButton != null) {
            boolean enabled = mOkButtonEnabled && isEnabled() && mFullScreen;
            mOkButton.setEnabled(enabled);
            mOkButton.setClickable(enabled);
        }
        if (mCancelButton != null) {
            boolean enabled = mCancelButtonEnabled && isEnabled() && mFullScreen;
            mCancelButton.setEnabled(enabled);
            mCancelButton.setClickable(enabled);
        }
    }

    public ShutterButton getPhotoShutter() {
        return mPhotoShutter;
    }

    public ShutterButton getVideoShutter() {
        return mVideoShutter;
    }

    public View getOkShutter() {
        return mOkButton;
    }

    public boolean performPhotoShutter() {
        boolean performed = false;
		if(mVideoShutterMasked)//add 0620 fixed video shutter error
		{
			if (mPhotoShutter1 != null && mPhotoShutter1.isEnabled()) {
			    mPhotoShutter1.performClick();
			    performed = true;
			}
		}else{
			if (mPhotoShutter != null && mPhotoShutter.isEnabled()) {
			    mPhotoShutter.performClick();
			    performed = true;
			}
		}
        
        Log.d(TAG, "performPhotoShutter() mPhotoShutter=" + mPhotoShutter + ", return "
                + mPhotoShutter);
        return performed;
    }

    public void setPhotoShutterEnabled(boolean enabled) {
        Log.d(TAG, "setPhotoShutterEnabled(" + enabled + ")");
        mPhotoShutterEnabled = enabled;
        refresh();
    }

    public boolean isPhotoShutterEnabled() {
        Log.v(TAG, "isPhotoShutterEnabled() return " + mPhotoShutterEnabled);
        return mPhotoShutterEnabled;
    }

    public void setVideoShutterEnabled(boolean enabled) {
        Log.d(TAG, "setVideoShutterEnabled(" + enabled + ")");
        mVideoShutterEnabled = enabled;
        refresh();
    }

    public boolean isVideoShutterEnabled() {
        Log.d(TAG, "isVideoShutterEnabled() return " + mVideoShutterEnabled);
        return mVideoShutterEnabled;
    }

    public void setCancelButtonEnabled(boolean enabled) {
        Log.d(TAG, "setCancelButtonEnabled(" + enabled + ")");
        mCancelButtonEnabled = enabled;
        refresh();
    }

    public void setOkButtonEnabled(boolean enabled) {
        Log.d(TAG, "setOkButtonEnabled(" + enabled + ")");
        mOkButtonEnabled = enabled;
        refresh();
    }

    public boolean isCancelButtonEnabled() {
        Log.d(TAG, "isCancelButtonEnabled() return " + mCancelButtonEnabled);
        return mCancelButtonEnabled;
    }

    public void setVideoShutterMask(boolean mask) {
        Log.d(TAG, "setVideoShutterMask(" + mask + ")");
        mVideoShutterMasked = mask;
        refresh();
        // dengjianzhang@20150728 add start
        updateShutter(mVideoShutterMasked);
        // dengjianzhang@20150728 add end
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        refresh();
    }
    
    // dengjianzhang@20150728 add start
    public void updateShutter(boolean isVideo){
        if(isVideo){
			setVisibility(mPhotoShutter, View.GONE);
            setVisibility(mInvisibleShutter, View.GONE);
            setVisibility(mVideoStateShutter, View.INVISIBLE);
            setVisibility(mVideoShutter, View.GONE);
			setVisibility(mPhotoShutter1, View.VISIBLE); 
          //zhouhua begin 20160618
            setVisibility(mVideoStateShutter1, View.VISIBLE); 
			if (mPhotoShutter1 != null){
            mPhotoShutter1.setImageResource(R.drawable.magcomm_camer_shots);
			}
			if (mVideoShutter != null){
			mVideoStateShutter1.setImageResource(R.drawable.magcomm_video_save);
			}
         //zhouhua end 20160618
        }else{			
			setVisibility(mPhotoShutter1, View.GONE); // zhouhua 20160618
            setVisibility(mInvisibleShutter, View.GONE);
            setVisibility(mVideoStateShutter, View.GONE);
            setVisibility(mVideoStateShutter1, View.GONE); //zhouhua 20160618
			setVisibility(mPhotoShutter, View.VISIBLE);
            setVisibility(mVideoShutter, View.VISIBLE);
			if (mPhotoShutter != null){
            mPhotoShutter.setImageResource(R.drawable.btn_photo);
			}
			if (mVideoShutter != null){
            mVideoShutter.setImageResource(R.drawable.btn_video);
			}
        }
    }
    
    private void setVisibility(View view, int visiblility){
        if(view != null){
            view.setVisibility(visiblility);
        }
    }
    
    public void setVideoStateListener(OnClickListener listener){
        Log.i("nian", "mVideoStateShutter = " + mVideoStateShutter);
        if(mVideoStateShutter != null){
            mVideoStateShutter.setOnClickListener(listener);
        }
    }
    
    public void updateVideoState(boolean recording){
        if(mVideoStateShutter == null){
            Log.i(TAG,"updateVideoState, but mVideoStateShutter = null");
            return;
        }
//        if(recording){
//            mVideoStateShutter.setImageResource(R.drawable.magcomm_video_pause);
//        }else{
//            mVideoStateShutter.setImageResource(R.drawable.magcomm_video_going);
//        }
        
    }
    
    // dengjianzhang@20150728 add end
}
