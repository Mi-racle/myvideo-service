package com.java.example.myvideo.encoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Encoder {
    public void encode(String srcPath, String dstPath, String rate) {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add(srcPath);
        command.add("-s");
        command.add(rate);
        command.add(dstPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.redirectErrorStream(true);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
