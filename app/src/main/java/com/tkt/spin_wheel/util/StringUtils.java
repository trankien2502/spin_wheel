package com.tkt.spin_wheel.util;

import android.widget.TextView;

public class StringUtils {
    public static int getNumbeText(TextView textView){
        return Integer.parseInt(textView.getText().toString().trim());
    }
}
