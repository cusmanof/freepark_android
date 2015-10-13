package au.com.fc.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class MdlRelease implements IModel {
    private String cmd = "rel";
    private final  Date date;
    private final String user;

    public MdlRelease(Date date, String user) {
        this.date = date;
        this.user = user;
    }


    @Override
    public String getGson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

    @Override
    public IModel setGson(String ins) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(ins, MdlRelease.class);
    }
}
