package com.classtune.app.schoolapp.classtune;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.classtune.app.R;

/**
 * Created by BLACK HAT on 07-Dec-15.
 */
public class InfoPageMainActivity extends Activity{

    private FontButton btnFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_classtune_infopage_main);

        btnFeature = (FontButton)this.findViewById(R.id.btnFeature);
        btnFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InfoPageMainActivity.this, InfoFeatureActivity.class);
                startActivity(intent);
            }
        });
    }
}
