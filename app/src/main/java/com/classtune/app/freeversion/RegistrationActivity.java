package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.classtune.app.R;
import com.classtune.app.schoolapp.SocialBaseActivity;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog.UserTypeListener;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.utils.SPKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

import java.util.ArrayList;


public class RegistrationActivity extends SocialBaseActivity implements OnClickListener,UserTypeListener{

	private EditText etEmail;
	private EditText etReEmail;
	private EditText etPassword;
	private EditText etRePassword;
	private Button btnCreateAcc;
	private RadioGroup rgUserType;
	private Button btnFbLogin;
	private Button btnGoogleLogin;

	SchoolApp app;
	UIHelper uiHelper;
	UserHelper userHelper;
	ListView listView;
	ArrayList<User> childrenList;

	String email = "", reEmail = "", password = "", rePassword = "";
	int selectedId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_type_selection);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		app = (SchoolApp) getApplicationContext();
		userHelper = new UserHelper(this,RegistrationActivity.this);
		uiHelper = new UIHelper(this);
		etEmail=(EditText)findViewById(R.id.et_email);
		etReEmail=(EditText)findViewById(R.id.et_re_email);
		etPassword=(EditText)findViewById(R.id.et_password);
		etRePassword=(EditText)findViewById(R.id.et_re_password);
		btnCreateAcc=(Button)findViewById(R.id.btn_create_acc);
		btnFbLogin=(Button)findViewById(R.id.fb_login_btn);
		btnGoogleLogin=(Button)findViewById(R.id.google_login_btn);
		rgUserType=(RadioGroup)findViewById(R.id.radioGroup_usertype);
		
		btnCreateAcc.setOnClickListener(this);
		btnFbLogin.setOnClickListener(this);
		btnGoogleLogin.setOnClickListener(this);

		app.setupUI(findViewById(R.id.layout_parent), this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_create_acc:
			doCreateAccount();
			//startActivity(new Intent(this, CompleteProfileActivity.class));
			break;
		case R.id.fb_login_btn:
			doFaceBookLogin(false);
			break;
		case R.id.google_login_btn:
			doGooglePlusLogin();
			break;
		default:
			break;
		}
	}
	
	int ordinal = -1;

	private void doCreateAccount() {
		// TODO Auto-generated method stub

		selectedId = rgUserType.getCheckedRadioButtonId();

		email = etEmail.getText().toString();
		reEmail = etReEmail.getText().toString();
		password = etPassword.getText().toString();
		rePassword = etRePassword.getText().toString();

		if (selectedId == -1) {
			uiHelper.showErrorDialog("Select User Type!");
		} else {
			if (email.equalsIgnoreCase("")) {
				uiHelper.showErrorDialog("Enter Email Address!");
			} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
				uiHelper.showErrorDialog("Invalid Email Address!");
			} else if (!email.equals(reEmail)) {
				uiHelper.showErrorDialog("Email Addresses did not match!");
			} 
			else if(password.length()<6)
			{
				uiHelper.showErrorDialog("Password should be minimum of 6 characters.");
			}else if (password.equalsIgnoreCase("")) {
				uiHelper.showErrorDialog("Enter Password!");
			} else if (!password.equals(rePassword)) {
				uiHelper.showErrorDialog("Passwords did not match!");
			} else {
				switch (selectedId) {
				case R.id.radio_student:
					ordinal = 2;
					break;

				case R.id.radio_parent:
					ordinal = 4;
					break;

				case R.id.radio_teacher:
					ordinal = 3;
					break;

				case R.id.radio_other:
					ordinal = 1;
					break;

				default:
					break;
				}
				User u=new User();
				u.setEmail(email);
				u.setPassword(password);
				u.setType(UserTypeEnum.values()[ordinal]);
				userHelper.createUser(u);
				
			}
		}
	}
	
	


	@Override
	public void onAuthenticationStart() {
		uiHelper.showLoadingDialog(getString(R.string.loading_text));
	}

	@Override
	public void onAuthenticationSuccessful() {
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		if (UserHelper.isRegistered()) {
			if (UserHelper.isLoggedIn()) {

			/*

				switch (UserHelper.getUserAccessType()) {
				case FREE:
					startActivity(new Intent(RegistrationActivity.this,
							HomeBaseActivity.class));
					finish();
					break;
				case PAID:
					switch (userHelper.getUser().getType()) {
					case ADMIN:
						break;
					case PARENTS:
						if (userHelper.getUser().getChildren() == null) {
							Log.e("Userhelper", "null");
						}
						if (userHelper.getUser().getChildren().size() > 1) {
							startActivity(new Intent(this,
									ChildSelectionActivity.class));
							finish();
						}
						break;
					case STUDENT:
						startActivity(new Intent(RegistrationActivity.this,
								HomePageFreeVersion.class));
						finish();
						break;
					case TEACHER:
						break;
					default:
						break;
					}

					break;

				default:
					break;
				}*/
			} else {

				Intent intent=new Intent(RegistrationActivity.this,CompleteProfileActivityContainer.class);
				int mm = SharedPreferencesHelper.getInstance().getInt(
						SPKeyHelper.USER_TYPE, 2);
				intent.putExtra(SPKeyHelper.USER_TYPE, SharedPreferencesHelper.getInstance().getInt(
						SPKeyHelper.USER_TYPE, 2));
				intent.putExtra("FIRST_TIME", true);
				startActivity(intent);
				finish();

			}
		} else {
			Log.e("TypeSelection!", "GOOOOOOOOOOOOOOOO");
			//StartTypeSelectionDialog
			UserTypeSelectionDialog dialogFrag = UserTypeSelectionDialog.newInstance();
            dialogFrag.show(getSupportFragmentManager().beginTransaction(), "dialog");

		}

	}

	
	
	

	@Override
	public void onAuthenticationFailed(String msg) {
		if(uiHelper.isDialogActive())
		{
			uiHelper.dismissLoadingDialog();
			uiHelper.showMessage(msg);
		}
	}


	@Override
	public void onTypeSelected(int ordinal) {
		
		Log.e("Type", UserTypeEnum.values()[ordinal].toString());
		userHelper.updateUserWithType(ordinal);
	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}
	
	
}
