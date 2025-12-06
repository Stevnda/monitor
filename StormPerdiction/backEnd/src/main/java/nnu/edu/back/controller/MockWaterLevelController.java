package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.Station;
import nnu.edu.back.service.WaterwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Mock controller for water level data
 * Used when external water level service is not available
 */
@RestController
@RequestMapping("/mock/waterLevel")
public class MockWaterLevelController {

    @Autowired
    private WaterwayService waterwayService;

    /**
     * Mock water level data by station and time range
     */
        @RequestMapping(value = "/{prefix}/getInfoByStationAndTime/{station}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getWaterLevelByStationAndTime(
            @PathVariable String prefix,
            @PathVariable String station,
            @PathVariable String startTime,
            @PathVariable String endTime) {

        // Return mock water level data in the same format as real service
        JSONArray data = new JSONArray();

        // Check if the station should return null data for testing purposes
        // Handle both Chinese names and possible encodings
        try {
            String decodedStation = java.net.URLDecoder.decode(station, "UTF-8");
            if (decodedStation.equals("南京") || decodedStation.equals("nanjing") || decodedStation.equals("nj") ||
                decodedStation.equals("天生港") || decodedStation.equals("tianshenggang") || decodedStation.equals("tsg") ||
                decodedStation.equals("江阴") || decodedStation.equals("jiangyin") || decodedStation.equals("jy")) {
                
                // Return empty array data for testing
                JSONObject response = new JSONObject();
                response.put("data", new JSONArray());
                return ResultUtils.success(response);
            }
        } catch (Exception e) {
            // If decoding fails, continue with normal processing
            System.out.println("Failed to decode station name: " + e.getMessage());
        }

        // Parse the time range
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long diffInMillis = Math.abs(end.getTime() - start.getTime());
            long diffInHours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            // Ensure we have at least some data points, but not too many
            long interval = Math.max(1, diffInHours / 20); // Max 20 data points

            Calendar cal = Calendar.getInstance();
            cal.setTime(start);

            // Generate data points within the requested time range
            while (!cal.getTime().after(end)) {
                JSONObject point = new JSONObject();
                point.put("time", sdf.format(cal.getTime()));

                // Generate realistic water level values with daily pattern
                // Different stations might have different base levels
                double baseLevel = 10.0 + (station.hashCode() % 10); // Different base levels for different stations
                double variation = 4.0 * Math.sin(2 * Math.PI * (cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE)/60.0) / 24.0);
                double randomFactor = 1.5 * (Math.random() - 0.5);
                double level = baseLevel + variation + randomFactor;

                // Add the required fields
                point.put("waterLevel", Math.round(level * 100.0) / 100.0); // Round to 2 decimal places
                point.put("upstreamWaterLevel", Math.round((level + 0.5 * (Math.random() - 0.5)) * 100.0) / 100.0);
                point.put("downstreamWaterLevel", Math.round((level + 0.5 * (Math.random() - 0.5)) * 100.0) / 100.0);
                data.add(point);

                cal.add(Calendar.HOUR, (int)interval);
            }
        } catch (Exception e) {
            // If there's any error with date parsing, provide some default data
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 10; i++) {
                JSONObject point = new JSONObject();
                point.put("time", sdf.format(cal.getTime()));
                point.put("waterLevel", 15.0 + (Math.random() - 0.5) * 2.0);
                point.put("upstreamWaterLevel", 15.0 + (Math.random() - 0.5) * 2.0);
                point.put("downstreamWaterLevel", 15.0 + (Math.random() - 0.5) * 2.0);
                data.add(point);
                cal.add(Calendar.HOUR, 1);
            }
        }

        // Return in the same format as the real service
        System.out.println(data);
        
        // 包装成与真实服务相同的格式
        JSONObject response = new JSONObject();
        response.put("data", data);
        return ResultUtils.success(response);
    }


    /**
     * Mock prediction data for a specific station
     */
    @RequestMapping(value = "/prediction/getPrediction/{name}", method = RequestMethod.GET)
    public JsonResult getPredictionValue(@PathVariable String name) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        // Generate mock prediction data for next 24 hours
        JSONArray values = new JSONArray();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < 24; i++) { // 24-hour prediction
            JSONObject point = new JSONObject();
            cal.add(Calendar.HOUR, 1);
            point.put("time", sdf.format(cal.getTime()));

            // Generate realistic prediction values
            double baseLevel = 15.0;
            double variation = 5.0 * Math.sin(2 * Math.PI * cal.get(Calendar.HOUR_OF_DAY) / 24.0); // Daily variation
            double randomFactor = 2.0 * (Math.random() - 0.5); // Random variation
            double level = baseLevel + variation + randomFactor;

            point.put("level", Math.max(5.0, Math.min(25.0, level)));
            values.add(point);
        }

        data.put("station", name);
        data.put("predictions", values);
        response.put("data", data);
        response.put("code", 0);

        return ResultUtils.success(response);
    }

    /**
     * Mock prediction data for all stations
     */
    @RequestMapping(value = "/prediction/getAllPrediction", method = RequestMethod.GET)
    public JsonResult getAllPredictionValue() {
        JSONObject response = new JSONObject();
        JSONArray data = new JSONArray();

        // Get real station names from database
        List<Station> predictionStations = waterwayService.getPredictionStation();
        List<String> stationNames = new ArrayList<>();

        if (predictionStations != null && !predictionStations.isEmpty()) {
            for (Station station : predictionStations) {
                stationNames.add(station.getName());
            }
        } else {
            // Fallback to default station names if none found
            stationNames = Arrays.asList("Station A", "Station B", "Station C", "Station D", "Station E");
        }

        // Current time for generating predictions
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Generate mock prediction data for stations
        for (String stationName : stationNames) {
            JSONObject stationData = new JSONObject();
            JSONArray values = new JSONArray();

            Calendar predictionTime = (Calendar) cal.clone();
            for (int i = 0; i < 24; i++) {
                JSONObject point = new JSONObject();
                predictionTime.add(Calendar.HOUR, 1);
                point.put("time", sdf.format(predictionTime.getTime()));

                // Generate realistic prediction values with some variation per station
                double baseLevel = 15.0 + 2.0 * (stationNames.indexOf(stationName) - stationNames.size()/2.0) / stationNames.size();
                double variation = 5.0 * Math.sin(2 * Math.PI * predictionTime.get(Calendar.HOUR_OF_DAY) / 24.0);
                double randomFactor = 2.0 * (Math.random() - 0.5);
                double level = baseLevel + variation + randomFactor;

                point.put("level", Math.max(5.0, Math.min(25.0, level)));
                values.add(point);
            }

            stationData.put("station", stationName);
            stationData.put("predictions", values);
            data.add(stationData);
        }

        response.put("data", data);
        response.put("code", 0);

        return ResultUtils.success(response);
    }
}