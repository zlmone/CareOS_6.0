/*
 * Copyright (C) 2007 The Android Open Source Project
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

package android.widget;

import android.annotation.Nullable;
import android.annotation.Widget;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.internal.R;

import java.util.Locale;

/**
 * A view for selecting the time of day, in either 24 hour or AM/PM mode. The
 * hour, each minute digit, and AM/PM (if applicable) can be conrolled by
 * vertical spinners. The hour can be entered by keyboard input. Entering in two
 * digit hours can be accomplished by hitting two digits within a timeout of
 * about a second (e.g. '1' then '2' to select 12). The minutes can be entered
 * by entering single digits. Under AM/PM mode, the user can hit 'a', 'A", 'p'
 * or 'P' to pick. For a dialog using this view, see
 * {@link android.app.CareTimePickerDialog}.
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
@Widget
public class CareTimePicker extends FrameLayout {
    private static final int MODE_SPINNER = 1;
    private static final int MODE_CLOCK = 2;

    private final TimePickerDelegate mDelegate;

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute The current minute.
         */
        void onTimeChanged(CareTimePicker view, int hourOfDay, int minute);
    }

    public CareTimePicker(Context context) {
        this(context, null);
    }

    public CareTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.careTimePickerStyle);
    }

    public CareTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CareTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CareTimePicker, defStyleAttr, defStyleRes);
        final int mode = a.getInt(R.styleable.CareTimePicker_timePickerMode, MODE_SPINNER);
        a.recycle();

        switch (mode) {
            case MODE_CLOCK:
                mDelegate = new CareTimePickerClockDelegate(
                        this, context, attrs, defStyleAttr, defStyleRes);
                break;
            case MODE_SPINNER:
            default:
                mDelegate = new CareTimePickerSpinnerDelegate(
                        this, context, attrs, defStyleAttr, defStyleRes);
                break;
        }
    }

    /**
     * Set the current hour.
     */
    public void setCurrentHour(Integer currentHour) {
        mDelegate.setCurrentHour(currentHour);
    }

    /**
     * @return The current hour in the range (0-23).
     */
    public Integer getCurrentHour() {
        return mDelegate.getCurrentHour();
    }

    /**
     * Set the current minute (0-59).
     */
    public void setCurrentMinute(Integer currentMinute) {
        mDelegate.setCurrentMinute(currentMinute);
    }

    /**
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
        return mDelegate.getCurrentMinute();
    }

    /**
     * Set whether in 24 hour or AM/PM mode.
     *
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    public void setIs24HourView(Boolean is24HourView) {
        mDelegate.setIs24HourView(is24HourView);
    }

    /**
     * @return true if this is in 24 hour view else false.
     */
    public boolean is24HourView() {
        return mDelegate.is24HourView();
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     *
     * @param onTimeChangedListener the callback, should not be null.
     */
    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    /**
     * Sets the callback that indicates the current time is valid.
     *
     * @param callback the callback, may be null
     * @hide
     */
    public void setValidationCallback(@Nullable ValidationCallback callback) {
        mDelegate.setValidationCallback(callback);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDelegate.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return mDelegate.isEnabled();
    }

    @Override
    public int getBaseline() {
        return mDelegate.getBaseline();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.onConfigurationChanged(newConfig);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return mDelegate.onSaveInstanceState(superState);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BaseSavedState ss = (BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mDelegate.onRestoreInstanceState(ss);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        mDelegate.onPopulateAccessibilityEvent(event);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        mDelegate.onInitializeAccessibilityEvent(event);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        mDelegate.onInitializeAccessibilityNodeInfo(info);
    }

    /**
     * A delegate interface that defined the public API of the CareTimePicker. Allows different
     * CareTimePicker implementations. This would need to be implemented by the CareTimePicker delegates
     * for the real behavior.
     */
    interface TimePickerDelegate {
        void setCurrentHour(Integer currentHour);
        Integer getCurrentHour();

        void setCurrentMinute(Integer currentMinute);
        Integer getCurrentMinute();

        void setIs24HourView(Boolean is24HourView);
        boolean is24HourView();

        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);
        void setValidationCallback(ValidationCallback callback);

        void setEnabled(boolean enabled);
        boolean isEnabled();

        int getBaseline();

        void onConfigurationChanged(Configuration newConfig);

        Parcelable onSaveInstanceState(Parcelable superState);
        void onRestoreInstanceState(Parcelable state);

        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event);
        void onPopulateAccessibilityEvent(AccessibilityEvent event);
        void onInitializeAccessibilityEvent(AccessibilityEvent event);
        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info);
    }

    /**
     * A callback interface for updating input validity when the CareTimePicker
     * when included into a Dialog.
     *
     * @hide
     */
    public static interface ValidationCallback {
        void onValidationChanged(boolean valid);
    }

    /**
     * An abstract class which can be used as a start for CareTimePicker implementations
     */
    abstract static class AbstractTimePickerDelegate implements TimePickerDelegate {
        // The delegator
        protected CareTimePicker mDelegator;

        // The context
        protected Context mContext;

        // The current locale
        protected Locale mCurrentLocale;

        // Callbacks
        protected OnTimeChangedListener mOnTimeChangedListener;
        protected ValidationCallback mValidationCallback;

        public AbstractTimePickerDelegate(CareTimePicker delegator, Context context) {
            mDelegator = delegator;
            mContext = context;

            // initialization based on locale
            setCurrentLocale(Locale.getDefault());
        }

        public void setCurrentLocale(Locale locale) {
            if (locale.equals(mCurrentLocale)) {
                return;
            }
            mCurrentLocale = locale;
        }

        @Override
        public void setValidationCallback(ValidationCallback callback) {
            mValidationCallback = callback;
        }

        protected void onValidationChanged(boolean valid) {
            if (mValidationCallback != null) {
                mValidationCallback.onValidationChanged(valid);
            }
        }
    }
}