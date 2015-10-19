package au.com.fc.utils;

import android.os.AsyncTask;
import au.com.fc.models.*;
import com.squareup.timessquare.CalendarPickerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author Frank Cusmano
 */
public class IOUtils {


    private final String name;
    private final String parkId;
    private final CalendarPickerView calendar;
    private MdlDates mdlDates = new MdlDates();
    private MdlAll mdlAll = new MdlAll();
    private boolean moreToDo;
    private boolean running;
    private Object lock = new Object();

    /**
     * Constructor
     */
    public IOUtils(String name, String parkId, CalendarPickerView calendar) {
        this.name = name;
        this.parkId = parkId;
        this.calendar = calendar;
    }


    private static HttpURLConnection getHttpURLConnection() throws IOException {
        final URL url = new URL(Defines.FP_HOST);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setDoInput(true);
        httpCon.setConnectTimeout(5000);
        httpCon.setRequestMethod("PUT");
        return httpCon;
    }

    public boolean loadDatesFree() {
        try {
            MdlDates dts = new MdlDates("get", name, parkId, null);
            HttpURLConnection httpCon = getHttpURLConnection();

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

    public boolean loadAll() {
        try {
            MdlAll all = new MdlAll();
            HttpURLConnection httpCon = getHttpURLConnection();

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
            HttpURLConnection httpCon = getHttpURLConnection();

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
            HttpURLConnection httpCon = getHttpURLConnection();

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

    public boolean reserveForMe(Date date) {
        MdlReserve rel = new MdlReserve(date, name);
        try {
            HttpURLConnection httpCon = getHttpURLConnection();

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

    public boolean requestForMe(Date date) {
        MdlRequest rel = new MdlRequest(date, name);
        try {
            HttpURLConnection httpCon = getHttpURLConnection();

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

    public List<Date> getRequested() {
        loadAll();
        List<Date> ret = new LinkedList<>();
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (!mdlAll.getUsed().get(i).isEmpty() && mdlAll.getParkId().get(i).isEmpty()) {
                    ret.add(dates.get(i));
                }
            }
        }
        return ret;
    }

    public Collection<Date> getFreeDates() {
        List<Date> ret = new LinkedList<>();
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (mdlDates.getUsed().get(i).isEmpty())
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
                if (!mdlDates.getUsed().get(i).isEmpty())
                    ret.add(dates.get(i));
            }
        }
        return ret;
    }

    public String getReservedName(Date date) {
        List<Date> dates = mdlDates.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (mdlDates.getDates().get(i).equals(date) && !mdlDates.getUsed().get(i).isEmpty())
                    return mdlDates.getUsed().get(i);
            }
        }
        return null;
    }

    public Collection<Date> getUnreservedDates() {
        Collection<Date> cannotUse = getReservedByMe();
        HashSet<Date> ret = new HashSet<>();
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                Date d = dates.get(i);
                if (mdlAll.getUsed().get(i).isEmpty() && !cannotUse.contains(d))
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
                if (name.equals(mdlAll.getUsed().get(i)) && !mdlAll.getParkId().get(i).isEmpty()) {
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
                    if (name.equals(mdlAll.getUsed().get(i)) &&  !mdlAll.getParkId().get(i).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getMyParkId(Date date) {
        List<Date> dates = mdlAll.getDates();
        if (dates != null) {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(date)) {
                    if (name.equals(mdlAll.getUsed().get(i))) {
                        String p = mdlAll.getParkId().get(i);
                        if (p.isEmpty()) {
                            return "REQ";
                        }
                        return p;
                    }
                }
            }
        }
        return null;
    }


    public boolean canReserve(Date date) {
        return getUnreservedDates().contains(date);
    }


    /**
     * background task to update free dates.
     */
    public void sendDatesFree() {
        synchronized (lock) {
            moreToDo = true;
        }
        if (!running) {
            new BgSender().execute();
        }
    }

    public String getName() {
        return name;
    }

    public String getParkId() {
        return parkId;
    }


    private class BgSender extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (moreToDo) {
                synchronized (lock) {
                    moreToDo = false;
                }
                List<Date> freeDates = calendar.getSelectedDates();
                MdlDates dts = new MdlDates("put", name, parkId, freeDates);
                //send out to server.
                uploadGson(dts);

                //wait and see if more dates pressed.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            running = false;
        }


    }
}
