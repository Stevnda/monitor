package nnu.edu.back.controller;

import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.SensorAdcp;
import nnu.edu.back.pojo.SensorChaoweiyi;
import nnu.edu.back.pojo.SensorLuojing;
import nnu.edu.back.pojo.SensorZhuoduyi;
import nnu.edu.back.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sensorData")
public class SensorDataController {
    @Autowired
    SensorDataService sensorDataService;
    
    @RequestMapping(value = "/adcp/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAdcpData(@PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(sensorDataService.getAdcpDataByTimeRange(startTime, endTime));
    }

    @RequestMapping(value = "/adcp/all", method = RequestMethod.GET)
    public JsonResult getAllAdcpData() {
        return ResultUtils.success(sensorDataService.getAllAdcpData());
    }
    
    @RequestMapping(value = "/chaoweiyi/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getChaoweiyiData(
            @PathVariable String startTime,
            @PathVariable String endTime) {
        return ResultUtils.success(sensorDataService.getChaoweiyiDataByStationAndTime(startTime, endTime));
    }

    @RequestMapping(value = "/chaoweiyi/all", method = RequestMethod.GET)
    public JsonResult getAllChaoweiyiData() {
        return ResultUtils.success(sensorDataService.getAllChaoweiyiData());
    }
    
    @RequestMapping(value = "/luojing/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getLuojingData(
            @PathVariable String startTime,
            @PathVariable String endTime) {
        return ResultUtils.success(sensorDataService.getLuojingDataByTimeRange(startTime, endTime));
    }

    @RequestMapping(value = "/luojing/all", method = RequestMethod.GET)
    public JsonResult getAllLuojingData(){
        return ResultUtils.success(sensorDataService.getAllLuojingData());
    }
    
    @RequestMapping(value = "/zhuoduyi/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getZhuoduyiData(
            @PathVariable String startTime,
            @PathVariable String endTime) {
        return ResultUtils.success(sensorDataService.getZhuoduyiDataByTimeRange(startTime, endTime));
    }

    @RequestMapping(value = "/zhuoduyi/all", method = RequestMethod.GET)
    public JsonResult getAllZhuoduyiData(){
        return ResultUtils.success(sensorDataService.getAllZhuoduyiData());
    }
}