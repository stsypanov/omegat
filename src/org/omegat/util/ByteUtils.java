package org.omegat.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by sergei on 14.03.15.
 */
public class ByteUtils {


    // BOM (byte order mark) bugfix
    public static void checkByteOrderMark(BufferedReader in) throws IOException {
        in.mark(1);
        int ch = in.read();
        if (ch != 0xFEFF)
            in.reset();
    }
}
