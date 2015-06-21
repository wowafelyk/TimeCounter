package com.example.fenix.timecounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

/**
 * Created by fenix on 16.06.2015.
 */
public class DialogSetting extends DialogFragment {

    private static final String TAG="Time";
    private Button menu_timeOutput,menu_color,menu_scope,menu_print;
    private Button apply,cancel;

    public DialogSetting(){
       //Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_settings,container);
        getDialog().setTitle(R.string.settings);

        menu_timeOutput = (Button)view.findViewById(R.id.menu_timeOutput);
        apply = (Button)view.findViewById(R.id.apply);
        cancel = (Button)view.findViewById(R.id.cancel);
        menu_color = (Button)view.findViewById(R.id.menu_color);
        menu_scope = (Button)view.findViewById(R.id.menu_scope);
        menu_print = (Button)view.findViewById(R.id.menu_print);



        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, Long.toString(Thread.currentThread().getId()) + " = DialogSettings");
                dismiss();
            }
        });




        return view;
    }





    public void onRadioButtonClicked(View view){

    }

}
