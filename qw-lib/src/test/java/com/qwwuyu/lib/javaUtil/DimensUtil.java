package com.qwwuyu.lib.javaUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class DimensUtil {
    private File resPath = null;

    @Before
    public void before() {
        if (resPath == null) {
            String path = getClass().getResource("").getPath();
            String end = "build/intermediates/classes/test/debug/" +
                    getClass().getPackage().getName().replace(".", "/") + "/";
            if (path.endsWith(end)) {
                resPath = new File(path.substring(0, path.length() - end.length()) + "src/main/res");
            }
        }
        if (resPath == null || !resPath.exists()) {
            throw new RuntimeException("path error");
        }
    }

    @Test
    public void main() throws Exception {
        for (int i = 0; i < Size.values().length; i++) {
            Size size = Size.values()[i];
            File dir = new File(resPath, "values-" + size.name);
            dir.mkdir();
            File file = new File(dir, "dimens-s.xml");
            if (file.createNewFile()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writeWithLine(writer, "<resources>");
                    for (int j = 5; j <= 128; j++) {
                        String sp = String.valueOf((int) ((size.start_sp + size.up_sp * (j - 5)) * 10 + 0.5f) / 10f);
                        writeWithLine(writer, String.format(Locale.getDefault(), "    <dimen name=\"txt%d\">%ssp</dimen>", j, sp));
                    }
                    writer.newLine();
                    for (int j = 0; j <= 1024; j++) {
                        String dp = String.valueOf((int) (size.up_dim * j * 10 + 0.5f) / 10f);
                        writeWithLine(writer, String.format(Locale.getDefault(), "    <dimen name=\"dim%d\">%sdp</dimen>", j, dp));
                    }
                    writeWithLine(writer, "</resources>");
                }
            }
        }
    }

    private void writeWithLine(BufferedWriter writer, String str) throws Exception {
        writer.write(str);
        writer.newLine();
    }

    @After
    public void after() {
    }

    private enum Size {
        HDPI("hdpi", 2.1f, 0.4f, 0.4f);

        public String name;
        public float start_sp, up_sp, up_dim;

        Size(String name, float start_sp, float up_sp, float up_dim) {
            this.name = name;
            this.start_sp = start_sp;
            this.up_sp = up_sp;
            this.up_dim = up_dim;
        }
    }
}