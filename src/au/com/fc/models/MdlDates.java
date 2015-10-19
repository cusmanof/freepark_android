package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class MdlDates implements IModel, Serializable {

    private List<Date> dates = new LinkedList<>();
    private List<String> used = new LinkedList<>();
    private String cmd;
    private String parkId;
    private String name;

    public MdlDates(String cmd, String name, String parkId, List<Date> freeDates) {
        this.cmd = cmd;
        this.parkId = parkId;
        this.name = name;
        dates = freeDates;

    }

    public MdlDates() {
        this.cmd = "";
        this.parkId = "";
        this.name = "";
    }


    @Override
    public String getGson() {
        Gson gson = new GsonBuilder().setDateFormat(Defines.DATE_FORMAT).create();
        return gson.toJson(this);
    }

    @Override
    public IModel setGson(String ins) {
        Gson gson = new GsonBuilder().setDateFormat(Defines.DATE_FORMAT).create();
        return gson.fromJson(ins, MdlDates.class);

    }

    public List<Date> getDates() {
        return dates;
    }

    public List<String> getUsed() {
        return used;
    }

}
