package au.com.fc.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author Frank Cusmano
 */
public class Defines {
    public static final File downDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    public static final String FP_CONF = "FreePark.conf";
    public static final String FP_DATES = "FreePark.dates";
    public static final String FP_HOST =  "http://freepark-1091.appspot.com/freedays.php";
    public static final String LOCAL = "http://10.0.2.2/freepark/freedays.php";    //used for testing n local PC
}
