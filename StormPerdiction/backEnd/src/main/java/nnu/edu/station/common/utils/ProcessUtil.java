//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nnu.edu.station.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.station.common.exception.MyException;
import nnu.edu.station.common.result.ProcessCmdOutput;
import nnu.edu.station.common.result.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessUtil {

    public static ProcessCmdOutput readProcessOutput(InputStream inputStream) {
        ProcessCmdOutput var2;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GBK")));
            StringBuilder cmdOutput = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
                cmdOutput.append(line);
            }

            System.out.println("-end");
            return ProcessCmdOutput.ok(cmdOutput.toString());
        } catch (IOException var14) {
            var2 = ProcessCmdOutput.error(var14);
        } finally {
            try {
                inputStream.close();
            } catch (IOException var13) {
                log.info(var13.getMessage(), var13.toString());
            }

        }

        return var2;
    }

    public static Process exeProcess(String commands) throws IOException {
        String osName = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        if (osName.contains("windows")) {
            processBuilder.command("cmd.exe", "/C", commands);
        } else if (osName.contains("linux")) {
            processBuilder.command("/bin/sh", "-c", commands);
        }

        return processBuilder.start();
    }

    public static void run(String cmdString, String taskName) {
        try {
            ProcessUtil.log.info("{} scheduled at {}", taskName, LocalDateTime.now());
            Process process = ProcessUtil.exeProcess(cmdString);
            ProcessCmdOutput processCmdOutput = ProcessUtil.readProcessOutput(process.getInputStream());
            ProcessUtil.log.info(processCmdOutput.toString());
            if (processCmdOutput.getStatusCode() == 0) {
                ProcessUtil.log.info("{} execution failed!", taskName);
            }
            int code = process.waitFor();
            process.destroy();
            if (code == 0) {
                ProcessUtil.log.info("{} execution successfully!", taskName);
            } else {
                ProcessUtil.log.info("{} execution ended not normally!", taskName);
            }

        } catch (InterruptedException | IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}
