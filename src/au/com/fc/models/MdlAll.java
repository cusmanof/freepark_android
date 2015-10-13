package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class MdlAll implements IModel {
    private String cmd = "all";
    private List<String> name = new LinkedList<>();
    private List<String> parkId = new LinkedList<>();
    private List<String> used = new LinkedList<>();
    private List<Date> dates = new LinkedList<>();



    @Override
    public String getGson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

    @Override
    public IModel setGson(String ins) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(ins, MdlAll.class);
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<String> getUsed() {
        return used;
    }

    public List<String> getParkId() {
        return parkId;
    }

    public List<String>  getOwner() {
        return name;
    }
}
