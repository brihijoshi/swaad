package com.tengo.camerayeetsfirst;

import android.view.View;

public class AnimationUtils {

    private static final int FADE_DURATION = 500;

    public static void fadeInToVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setAlpha(0.0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(0.8f).setDuration(FADE_DURATION);
    }

    public static void fadeOutToGone(View view) {
        if (view.getVisibility() == View.GONE) {
            return;
        }
        view.setAlpha(0.8f);
        view.animate().alpha(0.0f).setDuration(FADE_DURATION);
        view.setVisibility(View.GONE);
    }
}
