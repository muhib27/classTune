package com.classtune.app.schoolapp.viewhelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class UIHelper {

	ProgressDialog loadingDialog;
    Activity activity;

    public UIHelper(Activity activity) {
        this.activity = activity;
    }

    public void showMessage(String message)
	{
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
    
    public void showLoadingDialog(final String message) {
    	
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    loadingDialog = ProgressDialog.show(activity, "", message, true, false);
                }
            });
        }
    }

    public void updateLoadingDialog(final String message) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    loadingDialog.setMessage(message);
                }
            });
        }
    }

    public boolean isDialogActive() {
        if (loadingDialog != null) {
            return loadingDialog.isShowing();
        } else {
            return false;
        }
    }

    public void dismissLoadingDialog() {
        if (activity != null && loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showErrorDialog(String errorMessage) {
        new AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void showSuccessDialog(String errorMessage,String title) {
        new AlertDialog.Builder(activity).setMessage(errorMessage).setTitle(title).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).create().show();
    }
    
    public void showErrorDialogOnGuiThread(final String errorMessage) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            dismissLoadingDialog();
                        }
                    }).create().show();
                }
            });
        }
    }
    
    public void showInternetConnectivityDialog(String conenctionMessage,String title) {
    	
    	final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
    	dialog.setMessage(conenctionMessage);
    	dialog.setTitle(title);
    	dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
				dialog.dismiss();
			}
		});
    	
    	dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
    	
    	dialog.create();
    	dialog.show();
    	
        
    }
    
    
    
}
