package com.classtune.app.schoolapp.classtune;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.LoginActivity;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.SchoolApp;

/**
 * Created by BLACK HAT on 08-Nov-15.
 */
public class UserSelectionActivity extends Activity implements View.OnClickListener{

    private int ordinal = -1;

    private ImageButton btnStudentSelect, btnParentSelect, btnTeacherSelect;
    private TextView txtMeHeader;
    private TextView txtMember;
    private TextView txtStudentHeader;
    private TextView txtParentHeader;
    private TextView txtTeacherHeader;
    private Button btnSignIn;
    private ImageButton btnAbout;
    private TextView txtMidHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_userselection_layout2);
        initView();
        initAction();
    }

    private void initView()
    {
        btnStudentSelect = (ImageButton)this.findViewById(R.id.btnStudentSelect);
        btnParentSelect = (ImageButton)this.findViewById(R.id.btnParentSelect);
        btnTeacherSelect = (ImageButton)this.findViewById(R.id.btnTeacherSelect);
        txtMeHeader = (TextView)this.findViewById(R.id.txtMeHeader);
        txtMember = (TextView)this.findViewById(R.id.txtMember);
        txtStudentHeader = (TextView)this.findViewById(R.id.txtStudentHeader);
        txtParentHeader = (TextView)this.findViewById(R.id.txtParentHeader);
        txtTeacherHeader = (TextView)this.findViewById(R.id.txtTeacherHeader);
        btnSignIn = (Button)this.findViewById(R.id.btnSignIn);
        btnAbout = (ImageButton)this.findViewById(R.id.btnAbout);
        txtMidHeader = (TextView)this.findViewById(R.id.txtMidHeader);
    }

    private void initAction()
    {
        btnStudentSelect.setOnClickListener(this);
        btnParentSelect.setOnClickListener(this);
        btnTeacherSelect.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        txtMeHeader.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));
        txtMeHeader.setText("I'm");

        txtMember.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));

        txtStudentHeader.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));
        txtParentHeader.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));
        txtTeacherHeader.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));

        btnSignIn.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));


        txtMidHeader.setTypeface(SchoolApp.getInstance().getClassTuneFontRes(AppConstant.CLASSTUNE_FONT_NAME));

        String text1 = "Register";
        String text2 = " FREE ";
        String text3 = "Now!";

        Spannable wordtoSpan = new SpannableString(text1+text2+text3);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.classtune_green_color_light)), text1.length()+1, text1.length()+text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtMidHeader.setText(wordtoSpan);

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, InfoPageMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = null;


        switch (v.getId())
        {
            case R.id.btnStudentSelect:
                intent = new Intent(UserSelectionActivity.this, RegistrationFirstPhaseActivity.class);
                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, 2);
                break;

            case R.id.btnParentSelect:
                intent = new Intent(UserSelectionActivity.this, RegistrationFirstPhaseActivity.class);
                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, 4);
                break;

            case R.id.btnTeacherSelect:
                intent = new Intent(UserSelectionActivity.this, RegistrationFirstPhaseActivity.class);
                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, 3);
                break;

            case R.id.btnSignIn:
                intent = new Intent(UserSelectionActivity.this, LoginActivity.class);
                break;

            default:
                break;
        }

        if(intent != null) {
            startActivity(intent);
        }
    }
}
