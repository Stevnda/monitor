package nnu.edu.back.common.utils;

import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/22:04
 * @Description:
 */
@Slf4j
@Component
public class ProcessUtil {

//    static String pythonDir = "E:/monitor/pythonDir/";
    static String pythonStr = "/root/miniconda3/envs/Env1/bin/python";

    static String pythonDir = "/usr/local/resource/monitor_pythonDir/";
//    static String pythonDir = "E:/monitor/pythonDir/";
//    static String pythonStr = "C:/Users/22171/.conda/envs/Env1/python";

    static String cmdStr = "bash";
//    static String cmdStr = "cmd.exe";

    public static Process cmdShp2Pgsql(List<String> commands) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static void readProcessOutput(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GBK")));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("-end");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Process saveSectionValue(String tempPath, String rasterPath, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPath + "\n");
            out.write(resultPath + "\n");
            out.write(jsonArray.size() + "\n");
            for(int i = 0; i < jsonArray.size(); i++) {
                out.write(jsonArray.getObject(i, JSONArray.class).getString(0) + "," + jsonArray.getObject(i, JSONArray.class).getString(1) + "\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        log.info(pythonStr + " " + pythonDir + "section.py " + tempPath);
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add(pythonStr);
        commands.add(pythonDir + "section.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process savaSectionContrast(String tempPath, List<String> rasterPathList, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPathList.size() + "\n");
            for(String path : rasterPathList) {
                out.write(path + "\n");
            }
            out.write(resultPath + "\n");
            out.write(jsonArray.size() + "\n");
            for(int i = 0; i < jsonArray.size(); i++) {
                out.write(jsonArray.getObject(i, JSONArray.class).getString(0) + "," + jsonArray.getObject(i, JSONArray.class).getString(1) + "\n");
            }
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        log.info(pythonStr + " " + pythonDir + "SectionContrast.py " + tempPath);
        commands.add(pythonStr);
        commands.add(pythonDir + "SectionContrast.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process sectionFlush(String tempPath, String benchmarkPath, String referPath, String demPath, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(benchmarkPath + "\n");
            out.write(referPath + "\n");
            out.write(demPath + "\n");
            out.write(resultPath + "\n");
            out.write(jsonArray.size() + "\n");
            for(int i = 0; i < jsonArray.size(); i++) {
                out.write(jsonArray.getObject(i, JSONArray.class).getString(0) + "," + jsonArray.getObject(i, JSONArray.class).getString(1) + "\n");
            }
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        log.info(pythonStr + " " + pythonDir + "section_flush.py " + tempPath);
        commands.add(pythonStr);
        commands.add(pythonDir + "section_flush.py");
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process computeVolume(String tempPath, double deep, String rasterPath, String resultPath, String visualPath, JSONArray jsonArray, String volumePath, String coorPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(deep + "\n");
            out.write(jsonArray.getJSONArray(0).size() - 1 + "\n");
            for(int i = 0; i < jsonArray.getJSONArray(0).size() - 1; i++) {
                out.write(jsonArray.getJSONArray(0).getJSONArray(i).getString(0) + "," + jsonArray.getJSONArray(0).getJSONArray(i).getString(1) + "\n");
            }
            out.write(rasterPath + "\n");
            out.write(resultPath + "\n");
            out.write(visualPath + "\n");
            out.write(volumePath + "\n");
            out.write(coorPath + "\n");
            out.close();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();

        commands.add(pythonStr);
        commands.add(pythonDir + "compute_volume.py");
        commands.add(tempPath);
        log.info(pythonStr + " " + pythonDir + "compute_volume.py " + tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process rasterCrop(String tempPath, String rasterPath, String outputPng, String outputTif, String outputJson, JSONArray jsonArray) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPath + "\n");
            out.write(outputPng + "\n");
            out.write(outputJson + "\n");
            out.write(outputTif + "\n");
            out.write(jsonArray.getJSONArray(0).size() - 1 + "\n");
            for(int i = 0; i < jsonArray.getJSONArray(0).size() - 1; i++) {
                out.write(jsonArray.getJSONArray(0).getJSONArray(i).getString(0) + "," + jsonArray.getJSONArray(0).getJSONArray(i).getString(1) + "\n");
            }
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add(pythonStr);
        commands.add(pythonDir + "raster_clip.py");
        commands.add(tempPath);
        log.info(pythonStr + " " + pythonDir + "raster_clip.py " + tempPath);
        processBuilder.command(commands);
        return processBuilder.start();

    }
}
