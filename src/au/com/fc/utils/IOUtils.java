package au.com.fc.utils;

import android.widget.Toast;
import au.com.fc.Main;
import au.com.fc.models.MdlAll;
import au.com.fc.models.MdlDates;
import au.com.fc.models.MdlRelease;
import au.com.fc.models.MdlReserve;
import com.squareup.timessquare.CalendarPickerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author Frank Cusmano
 */
public class IOUtils {


    private Main main;
    private final String name;
    private final String parkId;
    private final CalendarPickerView calendar;
    MdlDates mdlDates = new MdlDates();
    private MdlAll mdlAll = new MdlAll();

    /**
     * Contructor
     *
     * @param main the context
     */
    public IOUtils(Main main, String name, String parkId, CalendarPickerView calendar) {
        this.main = main;
        this.name = name;
        this.parkId = parkId;
        this.calendar = calendar;
    }

    public void sendDatesFree() {
        List<Date> freeDates = calendar.getSelectedDates();
        MdlDates dts = new MdlDates("put", name, parkId, freeDates);
        //send out to server.
        if (!uploadGson(dts)) {
            Toast.makeText(main, " Error send data to the server. Try again later. ", Toast.LENGTH_LONG).show();
        }
    }

    public boolean loadDatesFree() {
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
            while ((line = ins.readLine()) != null) {
                buf.append(line);
            }
            mdlDates = (MdlDates) dts.setGson(buf.toString());
            return true;
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }

    public boolean loadUnreserved() {
        try {
            MdlAll all = new MdlAll();
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(5000);
            httpCon.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(all.getGson());
            out.close();
            BufferedReader ins = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = ins.readLine()) != null) {
                buf.append(line);
            }
            mdlAll = (MdlAll) all.setGson(buf.toString());
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
     * @param dts .
     */
    public boolean uploadGson(MdlDates dts) {
        try {
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
            while ((line = ins.readLine()) != null) {
                buf.append(line);
            }
            mdlDates = (MdlDates) dts.setGson(buf.toString());
            return true;
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }

    public boolean release(Date date) {
        MdlRelease rel = new MdlRelease(date, name);
        try {
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(5000);
            httpCon.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(rel.getGson());
            out.close();
            BufferedReader ins = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = ins.readLine()) != null) {
                buf.append(line);
            }
            mdlAll = (MdlAll) new MdlAll().setGson(buf.toString());
            return true;
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }

    public boolean reserveForMe (Date date) {
        MdlReserve rel = new MdlReserve(date, name);
        try {
            final URL url = new URL(Defines.LOCAL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(5000);
            httpCon.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(rel.getGson());
            out.close();
            BufferedReader ins = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = ins.readLine()) != null) {
                buf.append(line);
            }
            mdlAll = (MdlAll) new MdlAll().setGson(buf.toString());
            return true;
        } catch (Exception e) {
            //drop thru
            System.out.println("e = " + e);
        }
        return false;
    }
    public boolean isReserved(Date date) {
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(date)) {
                    return mdlDates.getUsed().get(i) != null;
                }
            }
        }
        return false;
    }

    public Collection<Date> getFreeDates() {
        List<Date> ret = new LinkedList<>();
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (mdlDates.getUsed().get(i) == null)
                    ret.add(dates.get(i));
            }
        }
        return ret;
    }

    public Collection<Date> getUnFreeDates() {
        List<Date> ret = new LinkedList<>();
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (mdlDates.getUsed().get(i) != null)
                    ret.add(dates.get(i));
            }
        }
        return ret;
    }

    public String getReservedName(Date date) {
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (mdlDates.getUsed().get(i) != null)
                    return mdlDates.getUsed().get(i);
            }
        }
        return "unknown";
    }

    public Collection<Date> getUnreservedDates() {
        Collection<Date> cannotUse = getReservedByMe();
        HashSet<Date> ret = new HashSet<>();
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                Date d = dates.get(i);
                if (mdlAll.getUsed().get(i) == null && !cannotUse.contains(d))
                    ret.add(d);
            }
        }
        return ret;
    }

    public Collection<Date> getReservedByMe() {
        List<Date> ret = new LinkedList<>();
        List<String> used = mdlAll.getUsed();
        if (used != null) {
            for (int i = 0; i < used.size(); i++) {
                if (name.equals(mdlAll.getUsed().get(i))) {
                    ret.add(mdlAll.getDates().get(i));
                }
            }
        }
        return ret;
    }

    public boolean isReservedByMe(Date date) {
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(date)) {
                    if (name.equals(mdlAll.getUsed().get(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getReservedBay(Date date) {
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(date)) {
                    if (name.equals(mdlAll.getUsed().get(i))) {
                        return mdlAll.getParkId().get(i) + " : " + mdlAll.getOwner().get(i);
                    }
                }
            }
        }
        return "unknown";
    }


    public boolean canReserve(Date date) {
        return getUnreservedDates().contains(date);
    }
}
