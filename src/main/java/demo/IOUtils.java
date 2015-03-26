package demo;

import java.io.IOException;

public class IOUtils {
    public static String getResourceAsString(String path) {
        try {
            return org.apache.commons.io.IOUtils.toString(IOUtils.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
