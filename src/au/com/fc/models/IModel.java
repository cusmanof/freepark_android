package au.com.fc.models;

import java.io.IOException;

/**
 * @author Frank Cusmano
 */
public interface IModel {

    void save() throws IOException;
    String getGson();
    IModel setGson(String ins);
}
