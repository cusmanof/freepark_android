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

}
