package com.hotmail.or_dvir.dxlibraries;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.MotionEvents;
import androidx.test.espresso.action.Press;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

public class PressActions {
    private static MotionEvent sMotionEventDownHeldView = null;

    public static ViewAction pressAndHold(@Nullable @IdRes final Integer innerViewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayingAtLeast(90); // Like GeneralClickAction
            }

            @Override
            public String getDescription() {
                return "Press and hold action";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                if (sMotionEventDownHeldView != null) {
                    throw new AssertionError("Only one view can be held at a time");
                }

                float[] precision = Press.FINGER.describePrecision();
                float[] coords;

                if (innerViewId == null) {
                    coords = GeneralLocation.CENTER.calculateCoordinates(view);
                } else {
                    coords = GeneralLocation.CENTER.calculateCoordinates(view.findViewById(innerViewId));
                }

                sMotionEventDownHeldView = MotionEvents.sendDown(uiController, coords, precision).down;
                //allow processing of the action
                uiController.loopMainThreadForAtLeast(ViewConfiguration.getLongPressTimeout() + 200);
            }
        };
    }

    public static ViewAction release(@Nullable @IdRes final Integer innerViewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayingAtLeast(90);  // Like GeneralClickAction
            }

            @Override
            public String getDescription() {
                return "Release action";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                if (sMotionEventDownHeldView == null) {
                    throw new AssertionError("Before calling release(), you must call pressAndHold() on a view");
                }

                float[] coords;
                if (innerViewId == null) {
                    coords = GeneralLocation.CENTER.calculateCoordinates(view);
                } else {
                    coords = GeneralLocation.CENTER.calculateCoordinates(view.findViewById(innerViewId));
                }

                MotionEvents.sendUp(uiController, sMotionEventDownHeldView, coords);
            }
        };
    }

    public static void tearDown() {
        sMotionEventDownHeldView = null;
    }
}
