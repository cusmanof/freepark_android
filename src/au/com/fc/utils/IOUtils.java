package au.com.fc.utils;

import org.apache.http.entity.StringEntity;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Frank Cusmano
 */
public class IOUtils {


    /**
     * Upload the specified file to the PHP server.
     *
     * @param filePath The path towards the file.
     * @param fileName The name of the file that will be saved on the server.
     */
    public static boolean uploadGson(String data) {
        try {
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(data);
            out.close();
            final int statusCode = httpCon.getResponseCode();
            if (HttpURLConnection.HTTP_OK == statusCode) {
                return true;
            }
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }

}
