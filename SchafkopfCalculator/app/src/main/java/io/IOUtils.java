package io;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Matthias on 09.02.2018.
 */

public class IOUtils {

    public static void writeToFile(FileOutputStream fos, String content) {
        try {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
