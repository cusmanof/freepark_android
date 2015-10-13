package au.com.fc.utils;

import android.widget.Toast;
import au.com.fc.Main;
import au.com.fc.models.MdlDates;
import com.squareup.timessquare.CalendarPickerView;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class IOUtils {


    private Main main;

    /**
     * Contructor
     * @param main    the context
     */
    public IOUtils(Main main) {

        this.main = main;
    }

    public  void sendDatesFree(String name, String parkId, CalendarPickerView calendar) {
        List<Date> freeDates = calendar.getSelectedDates();
        MdlDates dts = new MdlDates("put", name, parkId, freeDates);
        try {
            dts.save();
        } catch (IOException e) {
            Toast.makeText(main, " Error saving the data " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //send out to server.
        if (!uploadGson(dts.getGson())) {
            Toast.makeText(main, " Error send data to the server. Try again later. ", Toast.LENGTH_LONG).show();
        }
    }

    public boolean getDatesFree(String name, String parkId, CalendarPickerView calendar) {
        try {
            MdlDates dts = new MdlDates("get", name, parkId, null);
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(5000);
            httpCon.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(dts.getGson());
            out.close();
            BufferedReader ins = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = ins.readLine()) != null)   {
                buf.append(line);
            }
            dts = (MdlDates) dts.setGson(buf.toString());
            dts.save();
            return true;
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }

    /**
     * Upload the specified data to the PHP server.
     *
     */
    public  boolean uploadGson(String data) {
        try {
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(5000);
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
