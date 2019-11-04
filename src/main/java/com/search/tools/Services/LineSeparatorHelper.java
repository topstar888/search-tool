/**************************************************************************************************
 * Copyright (c) 2019. Abu all rights reserved.                                                   *
 **************************************************************************************************/

package com.search.tools.Services;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class LineSeparatorHelper {

    public enum LINE_SEPARATOR {
        WINDOWS, LINUX, MAC, UNKNOWN
    }

    private LineSeparatorHelper() {
    }

    public static LINE_SEPARATOR getLineSeparator(File f) throws IllegalArgumentException {
        if (f == null || !f.isFile() || !f.exists()) {
            throw new IllegalArgumentException("file must exists!");
        }

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "r");
            String line = raf.readLine();
            if (line == null) {
                return LINE_SEPARATOR.UNKNOWN;
            }

            // 必须执行这一步，因为 RandomAccessFile 的 readLine() 会自动忽略并跳过换行符，所以需要先回退文件指针位置
            // "ISO-8859-1" 为 RandomAccessFile 使用的字符集，此处必须指定，否则中文 length 获取不对
            raf.seek(line.getBytes("ISO-8859-1").length);

            byte nextByte = raf.readByte();
            if (nextByte == 0x0A) {
                return LINE_SEPARATOR.LINUX;
            }

            if (nextByte != 0x0D) {
                return LINE_SEPARATOR.UNKNOWN;
            }

            try {
                nextByte = raf.readByte();
                if (nextByte == 0x0A) {
                    return LINE_SEPARATOR.WINDOWS;
                }
                return LINE_SEPARATOR.MAC;
            } catch (EOFException e) {
                return LINE_SEPARATOR.MAC;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return LINE_SEPARATOR.UNKNOWN;
    }

}