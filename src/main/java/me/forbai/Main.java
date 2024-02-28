package me.forbai;

import java.io.*;
import java.util.Base64;

public class Main {

    public static String hwid;
    public static String correctHwid = "none";
    public static boolean isHwidCorrect = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Getting HWID for license");
        File file = new File("/proc/cpuinfo");
        FileInputStream fileIn = new FileInputStream(file);
        hwid = Base64.getEncoder().encodeToString(fileIn.readAllBytes());
        Thread setThread = new Thread(() -> {
            isHwidCorrect = hwid.equals(correctHwid);
        }, "check-hwid-thread");
        setThread.start();
        if (isHwidCorrect) {
            System.out.println("You can now do something funny!");

        } else {
            System.out.println("ERRRRR Wrong HWID, Please verify yor hwid!");
            System.exit(-1);
        }
    }
}