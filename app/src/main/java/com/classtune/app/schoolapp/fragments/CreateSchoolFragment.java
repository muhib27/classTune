package com.classtune.app.schoolapp.fragments;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.classtune.app.schoolapp.LoginActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.CropOptionAdapter;
import com.classtune.app.schoolapp.model.CropOption;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateSchoolFragment extends Fragment implements UserAuthListener {

	private View view;
	private UIHelper uiHelper;

	private CustomButton btnUploadLogo;
	private CustomButton btnUploadPicture;
	private CustomButton btnSend;
	
	private final int REQUEST_CODE_CAMERA = 110;
	private final int REQUEST_CODE_GELLERY = 111;
	private final int REQUEST_CODE_CROP = 112;
	private static File schoolDirectory = null;
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private Uri uri = null;
	private String mCurrentPhotoPath;
	
	private String selectedImagePath="";
	private String selectedImagePath2="";
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private EditText txtSchoolName;
	private EditText txtContact;
	private EditText txtAddress;
	private EditText txtZipCode;
	private EditText txtAbout;
	private boolean isLogoImage = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		schoolDirectory = new File(getActivity().getFilesDir().getPath()
				+ "/champs21");
		schoolDirectory.mkdirs(); // create folders where write files

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}	

		// picsUrls=new ArrayList<String>();
	}
	
	
	@Override
	public void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	}


	@Override
	public void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		uiHelper = new UIHelper(this.getActivity());

		view = inflater.inflate(R.layout.fragment_create_school, container,
				false);

		initView(view);

		return view;
	}

	private void initView(View view) {
		this.btnUploadLogo = (CustomButton)view.findViewById(R.id.btnUploadLogo);

		this.btnUploadLogo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLogoImage = true;
				showPicChooserDialog();
			}
		});
		
		
		this.btnUploadPicture = (CustomButton)view.findViewById(R.id.btnUploadPicture);
		
		this.btnUploadPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLogoImage = false;
				showPicChooserDialog();
			}
		});
		


		this.btnSend = (CustomButton)view.findViewById(R.id.btnSend);
		this.btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(UserHelper.isLoggedIn() == false)
				{
					showLoginDialog();
				}
				else
				{
					initApiCall();
				}
			}
		});
		
		
		this.txtSchoolName = (EditText)view.findViewById(R.id.txtSchoolName);
		this.txtContact = (EditText)view.findViewById(R.id.txtContact);
		this.txtAddress = (EditText)view.findViewById(R.id.txtAddress);
		this.txtZipCode = (EditText)view.findViewById(R.id.txtZipCode);
		this.txtAbout = (EditText)view.findViewById(R.id.txtAbout);
		
	}

	private void initApiCall() {

		RequestParams params = new RequestParams();
		params.put("user_id", UserHelper.getUserFreeId());
		params.put("school_name", this.txtSchoolName.getText().toString());
		params.put("contact", this.txtContact.getText().toString());
		params.put("address", this.txtAddress.getText().toString());
		params.put("zip_code", this.txtZipCode.getText().toString());
		params.put("about", this.txtAbout.getText().toString());
		
		if(!selectedImagePath.equalsIgnoreCase(""))
		{
			File myImage = new File(selectedImagePath);
			try {
			    params.put("logo", myImage);
			    
			    Log.e("IMG_FILE", "is: " + myImage);
			} catch(FileNotFoundException e) {}
		}
		
		if(!selectedImagePath2.equalsIgnoreCase(""))
		{
			File myImage = new File(selectedImagePath2);
			try {
			    params.put("picture", myImage);
			    
			    Log.e("PHOTO_FILE", "is: " + myImage);
			} catch(FileNotFoundException e) {}
		}
		
		
		

		
		if(this.txtSchoolName.getText().toString().length() <= 0 )
			Toast.makeText(CreateSchoolFragment.this.getActivity(), "Please provide your school name correctly", Toast.LENGTH_SHORT).show();
		
		else if(this.txtContact.getText().toString().length() <= 0 )
			Toast.makeText(CreateSchoolFragment.this.getActivity(), "Please provide your school's contact correctly", Toast.LENGTH_SHORT).show();
		
		else if(this.txtAddress.getText().toString().length() <= 0 )
			Toast.makeText(CreateSchoolFragment.this.getActivity(), "Please provide your school's address correctly", Toast.LENGTH_SHORT).show();
		
		else if(this.txtAbout.getText().toString().length() <= 0 )
			Toast.makeText(CreateSchoolFragment.this.getActivity(), "Please provide some information about your school correctly", Toast.LENGTH_SHORT).show();
		
		
		if(this.txtSchoolName.getText().toString().length() > 0 && this.txtContact.getText().toString().length() > 0 && this.txtAddress.getText().toString().length() > 0 &&
				this.txtAddress.getText().toString().length() > 0)
			
			AppRestClient.post(URLHelper.URL_FREE_VERSION_SCHOOL_CREATE, params,
					createSchoolNameHandler);
	}

	private AsyncHttpResponseHandler createSchoolNameHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog("Please wait...");
		};

		@Override
		public void onSuccess(String responseString) {
			// Log.e("FREE_HOME", "data: "+responseString);

			uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				
				Toast.makeText(CreateSchoolFragment.this.getActivity(), "Successfully submitted  your request.", Toast.LENGTH_SHORT).show();

			}

			else {

			}
		};
	};

	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticationSuccessful() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub

	}
	
	public void dispatchOpenGelleryApp() {

		if (Build.VERSION.SDK_INT <19){
		    Intent intent = new Intent();
		    intent.setType("image/jpeg");
		    intent.setAction(Intent.ACTION_GET_CONTENT);
		    startActivityForResult(Intent.createChooser(intent, "Select Picture"),
					REQUEST_CODE_GELLERY);
		} else {
			Intent intent = new Intent(Intent.ACTION_PICK);
			 intent.setType("*/*");
			 intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
			                            uri);
			 startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_CODE_GELLERY);
		   
		}

	}
	
	public void dispatchTakePictureIntent() {

		PackageManager pm = getActivity().getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
			Toast.makeText(getActivity(), "Camera Nai", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File f = null;

		try {
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}

		startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
	}
	

	/*public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		switch (requestCode) {
		case 0:
			
				if(resultCode == getActivity().RESULT_OK)
				{
					Uri selectedImage = imageReturnedIntent.getData();
					Log.e("IMG_URI", "path is: "+selectedImage);
				}
				if (resultCode == getActivity().RESULT_CANCELED) {
					return;
				}
			

			break;
		case 1:
				if(resultCode == getActivity().RESULT_OK)
				{
					Uri selectedImage2 = imageReturnedIntent.getData();
					Log.e("IMG_URI", "path is: "+selectedImage2);
				}
				if (resultCode == getActivity().RESULT_CANCELED) {
					return;
				}
			
			break;
		}
	}*/

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		
		switch (requestCode) {
		
		
		
		case REQUEST_CODE_CAMERA:
			if (resultCode == getActivity().RESULT_OK) {
				// Log.e("addAdvertiseController.mCurrentPhotoPath",""+addAdvertiseController.mCurrentPhotoPath);
				dispatchCropIntent(Uri.fromFile(new File(mCurrentPhotoPath)));
			}
			if (resultCode == getActivity().RESULT_CANCELED) {
				return;
			}
			break;
		case REQUEST_CODE_GELLERY:
			if (resultCode == getActivity().RESULT_OK) {
				// addAdvertiseController.mCurrentPhotoPath=getFilePath(data.getData());
				// Log.e("addAdvertiseController.mCurrentPhotoPath2",""+addAdvertiseController.mCurrentPhotoPath);
				
				dispatchCropIntent(data.getData());
				
				
				
			}
			if (resultCode == getActivity().RESULT_CANCELED) {
				return;
			}
			break;

		case REQUEST_CODE_CROP:
			if (resultCode == getActivity().RESULT_OK) {
				// Log.e("addAdvertiseController.mCurrentPhotoPath3",""+addAdvertiseController.mCurrentPhotoPath);
				File file = new File(mCurrentPhotoPath);
				Log.e("Normal file size:", "Image size before compress:"
						+ (file.length() / 1024) + "");
				/*
				 * if((file.length()/1024)<300) { AlertDialog_big_image_size();
				 * } else {
				 */
				handleBigCameraPhoto(false,
						Uri.fromFile(new File(mCurrentPhotoPath)));
				// }
			}

			if (resultCode == getActivity().RESULT_CANCELED) {
				return;
			}
			break;

		}
		
		
		//super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void dispatchCropIntent(Uri uriParam) {

		uri = uriParam;
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getActivity().getPackageManager()
				.queryIntentActivities(intent, 0);

		int size = list.size();
		if (size == 0) {
			// Toast.makeText(this, "Can not find image crop app",
			// Toast.LENGTH_SHORT).show();
			handleBigCameraPhoto(true, uriParam);
			return;
		} else {
			intent.setData(uri);
			int height = AppUtility.getImageViewerImageHeight(getActivity());
			intent.putExtra("outputX", 600);
			intent.putExtra("outputY", 600);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", false);
			intent.putExtra("noFaceDetection", true);

			File f = null;
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();

			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			intent.putExtra("output", Uri.fromFile(f));

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, REQUEST_CODE_CROP);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getActivity().getPackageManager()
							.getApplicationLabel(
									res.activityInfo.applicationInfo);
					co.icon = getActivity().getPackageManager()
							.getApplicationIcon(
									res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(getActivity()
						.getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										REQUEST_CODE_CROP);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (uri != null) {
							getActivity().getContentResolver().delete(uri,
									null, null);
							uri = null;
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
		
		
		
	}
	
	
	private String getFileNameFromPath(String path) {
		String[] tokens = path.split("/");
		return tokens[tokens.length - 1];
	}
	
	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		// Uri.fromFile(f);
		return f;
	}
	
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}
	private String getAlbumName() {
		return getString(R.string.album_name);
	}
	
	private void handleBigCameraPhoto(boolean resizeFlag, Uri uriParam) {

		if (uriParam != null) {
			setPic(resizeFlag, uriParam);
			if(isLogoImage == true)
			{
				selectedImagePath = mCurrentPhotoPath;
				
				// galleryAddPic();
				mCurrentPhotoPath = null;
			}
			else if(isLogoImage == false)
			{
				selectedImagePath2 = mCurrentPhotoPath;
				
				// galleryAddPic();
				mCurrentPhotoPath = null;
			}
			
			
			//Log.e("IMG_FILE", "is: "+getFileNameFromPath(selectedImagePath));
		}

	}
	
	private void setPic(boolean resizeFlag, Uri uriParam) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;

		ContentResolver res = getActivity().getContentResolver();
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			is = res.openInputStream(uriParam);
			if (resizeFlag == true) {
				bitmap = BitmapFactory.decodeStream(is, null, bmOptions);

			} else {
				bitmap = BitmapFactory.decodeStream(is);
			}
			is.close();
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		/* Figure out which way needs to be reduced less */
		/* Get the size of the ImageView */
		/*
		 * int targetW = mImageView.getWidth(); int targetH =
		 * mImageView.getHeight();
		 */
		if (resizeFlag) {
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;
			int value = AppUtility.getImageViewerImageHeight(getActivity());
			int targetW = value;
			int targetH = value;

			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {
				scaleFactor = Math.min(photoW / targetW, photoH / targetH);
			}

			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			/* Decode the JPEG file into a Bitmap */
			try {
				is = res.openInputStream(uriParam);
				bitmap = BitmapFactory.decodeStream(is, null, bmOptions);
				is.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		// force orientation to portrait
		if (bitmap.getWidth() > bitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		}
		// Log.e("Aise", "Aise");

		// File file = new File(addAdvertiseController.mCurrentPhotoPath);
		// Log.e("Normal file size:", file.length() + "");
		// Toast.makeText(AddAdvertiseActivity.this, file.length()+"",
		// Toast.LENGTH_SHORT).show();

		FileOutputStream fOut = null;
		try {
			long timestamp = System.currentTimeMillis();
			File ezpsaImageFile = new File(schoolDirectory,
					getString(R.string.album_name) + timestamp + ".png");

			fOut = new FileOutputStream(ezpsaImageFile);

			int quality = 40;
			int increament = 10;
			int maxFileSize = 100 * 1024;
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);

			while (ezpsaImageFile.length() > maxFileSize) {
				quality += increament;
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
			}

			// Log.e("Compressed file size:", file.length() + "");

			// Toast.makeText(AddAdvertiseActivity.this, file.length()+"",
			// Toast.LENGTH_SHORT).show();
			fOut.flush();
			fOut.close();
			// b.recycle();
			bitmap.recycle();
			mCurrentPhotoPath = ezpsaImageFile.getPath();

		} catch (Exception e) { // TODO
			e.printStackTrace();
		}

	}
	
	private void showLoginDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				CreateSchoolFragment.this.getActivity());

		

		alertDialogBuilder
				.setMessage("you need to be logged in first to send create school request.")
				.setCancelable(false)
				.setPositiveButton("Login",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								
								Intent intent = new Intent(CreateSchoolFragment.this.getActivity(), LoginActivity.class);
								startActivity(intent);
								getActivity().getSupportFragmentManager().beginTransaction().remove(CreateSchoolFragment.this).commit();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}
	
	private void showPicChooserDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				CreateSchoolFragment.this.getActivity());

		

		alertDialogBuilder
				.setMessage("Select source")
				.setCancelable(false)
				.setPositiveButton("Gallery",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

								/*Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Picture"),
										1);*/
								
								dispatchOpenGelleryApp();

							}
						})
				.setNegativeButton("Camera",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								
								dispatchTakePictureIntent();

								/*Intent takePicture = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(takePicture, 0);*/
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCancelable(true);

		alertDialog.show();

	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}

}
