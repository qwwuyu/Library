package com.qwwuyu.lib.javaUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成dimens适配屏幕大小和文字
 * Created by qiwei on 2018/3/16 12:29
 */
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
            throw new RuntimeException("res path error");
        }
    }

    @Test
    public void gen() throws Exception {
        for (int i = 0; i < Size.values().length; i++) {
            Size size = Size.values()[i];
            File dir = new File(resPath, "values-" + size.name);
            dir.mkdir();
            File file = new File(dir, "dimens-s.xml");
            if (file.exists() || file.createNewFile()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writeWithLine(writer, "<resources>");
                    for (int j = 5; j <= 128; j++) {
                        BigDecimal bd1 = new BigDecimal(size.spStartX).divide(new BigDecimal(size.spStartY), 5, BigDecimal.ROUND_HALF_UP);
                        BigDecimal bd2 = new BigDecimal(size.spUpX).multiply(new BigDecimal(j - 5)).divide(new BigDecimal(size.spUpY), 5, BigDecimal.ROUND_HALF_UP);
                        String sp = String.valueOf(bd1.add(bd2).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
                        writeWithLine(writer, String.format(Locale.getDefault(), "    <dimen name=\"txt%d\">%ssp</dimen>", j, sp));
                    }
                    writer.newLine();
                    for (int j = 0; j <= 1024; j++) {
                        String dp = String.valueOf(new BigDecimal(size.dimX).multiply(new BigDecimal(j)).divide(new BigDecimal(size.dimY), 1, BigDecimal.ROUND_HALF_UP).floatValue());
                        writeWithLine(writer, String.format(Locale.getDefault(), "    <dimen name=\"dim%d\">%sdp</dimen>", j, dp));
                    }
                    writeWithLine(writer, "</resources>");
                }
                System.out.println("true=" + file.getPath());
            } else {
                System.out.println("false=" + file.getPath());
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
        HDPI("hdpi", 50, 24, 10, 24, 10, 24),
        LDPI("ldpi", 5, 3, 1, 3, 1, 3),
        MDPI("mdpi", 15, 8, 3, 8, 3, 8),
        XHDPI("xhdpi", 5, 2, 1, 2, 1, 2),
        XXHDPI("xxhdpi", 5, 2, 1, 2, 1, 2),
        LARGE("large", 380, 100, 44, 100, 2, 3),
        XLARGE("xlarge", 550, 100, 46, 100, 1, 1),;

        public String name;
        public int spStartX, spStartY, spUpX, spUpY, dimX, dimY;

        Size(String name, int spStartX, int spStartY, int spUpX, int spUpY, int dimX, int dimY) {
            this.name = name;
            this.spStartX = spStartX;
            this.spStartY = spStartY;
            this.spUpX = spUpX;
            this.spUpY = spUpY;
            this.dimX = dimX;
            this.dimY = dimY;
        }
    }

    @Test
    public void encode() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("1.txt").getFile());
        int sn = 0;
        float vn = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String read;
            while ((read = reader.readLine()) != null) {
                Pattern compile = Pattern.compile("[^\"]*\"txt([^\"]+)\">([^s]+)sp</dimen>");
                Matcher matcher = compile.matcher(read);
                if (matcher.find()) {
                    int n = Integer.parseInt(matcher.group(1));
                    float v = Float.parseFloat(matcher.group(2));
                    if (sn == 0) {
                        sn = n;
                        vn = v;
                    } else {
                        System.out.println("txt=" + n + " = " + ((v - vn) / (n - sn)));
                    }
                }
                compile = Pattern.compile("[^\"]*\"dim([^\"]+)\">([^s]+)dp</dimen>");
                matcher = compile.matcher(read);
                if (matcher.find()) {
                    int n = Integer.parseInt(matcher.group(1));
                    float v = Float.parseFloat(matcher.group(2));
                    if (n != 0) {
                        System.out.println("dim=" + n + " = " + (v / n));
                    }
                }
            }
        }
    }
}