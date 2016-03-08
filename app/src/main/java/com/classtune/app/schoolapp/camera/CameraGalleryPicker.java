package com.classtune.app.schoolapp.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.classtune.app.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Date;

/**
 * Created by BLACK HAT on 20-Dec-15.
 */
public class CameraGalleryPicker {


    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1, REQUEST_CROP = 3;


    private Activity mActivity;
    private String dir;
    private IPictureCallback listener;
    private Uri mImageCaptureUri;

    public CameraGalleryPicker(Activity mActivity, IPictureCallback listener)
    {
        this.mActivity = mActivity;
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/classtune/";
        File newdir = new File(dir);

        if(!newdir.exists())
            newdir.mkdirs();

        this.listener = listener;
    }



    public void openCamere()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(new File(dir+new Date().getTime()+".jpeg"));

        intent.putExtra(
                android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);

        mActivity.startActivityForResult(intent, REQUEST_CAMERA);
    }


    public void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        mActivity.startActivityForResult(Intent.createChooser(intent, mActivity.getString(R.string.java_ameragallerypicker_select_file)), SELECT_FILE);

    }


    public void activityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
            else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(mActivity.getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(600, 600).start(mActivity);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            //resultView.setImageURI(Crop.getOutput(result));
            listener.onPicturetaken(new File(Crop.getOutput(result).getPath()));

        } else if (resultCode == Crop.RESULT_ERROR) {
            //Toast.makeText(mActivity, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void onCaptureImageResult(Intent data)
    {
        beginCrop(mImageCaptureUri);
    }

    private void onSelectFromGalleryResult(Intent data) {

        beginCrop(data.getData());


    }

}