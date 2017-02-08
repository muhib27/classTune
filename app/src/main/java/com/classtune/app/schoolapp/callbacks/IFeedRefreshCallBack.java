package com.classtune.app.schoolapp.callbacks;

import android.content.Intent;

/**
 * Created by Extreme_Piash on 2/8/2017.
 */

public interface IFeedRefreshCallBack {

    void onRefresh(int requestCode, int resultCode, Intent data);
}
