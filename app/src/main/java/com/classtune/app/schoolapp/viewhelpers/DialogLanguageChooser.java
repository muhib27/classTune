package com.classtune.app.schoolapp.viewhelpers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.classtune.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by BLACK HAT on 09-Mar-16.
 */
public class DialogLanguageChooser extends Dialog {

    private Context context;
    private CheckBox checkBoxEnglish;
    private CheckBox checkBoxBangla;
    private Button btnCancel;
    private Button btnOk;

    private List<CheckBox> listCheckBox;
    private CheckBox selectedCheckBox;

    private IDialogLanguageOkButtonListener okButtonListener;
    private String localIdentifier = "en";
    private boolean isSelected = false;

    public DialogLanguageChooser(Context context, IDialogLanguageOkButtonListener okButtonListener){
        super(context);
        this.context = context;
        this.okButtonListener = okButtonListener;

        localIdentifier = Locale.getDefault().getLanguage();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int dividerId = this.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = this.findViewById(dividerId);
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.classtune_green_color));

        setTitle(R.string.app_name);

        listCheckBox = new ArrayList<CheckBox>();

        setContentView(R.layout.layout_language_dialog);
        initView();
        initAction();

        if(localIdentifier.equals("en")){
            checkBoxEnglish.setChecked(true);
        }
        else if(localIdentifier.equals("bn")){
            checkBoxBangla.setChecked(true);
        }
    }

    private void initView(){

        checkBoxEnglish = (CheckBox)this.findViewById(R.id.checkBoxEnglish);
        checkBoxEnglish.setButtonDrawable(R.drawable.check_btn);
        checkBoxBangla = (CheckBox)this.findViewById(R.id.checkBoxBangla);
        checkBoxBangla.setButtonDrawable(R.drawable.check_btn);

        listCheckBox.add(checkBoxEnglish);
        listCheckBox.add(checkBoxBangla);

        btnCancel = (Button)this.findViewById(R.id.btnCancel);
        btnOk = (Button)this.findViewById(R.id.btnOk);

    }

    private void initAction(){

        checkBoxEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedCheckBox = (CheckBox)compoundButton;
                    updateCheckBoxes(selectedCheckBox);
                    localIdentifier = "en";
                    isSelected = true;
                }
                else{
                    isSelected = false;
                }
            }
        });

        checkBoxBangla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedCheckBox = (CheckBox)compoundButton;
                    updateCheckBoxes(selectedCheckBox);
                    localIdentifier = "bn";
                    isSelected = true;
                }
                else{
                    isSelected = false;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLanguageChooser.this.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do localization here
                if(isSelected){

                    if(Locale.getDefault().getLanguage().equals(localIdentifier)){
                        DialogLanguageChooser.this.dismiss();
                    }
                    else {
                        okButtonListener.onOkButtonPresse(localIdentifier);
                        DialogLanguageChooser.this.dismiss();
                    }


                }
                else {
                    Toast.makeText(context, R.string.java_dialoglanguagechooser_toast_select, Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void updateCheckBoxes(CheckBox cb){

        if(cb.isChecked()) {

            for(int i=0; i<listCheckBox.size();i++) {

                if(!listCheckBox.get(i).equals(cb)){
                    listCheckBox.get(i).setChecked(false);
                }

            }
        }
    }

    public interface IDialogLanguageOkButtonListener{

        public void onOkButtonPresse(String localIdentifier);
    }

}
