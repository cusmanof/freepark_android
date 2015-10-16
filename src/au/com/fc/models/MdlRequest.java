package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * @author Frank Cusmano
 */
public class MdlRequest implements IModel {
    private String cmd = "req";
    private final  Date date;
    private final String userId;

    public MdlRequest(Date date, String userId) {
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
        return gson.fromJson(ins, MdlRequest.class);
    }
}
