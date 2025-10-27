package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.Anchor;
import nnu.edu.back.pojo.Buoy;
import nnu.edu.back.service.WaterwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/23:00
 * @Description:
 */
@RestController
@RequestMapping("/waterway")
public class WaterwayController {
    @Autowired
    WaterwayService waterwayService;

    @RequestMapping(value = "/getAnchorInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getAnchorInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(waterwayService.getAnchorInfoByBox(top, right, bottom, left));
    }

    @RequestMapping(value = "/getBuoyByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getBuoyByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(waterwayService.getBuoyByBox(top, right, bottom, left));
    }

    @RequestMapping(value = "/img/{fileName}", method = RequestMethod.GET)
    public void getPhoto(@PathVariable String fileName, HttpServletResponse response) {
        waterwayService.getPhoto(fileName, response);
    }

    @RequestMapping(value = "/getParkInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getParkInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(waterwayService.getParkInfoByBox(top, right, bottom, left));
    }

    @RequestMapping(value = "/getStationByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getStationByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(waterwayService.getStationByBox(top, right, bottom, left));
    }

    @RequestMapping(value = "/getWaterLevelByStationAndTime/{type}/{station}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getWaterLevelByStationAndTime(@PathVariable String type, @PathVariable String station, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(waterwayService.getWaterLevelByStationAndTime(type, station, startTime, endTime));
    }

    @RequestMapping(value = "/getAllStation", method = RequestMethod.GET)
    public JsonResult getAllStation() {
        return ResultUtils.success(waterwayService.getAllStation());
    }

    @RequestMapping(value = "/getAllBridgeInfo", method = RequestMethod.GET)
    public JsonResult getBridgeInfo() {
        return ResultUtils.success(waterwayService.getAllBridgeInfo());
    }

    @RequestMapping(value = "/getMeteorology", method = RequestMethod.GET)
    public JsonResult getMeteorology() {
        return ResultUtils.success(waterwayService.getMeteorology());
    }

    @RequestMapping(value = "/getMeteorologyPng/{fileName}", method = RequestMethod.GET)
    public void getMeteorologyPng(@PathVariable String fileName, HttpServletResponse response) {
        waterwayService.getMeteorologyPng(fileName, response);
    }

    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    public JsonResult pageList(@RequestBody JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        String keyword = jsonObject.getString("keyword");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(waterwayService.pageList(type, page, size, keyword));
    }


    @CrossOrigin
    @RequestMapping(value = "/seaChart/{type}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void seaChart(@PathVariable String type, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        waterwayService.seaChart(type, x, y, z, response);
    }


    /**
     * @Description:作为临时接口，做出船的动态效果
     * @Author: Yiming
     * @Date: 2022/12/7
     */
    @RequestMapping(value = "/getShipInfoByBoxAndTime/{top}/{right}/{bottom}/{left}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getShipInfoByBoxAndTime(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(waterwayService.getShipInfoByBoxAndTime(top, right, bottom, left, startTime, endTime));
    }

    /**
     * @Description:AIS船舶实时接入
     * @Author: Yiming
     * @Date: 2023/3/13
     */
    @RequestMapping(value = "/queryBoxShip/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult queryBoxShip(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(waterwayService.queryBoxShip(top, right, bottom, left));
    }

    @RequestMapping(value = "/getPredictionStation", method = RequestMethod.GET)
    public JsonResult getPredictionStation() {
        return ResultUtils.success(waterwayService.getPredictionStation());
    }

    @RequestMapping(value = "/getPredictionValue/{name}", method = RequestMethod.GET)
    public JsonResult getPredictionValue(@PathVariable String name) {
        return ResultUtils.success(waterwayService.getPredictionValue(name));
    }

    @RequestMapping(value = "/getAllPredictionValue", method = RequestMethod.GET)
    public JsonResult getAllPredictionValue() {
        return ResultUtils.success(waterwayService.getAllPredictionValue());
    }
}
