package com.devbyrod.distancetocity;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Rodrigo on 11/3/2014.
 */
public class MessageBox {

    public static void display( Context context, String msg ){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate( R.layout.message_box_toast, null );

        TextView textView = (TextView) view.findViewById(R.id.txt_toast_message);
        textView.setText( msg );

        Toast toast = new Toast( context );

        toast.setView( view );
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration( Toast.LENGTH_LONG );
        toast.show();
    }
}
