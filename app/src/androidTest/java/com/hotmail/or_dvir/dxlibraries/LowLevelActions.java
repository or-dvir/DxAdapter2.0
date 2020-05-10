package com.hotmail.or_dvir.dxlibraries;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.MotionEvents;
import androidx.test.espresso.action.Press;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

public class LowLevelActions {
    private static MotionEvent sMotionEventDownHeldView = null;

    public static PressAndHoldAction pressAndHold() {
        return new PressAndHoldAction();
    }

    public static ReleaseAction release() {
        return new ReleaseAction();
    }

    public static void tearDown() {
        sMotionEventDownHeldView = null;
    }

    static class PressAndHoldAction implements ViewAction {
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
            float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
            sMotionEventDownHeldView = MotionEvents.sendDown(uiController, coords, precision).down;
            //allow processing of the action
            uiController.loopMainThreadForAtLeast(ViewConfiguration.getLongPressTimeout() + 200);
        }
    }

    static class ReleaseAction implements ViewAction {
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

            float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
            MotionEvents.sendUp(uiController, sMotionEventDownHeldView, coords);
        }
    }
}
