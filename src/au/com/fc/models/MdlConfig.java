package au.com.fc.models;

import au.com.fc.utils.Defines;
import com.google.gson.Gson;

import java.io.*;

/**
 * @author Frank Cusmano
 */
public class MdlConfig implements IModel {


    private String copyright = "Frank Cusmano copyright 2015";
    /**
     * the park ID. If blank, not an owner
     */
    private String parkId;
    private String name;

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void save() throws IOException {
        File fil = new File(Defines.downDir, Defines.FP_CONF);
        FileWriter fOut = new FileWriter(fil);
        fOut.write(getGson());
        fOut.close();
    }

    @Override
    public String getGson() {
        Gson gson = new Gson();
        return gson.toJson(this);

    }
    @Override
    public IModel setGson(String ins) {
        Gson gson = new Gson();
        return gson.fromJson(ins, MdlConfig.class);

    }

    public static MdlConfig load() {
        Gson gson = new Gson();
        File fil = new File(Defines.downDir, Defines.FP_CONF);
        try {
            return gson.fromJson(new FileReader(fil), MdlConfig.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * check to see if first time thru.
     *
     * @return true if not yet configured.
     */
    public static boolean isConfigured() {
        return new File(Defines.downDir, Defines.FP_CONF).exists();
    }


    /**
     * check to see if owner or user.
     *
     * @return true if owner.
     */
    public boolean isOwner() {
        return parkId != null && !parkId.isEmpty();
    }

    public String getParkId() {
        return parkId;
    }

    public String getName() {
        return name;
    }
}
