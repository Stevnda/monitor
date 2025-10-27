package nnu.edu.back.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.pojo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/23:01
 * @Description:
 */
public interface WaterwayService {
    List<Anchor> getAnchorInfoByBox(double top, double right, double bottom, double left);

    List<Buoy> getBuoyByBox(double top, double right, double bottom, double left);

    void getPhoto(String fileName, HttpServletResponse response);

    List<Park> getParkInfoByBox(double top, double right, double bottom, double left);

    List<Station> getStationByBox(double top, double right, double bottom, double left);

    JSONArray getWaterLevelByStationAndTime(String type, String station, String startTime, String endTime);

    List<Station> getAllStation();

    List<Bridge> getAllBridgeInfo();

    JSONArray getMeteorology();

    void getMeteorologyPng(String fileName, HttpServletResponse response);

    Map<String, Object> pageList(String type, int page, int size, String keyword);

    void seaChart(String type, String x, String y, String z, HttpServletResponse response);

    List<Ship> getShipInfoByBoxAndTime(double top, double right, double bottom, double left, String startTime, String endTime);

    List<Ship> queryBoxShip(double top, double right, double bottom, double left);

    List<Station> getPredictionStation();

    JSONObject getPredictionValue(String name);

    JSONArray getAllPredictionValue();
}
