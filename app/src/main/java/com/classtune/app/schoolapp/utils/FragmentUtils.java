package com.classtune.app.schoolapp.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by BLACK HAT on 17-Feb-16.
 */
public class FragmentUtils {

    /**
     * @param fragment
     *            The Fragment whose parent is to be found
     * @param parentClass
     *            The interface that the parent should implement
     * @return The parent of fragment that implements parentClass,
     *         null, if no such parent can be found
     */
    @Nullable
    public static <T> T getParent(@NonNull Fragment fragment, @NonNull Class<T> parentClass) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentClass.isInstance(parentFragment)) {
            //Checked by runtime methods
            //noinspection unchecked
            return (T) parentFragment;
        } else if (parentClass.isInstance(fragment.getActivity())) {
            //Checked by runtime methods
            //noinspection unchecked
            return (T) fragment.getActivity();
        }
        return null;
    }
}