package au.com.fc.models;

import java.io.IOException;

/**
 * the unusual structure in the implementing classes are so it makes it easier for gson.
 *
 * @author Frank Cusmano
 */
public interface IModel {
    String getGson();
    IModel setGson(String ins);
}
