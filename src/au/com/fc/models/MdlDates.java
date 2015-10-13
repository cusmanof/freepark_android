package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

/**
 * @author Frank Cusmano
 */
public class MdlDates implements IModel, Serializable{
    private List<Date> dates ;
    private String cmd;
    private String parkId;
    private String name;

    public MdlDates(String cmd, String name , String parkId, List<Date> freeDates) {
        this.cmd = cmd;
        this.parkId = parkId;
        this.name = name;
        this.dates = freeDates;
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

    @Override
    public IModel setGson(String ins) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(ins, MdlDates.class);
    }


    public static MdlDates load() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        File fil = new File(Defines.downDir, Defines.FP_DATES);
        try {
            return gson.fromJson(new FileReader(fil), MdlDates.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public Collection<Date> getFreeDates() {
        return dates ;
    }
}
