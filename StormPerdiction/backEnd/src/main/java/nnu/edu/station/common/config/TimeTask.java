//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nnu.edu.station.common.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import nnu.edu.station.common.utils.ClawingUtil;
import nnu.edu.station.common.utils.FieldUtil;
import nnu.edu.station.common.utils.UpdateUtil;
import nnu.edu.station.service.LevelService;
import nnu.edu.station.service.NCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeTask {
    private static final Logger log = LoggerFactory.getLogger(TimeTask.class);
    @Value("${python}")
    String python;
    @Value("${webdriver}")
    String webdriver;
    @Value("${updateData}")
    String updateData;
    @Value("${updateManData}")
    String updateManData;
    @Value("${dataprocess}")
    String dataprocess;
    @Value("${clawingCloud}")
    String clawingCloud;
    @Value("${clawingRadar}")
    String clawingRadar;
    @Value("${clawingRainfall}")
    String clawingRainfall;
    @Value("${clawingRainfallpre}")
    String clawingRainfallpre;
    @Value("${clawingTyphoon}")
    String clawingTyphoon;
    @Value("${deleteClawingData}")
    String deleteClawingData;
    @Value("${meteorology.file.cloud}")
    String cloudfile;
    @Value("${meteorology.file.radar}")
    String radarfile;
    @Value("${meteorology.file.rainfall}")
    String rainfallfile;
    @Value("${meteorology.file.rainfallpre}")
    String rainfallprefile;
    @Value("${meteorology.db}")
    String meteorologydb;
    @Value("${FieldGenerating}")
    String fieldGenerating;
    @Value("${FieldPath}")
    String field_path;
    @Value("${AddFieldGenerating}")
    String addFieldGenerating;
    @Value("${WaterLevelCalculating}")
    String waterLevelCalculating;
    @Value("${AddField}")
    String addField;
    @Value("${AddFieldMask}")
    String addFieldMask;
    @Value("${WaterLevel}")
    String WaterLevel;
    @Value("${WaterLevelPoints}")
    String waterLevelPoints;
    @Value("${DataProcessLog}")
    String logPath;
    @Autowired
    LevelService levelService;
    @Autowired
    NCService ncService;

    public TimeTask() {
    }

    @Scheduled(
            cron = "0 30 10 * * ?"
    )
    public void executePythonUpdateData() {
        UpdateUtil.DataUpdating(this.python, this.updateData, this.dataprocess);
    }

    @Scheduled(
            cron = "01 00 0/3 * * ?"
    )
    public void executePythonUpdateManData() {
        UpdateUtil.ManuelDataUpdating(this.python, this.updateManData, this.dataprocess);
    }

    @Scheduled(
            cron = "0 10 */2 * * ?"
    )
    public void executePythonClawingCloudData() {
        ClawingUtil.ClawingCloudData(this.python, this.clawingCloud, this.meteorologydb, this.cloudfile, this.webdriver);
    }

    @Scheduled(
            cron = "0 30 */2 * * ?"
    )
    public void executePythonClawingRadarData() {
        ClawingUtil.ClawingRadarData(this.python, this.clawingRadar, this.meteorologydb, this.radarfile, this.webdriver);
    }

    @Scheduled(
            cron = "0 50 */2 * * ?"
    )
    public void executePythonClawingRainfallData() {
        ClawingUtil.ClawingRainfallData(this.python, this.clawingRainfall, this.meteorologydb, this.rainfallfile, this.webdriver);
    }

    @Scheduled(
            cron = "0 20 0 * * ?"
    )
    public void executePythonClawingRainfallpreData() {
        ClawingUtil.ClawingRainfallpreData(this.python, this.clawingRainfallpre, this.meteorologydb, this.rainfallprefile, this.webdriver);
    }

    @Scheduled(
            cron = "00 00 00 * * ?"
    )
    public void executePythonDeleteClawingData() {
        ClawingUtil.DeleteClawingData(this.python, this.deleteClawingData);
    }

    @Scheduled(
            cron = "0 0 1 * * ?"
    )
    public void executePythonFieldProcessingData() throws IOException {
        LocalDateTime time = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time_str = time.format(formatter);
        Integer iftyph = this.levelService.ifTyph(time_str);
        String acdirc_path = '"' + this.ncService.getPathByTimeAndType(time_str, "adcirc") + '"';
        String fort_path = '"' + this.ncService.getPathByTimeAndType(time_str, "fort63") + '"';
        FieldUtil.executePythonCalculateWaterLevel(this.python, this.waterLevelCalculating, acdirc_path, this.WaterLevel, this.waterLevelPoints);
        FieldUtil.executePythonFieldGenerating(this.python, this.fieldGenerating, acdirc_path, this.field_path);
        if (iftyph == 1) {
            FieldUtil.executePythonTxtBuilder4add(this.python, this.addFieldGenerating, acdirc_path, fort_path, this.addField, this.addFieldMask);
        }

        PrintWriter writer = new PrintWriter(new FileWriter(this.logPath, true));
        writer.println("Log message: Field data processed successfully at " + LocalDateTime.now());
        System.out.println("Log message: Field data processed successfully at " + LocalDateTime.now());
    }
}
