package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;

import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class MdlDates implements IModel {

    private String parkId;
    private String name;
    private List<Date> dates;

    public MdlDates(String parkId, String name, List<Date> freeDates) {
        this.parkId = parkId;
        this.name = name;
        dates = freeDates;
    }


    @Override
    public void save() throws IOException {
        File fil = new File(Defines.downDir, Defines.FP_DATES);
        FileWriter fOut = new FileWriter(fil);
        fOut.write(getGson());
        fOut.close();
    }

    @Override
    public String getGson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static MdlDates load() {
        Gson gson = new Gson();
        File fil = new File(Defines.downDir, Defines.FP_DATES);
        try {
            return gson.fromJson(new FileReader(fil), MdlDates.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public Collection<Date> getFreeDates() {
        return dates;
    }
}
