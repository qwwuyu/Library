package com.qwwuyu.lib.javaUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmlStringUtil {
    /** res目录 */
    private File resDir;
    /** 放置目录 */
    private final File results = new File("D:\\qwwuyu\\xml\\res");
    /** 所有strings文件 */
    private ArrayList<File> files = new ArrayList<>();

    @Before
    public void before() {
        if (resDir == null) {
            String path = getClass().getResource("").getPath();
            String end = "build/intermediates/classes/test/debug/" +
                    getClass().getPackage().getName().replace(".", "/") + "/";
            if (path.endsWith(end)) {
                resDir = new File(path.substring(0, path.length() - end.length()) + "src/main/res");
            }
        }
        if (resDir == null || !resDir.exists()) {
            throw new RuntimeException("res path error");
        }
        if (!results.exists()) results.mkdirs();
        File[] fs = resDir.listFiles();
        for (File file : fs) {
            File res = new File(file, "strings.xml");
            if (res.exists()) {
                files.add(file);
                File resultDir = new File(results, file.getName());
                if (!resultDir.exists()) resultDir.mkdir();
            }
        }
    }

    @Test
    public void main() throws Exception {
        checkOneLine();
        compareAll(new File(new File(resDir, "values"), "strings.xml"));
        get(Arrays.asList("key1", "key2"));
        copyToEnd(Arrays.asList("key1", "key2"), Arrays.asList("key3", "key4"), "key1");
    }

    @After
    public void after() {
    }

    /** 复制key为newkeys到start行下 */
    private void copyToEnd(List<String> keys, List<String> newkeys, String start) throws Exception {
        transfer(new CopyTransfer(keys, newkeys, start));
    }

    /** 打包key */
    private void get(List<String> keys) throws Exception {
        transfer(new GetTransfer(keys));
    }

    /** 根据ob格式化所有文件 */
    private void compareAll(File ob) throws Exception {
        for (File file : files) {
            File ex = new File(new File(resDir, file.getName()), "strings.xml");
            if (!ob.getAbsolutePath().equals(ex.getAbsolutePath())) compare(ob, ex);
        }
    }

    /** 检查string只占一行 */
    private void checkOneLine() throws Exception {
        for (File file : files) {
            File res = new File(file, "strings.xml");
            try (BufferedReader reader = new BufferedReader(new FileReader(res))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    i++;
                    if (line.contains("</string>") && !line.contains("<string")) {
                        System.out.println(res.getAbsolutePath() + " : " + i);
                    }
                }
            }
        }
    }

    /** 格式化文件 */
    private void compare(File ex, File ob) throws Exception {
        ArrayList<String> exLines = read(ex);
        ArrayList<String> obLines = read(ob);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ob))) {
            boolean exBegin = false, obBegin = false, array = false;
            for (String exLine : exLines) {
                if (!exBegin && !obBegin) {//处理resources之前
                    for (String obLine : obLines) {
                        writer.write(obLine);
                        writer.newLine();
                        if (obLine.contains("<resources>")) {
                            obBegin = true;
                            break;
                        }
                    }
                } else if (exLine.contains("<resources>")) {//处理resources之前
                    exBegin = true;
                } else if (exLine.contains("</string>")) {//处理string
                    String key = exLine.replaceAll("^[^\"]+\"([^\"]+)\".+$", "$1");
                    boolean have = false;
                    for (int i = 0; i < obLines.size(); i++) {
                        if (obLines.get(i).contains("=\"" + key + "\"") && !obLines.get(i).contains("</string>-->")) {
                            writer.write(obLines.remove(i));
                            writer.newLine();
                            have = true;
                            break;
                        }
                    }
                    if (!have) {
                        System.out.println(exLine);
                        writer.write(exLine + "***");
                        writer.newLine();
                    }
                } else if (exLine.contains("<string-array name=")) {//处理string-array
                    array = true;
                    String key = exLine.replaceAll("^[^\"]+\"([^\"]+)\".+$", "$1");
                    boolean write = false;
                    for (int i = 0; i < obLines.size(); i++) {
                        if (obLines.get(i).contains("=\"" + key + "\"")) {
                            writer.write(obLines.remove(i));
                            i--;
                            writer.newLine();
                            write = true;
                        } else if (write && obLines.get(i).contains("</string-array>")) {
                            writer.write(obLines.remove(i));
                            writer.newLine();
                            break;
                        } else if (write) {
                            writer.write(obLines.remove(i));
                            i--;
                            writer.newLine();
                        }
                    }
                } else if (exLine.contains("</string-array>")) {//处理string-array
                    array = false;
                } else if (!array && exBegin) {//处理其他
                    writer.write(exLine);
                    writer.newLine();
                }
            }
            for (String obLine : obLines) {//处理缺失
                if (obLine.contains("</string>")) {
                    writer.write(obLine);
                    writer.newLine();
                }
            }
        }
    }

    private void transfer(ITransfer transfer) throws Exception {
        for (File file : files) {
            File res = new File(file, "strings.xml");
            File result = new File(new File(results, file.getName()), "strings.xml");
            if (!result.exists()) result.createNewFile();
            transfer.transfer(res, result);
        }
    }

    /** 复制key为newkeys到start行下 */
    public static class CopyTransfer implements ITransfer {
        private List<String> keys;
        private List<String> newkeys;
        private String start;

        public CopyTransfer(List<String> keys, List<String> newkeys, String start) {
            this.keys = keys;
            this.newkeys = newkeys;
            this.start = start;
        }

        @Override
        public void transfer(File res, File result) throws Exception {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
                ArrayList<String> lines = read(res);
                ArrayList<String> keyLine = new ArrayList<>();
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    for (int j = 0; j < lines.size(); j++) {
                        String line = lines.get(j);
                        if (line.contains("=\"" + key + "\"") && line.contains("</string>")) {
                            keyLine.add(line.replaceAll("^([^\"]+\")[^\"]+(\".+$)", "$1" + newkeys.get(i) + "$2"));
                            break;
                        }
                    }
                }
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                    if (l.startsWith(start)) {
                        writer.newLine();
                        for (int i = 0; i < keyLine.size(); i++) {
                            writer.write(keyLine.get(i));
                            writer.newLine();
                        }
                    }
                }
            }
        }
    }

    /** 打包key到指定目录 */
    public static class GetTransfer implements ITransfer {
        private List<String> keys;

        public GetTransfer(List<String> keys) {
            this.keys = keys;
        }

        @Override
        public void transfer(File res, File result) throws Exception {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
                ArrayList<String> lines = read(res);
                for (String line : lines) {
                    if (line.contains("</string>")) {
                        String key = line.replaceAll("^[^\"]+\"([^\"]+)\".+$", "$1");
                        if (keys.contains(key)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }
            }
        }
    }

    private static ArrayList<String> read(File res) throws Exception {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(res))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public interface ITransfer {
        void transfer(File res, File result) throws Exception;
    }
}