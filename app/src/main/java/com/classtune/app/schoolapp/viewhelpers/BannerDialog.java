package com.classtune.app.schoolapp.viewhelpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppConstant;

import java.util.Random;

/**
 * Created by BLACK HAT on 03-Jan-16.
 */
public class BannerDialog extends Dialog{

    private Context context;
    private ImageView imgBanner;
    private int[] arrayImage = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4, R.drawable.banner_5};
    private ImageButton btnCross;


    public BannerDialog(Context context)
    {
        super(context);
        this.context = context;

        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(BannerDialog.this != null)
                    BannerDialog.this.dismiss();

            }
        };

        Handler h = new Handler();
        h.postDelayed(r, AppConstant.BANNER_POPUP_DISMISS_TIME);

        this.setCancelable(false);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_banner_dialog_free);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        initView();
        initAction();
    }


    private void initView()
    {
        imgBanner = (ImageView)findViewById(R.id.imgBanner);
        btnCross = (ImageButton)findViewById(R.id.btnCross);
    }

    private void initAction()
    {
        imgBanner.setImageResource(arrayImage[getRandomNumberInRange(0, arrayImage.length-1)]);
        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerDialog.this.dismiss();
            }
        });
    }

    private int getRandomNumberInRange(int minVal, int maxVal)
    {
        Random r = new Random();
        int num = r.nextInt(maxVal - minVal + 1) + minVal;

        return num;
    }

}
