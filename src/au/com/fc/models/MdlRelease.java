package au.com.fc.models;

import au.com.fc.utils.Defines;
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
    private final String userId;

    public MdlRelease(Date date, String userId) {
        this.date = date;
        this.userId = userId;
    }


    @Override
    public String getGson() {
        Gson gson = new GsonBuilder().setDateFormat(Defines.DATE_FORMAT).create();
        return gson.toJson(this);
    }

    @Override
    public IModel setGson(String ins) {
        Gson gson = new GsonBuilder().setDateFormat(Defines.DATE_FORMAT).create();
        return gson.fromJson(ins, MdlRelease.class);
    }
}
