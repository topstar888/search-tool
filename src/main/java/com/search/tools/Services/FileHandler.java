/**************************************************************************************************
 * Copyright (c) 2019. Abu all rights reserved.                                                   *
 **************************************************************************************************/

package com.search.tools.Services;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileHandler {

    public static void test2() throws IOException {
        byte[]              buf = new byte[4096];
        java.io.InputStream fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get("test.txt"));

        // (1)
        UniversalDetector detector = new UniversalDetector();

        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();

        // (4)
        String encoding = detector.getDetectedCharset();
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
        } else {
            System.out.println("No encoding detected.");
        }

        // (5)
        detector.reset();
    }
}
