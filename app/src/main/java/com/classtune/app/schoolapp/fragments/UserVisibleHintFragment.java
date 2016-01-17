package com.classtune.app.schoolapp.fragments;

import android.support.v4.app.Fragment;

public abstract class UserVisibleHintFragment extends Fragment {

    private boolean mResumed;

    @Override
    public final void setUserVisibleHint(final boolean isVisibleToUser) {
        final boolean needUpdate = mResumed && isVisibleToUser != getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (needUpdate) {
            if (isVisibleToUser) {
                this.onVisible();
            } else {
                this.onInvisible();
            }
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        mResumed = true;
        if (this.getUserVisibleHint()) {
            this.onVisible();
        }
    }

    @Override
    public final void onPause() {
        super.onPause();
        mResumed = false;
        this.onInvisible();
    }

    /**
     * Returns true if the fragment is in resumed state and userVisibleHint was set to true
     *
     * @return true if resumed and visible
     */
    protected final boolean isResumedAndVisible() {
        return mResumed && getUserVisibleHint();
    }

    /**
     * Called when onResume was called and userVisibleHint is set to true or vice-versa
     */
    protected abstract void onVisible();

    /**
     * Called when onStop was called or userVisibleHint is set to false
     */
    protected abstract void onInvisible();
}