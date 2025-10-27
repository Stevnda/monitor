package nnu.edu.back.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.common.utils.InternetUtil;
import nnu.edu.back.dao.map.MapMapper;
import nnu.edu.back.dao.ship.ShipMapper;
import nnu.edu.back.dao.waterway.WaterwayMapper;
import nnu.edu.back.pojo.*;
import nnu.edu.back.service.WaterwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/23:01
 * @Description:
 */
@Service
@Slf4j
public class WaterwayServiceImpl implements WaterwayService {
    @Autowired
    WaterwayMapper waterwayMapper;

    @Autowired
    ShipMapper shipMapper;

    @Autowired
    MapMapper mapMapper;

    @Value("${resourceDir}")
    String resourceDir;

    @Value("${meteorologyUrl}")
    String meteorologyUrl;

    @Value("${meteorologyPngUrl}")
    String meteorologyPngUrl;

    @Value("${visualDir}")
    String visualDir;

    @Value("${waterLevelAddress}")
    String waterLevelAddress;

    @Value("${AISUrl}")
    String AISUrl;

    @Value("${AISListUrl}")
    String AISListUrl;

    @Override
    public List<Anchor> getAnchorInfoByBox(double top, double right, double bottom, double left) {
        List<Anchor> result = new ArrayList<>();
        List<Anchor> anchorList = waterwayMapper.getAllAnchorInfo();
        for (Anchor anchor : anchorList) {
            JSONArray jsonArray = anchor.getRegion().getJSONArray("points");
            double tempTop = jsonArray.getJSONArray(0).getDouble(1);
            double tempBottom = jsonArray.getJSONArray(0).getDouble(1);
            double tempRight = jsonArray.getJSONArray(0).getDouble(0);
            double tempLeft = jsonArray.getJSONArray(0).getDouble(0);
            for (int i = 1; i < jsonArray.size(); i++) {
                double lon = jsonArray.getJSONArray(i).getDouble(0);
                double lat = jsonArray.getJSONArray(i).getDouble(1);
                if (lon > tempRight) {
                    tempRight = lon;
                } else if (lon < tempLeft) {
                    tempLeft = lon;
                }
                if (lat > tempTop) {
                    tempTop = lat;
                } else if (lat < tempBottom) {
                    tempBottom = lat;
                }
            }
            if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                result.add(anchor);
            }
        }
        return result;
    }

    @Override
    public List<Buoy> getBuoyByBox(double top, double right, double bottom, double left) {
        return waterwayMapper.getBuoyByBox(top, right, bottom, left);
    }

    @Override
    public void getPhoto(String fileName, HttpServletResponse response) {
        byte[] bytes = (byte[]) waterwayMapper.getPhoto(fileName);
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream sos = response.getOutputStream();
            int off = 0;
            while (off < bytes.length) {
                if (off + 1024 <= bytes.length) {
                    sos.write(bytes, off, 1024);
                } else {
                    sos.write(bytes, off, bytes.length - off);
                }
                off += 1024;
            }
            sos.flush();
            sos.close();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<Park> getParkInfoByBox(double top, double right, double bottom, double left) {
        List<Park> result = new ArrayList<>();
        List<Park> parkList = waterwayMapper.getAllParkInfo();
        for (Park park : parkList) {
            JSONObject jsonObject = park.getRegion();
            if (jsonObject.getString("type").equals("point")) {
                JSONArray jsonArray = jsonObject.getJSONArray("point");
                double lon = jsonArray.getDouble(0);
                double lat = jsonArray.getDouble(1);
                if (lon > left && lon < right && lat > bottom && lat < top) {
                    result.add(park);
                }
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("points");
                if (jsonArray.size() > 2) {
                    double tempTop = jsonArray.getJSONArray(0).getDouble(1);
                    double tempBottom = jsonArray.getJSONArray(0).getDouble(1);
                    double tempRight = jsonArray.getJSONArray(0).getDouble(0);
                    double tempLeft = jsonArray.getJSONArray(0).getDouble(0);
                    for (int i = 1; i < jsonArray.size(); i++) {
                        double lon = jsonArray.getJSONArray(i).getDouble(0);
                        double lat = jsonArray.getJSONArray(i).getDouble(1);
                        if (lon > tempRight) {
                            tempRight = lon;
                        } else if (lon < tempLeft) {
                            tempLeft = lon;
                        }
                        if (lat > tempTop) {
                            tempTop = lat;
                        } else if (lat < tempBottom) {
                            tempBottom = lat;
                        }
                    }
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                        result.add(park);
                    }
                } else {
                    double tempTop = jsonArray.getJSONArray(0).getDouble(1);
                    double tempRight = jsonArray.getJSONArray(0).getDouble(0);
                    double tempBottom = jsonArray.getJSONArray(1).getDouble(1);
                    double tempLeft = jsonArray.getJSONArray(1).getDouble(0);
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                        result.add(park);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Station> getStationByBox(double top, double right, double bottom, double left) {
        return waterwayMapper.getStationByBox(top, right, bottom, left);
    }

    @Override
    public JSONArray getWaterLevelByStationAndTime(String type, String station, String startTime, String endTime) {
        String prefix;
        if (type.equals("yangtze")) {
            prefix = "YangtzeDownstream";
        } else if (type.equals("hubei")) {
            prefix = "hubei";
        } else if (type.equals("anhui")) {
            prefix = "anhui";
        } else if (type.equals("jiangsu")) {
            prefix = "jiangsu";
        } else if (type.equals("zhejiang")) {
            prefix = "zhejiang";
        } else {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

        try {
            String url = waterLevelAddress + "/" + prefix + "/getInfoByStationAndTime/" + station + "/" + startTime + "/" + endTime;
            url = InternetUtil.encodeSpaceChinese(url, "UTF-8");

//            HttpEntity requestEntity = new HttpEntity(new HttpHeaders());
//            JSONObject res = InternetUtil.httpHandle(url, requestEntity, JSONObject.class, "get");
            JSONObject res = InternetUtil.GetRealData(url);
            return res.getJSONArray("data");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
    }

    @Override
    public List<Station> getAllStation() {
        return waterwayMapper.getAllStation();
    }

    @Override
    public List<Bridge> getAllBridgeInfo() {
        return waterwayMapper.getAllBridgeInfo();
    }

    @Override
    public JSONArray getMeteorology() {
        String path = resourceDir + "meteorology/meteorology.json";
        JSONArray jsonArray = FileUtil.readJsonArray(path);
        List<CompletableFuture> list = new ArrayList<>();
        JSONArray result = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            int code = jsonArray.getJSONObject(i).getIntValue("region");
            String url = meteorologyUrl + "?adcode=" + code;
            CompletableFuture future = CompletableFuture.supplyAsync(() -> {
                try {
                    JSONObject jsonObject = InternetUtil.doGet(url);
                    JSONArray temp = jsonObject.getJSONArray("data");
                    for (int j = 0; j < temp.size(); j++) {
                        File f = new File(resourceDir + "meteorology/png/" + temp.getJSONObject(j).getString("type") + ".png");
                        if (!f.exists()) {
                            String pictureUrl = meteorologyPngUrl + temp.getJSONObject(j).getString("type") + ".png";
                            String pngPath = resourceDir + "meteorology/png/" + temp.getJSONObject(j).getString("type") + ".png";
                            try {
                                InternetUtil.downloadMeteorologyPng(pictureUrl, pngPath);
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                        result.add(temp.getJSONObject(j));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return null;
            });
            list.add(future);
        }
        CompletableFuture all = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
        try {
            all.get();
        } catch (Exception e) {

            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        return result;
    }

    @Override
    public void getMeteorologyPng(String fileName, HttpServletResponse response) {
        String path = resourceDir + "meteorology/png/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            path = resourceDir + "meteorology/png/11B62_YELLOW.png";
        }
        try {
            InputStream inputStream = new FileInputStream(path);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public Map<String, Object> pageList(String type, int page, int size, String keyword) {
        Map<String, Object> map = new HashMap<>();

        if (!keyword.equals("")) keyword = "%" + keyword + "%";
        if (type.equals("ship")) {
            map.put("total", shipMapper.count(keyword));
            List<Map<String, Object>> list = shipMapper.pageQuery(size, size * page, keyword);
            List<Ship> result = new ArrayList<>();
            for (Map<String, Object> m : list) {
                String mmsi = m.get("mmsi").toString();
                String name = m.get("cbmc") == null ? "" : m.get("cbmc").toString();
                String nameCn = m.get("zwmc") == null ? "" : m.get("zwmc").toString();
                String updateTime = "";
                Double longitude = null;
                Double latitude = null;
                String speed = "";
                String course = "";
                String draft = m.get("cbcs") == null ? "" : m.get("cbcs").toString();
                String length = m.get("cd") == null ? "" : m.get("cd").toString();
                String width = m.get("kd") == null ? "" : m.get("kd").toString();
                Ship ship = new Ship(mmsi, name, nameCn, updateTime, longitude, latitude, speed, course, draft, length, width, "0");
                result.add(ship);
            }
            map.put("list", result);
        } else if (type.equals("buoy")) {
            map.put("total", waterwayMapper.countBuoy(keyword));
            map.put("list", waterwayMapper.pageQueryBuoy(size, size * page, keyword));
        } else if (type.equals("park")) {
            map.put("total", waterwayMapper.countPark(keyword));
            map.put("list", waterwayMapper.pageQueryPark(size, size * page, keyword));
        } else if (type.equals("anchor")) {
            map.put("total", waterwayMapper.countAnchor(keyword));
            map.put("list", waterwayMapper.pageQueryAnchor(size, size * page, keyword));
        } else if (type.equals("bridge")) {
            map.put("total", waterwayMapper.countBridge(keyword));
            map.put("list", waterwayMapper.pageQueryBridge(size, size * page, keyword));
        } else if (type.equals("station")) {
            map.put("total", waterwayMapper.countStation(keyword));
            map.put("list", waterwayMapper.pageQueryStation(size, size * page, keyword));
        } else if (type.equals("realShip")) {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Accesstoken", "v7_q1SUWcRpfm2zVhAbjIkmtC6PpcaclC3k");
            String url = MessageFormat.format(AISListUrl, page + 1, keyword, size);
            JSONObject jsonObject;
            try {
                jsonObject = InternetUtil.doGet(url, headerMap);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                map.put("total", jsonObject.getIntValue("totalSize"));
                List<Ship> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    JSONObject json = jsonArray.getJSONObject(i);
                    String mmsi = json.getString("mmsi");
                    String name = json.getString("shipName") == null ? "" : json.getString("shipName");
                    String nameCn = json.getString("chName") == null ? "" : json.getString("chName");
                    String updateTime = json.getLong("utc") == null ? "" : sdf.format(new Date(json.getLongValue("utc")));
                    Double longitude = json.getDouble("nLongitude");
                    Double latitude = json.getDouble("nLatitude");
                    String speed = json.getString("speed") == null ? "" : json.getString("speed");
                    String course = json.getString("course") == null ? "" : json.getString("course");
                    String draft = json.getDouble("draft") == null ? "" : json.getDouble("draft").toString();
                    String length = json.getDouble("length") == null ? "" : json.getDouble("length").toString();
                    String width = json.getDouble("width") == null ? "" : json.getDouble("width").toString();
                    String classType = json.getString("classType") == null ? "0" : json.getString("classType");
                    Ship ship = new Ship(mmsi, name, nameCn, updateTime, longitude, latitude, speed, course, draft, length, width, classType);
                    list.add(ship);
                }
                map.put("list", list);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        return map;
    }

    @Override
    public void seaChart(String type, String x, String y, String z, HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
            sos = response.getOutputStream();
            byte[] bytes;
            if (type.equals("map")) {
                bytes = (byte[]) mapMapper.getWaterwayMap(x, y, z);
            } else if (type.equals("mark")) {
                bytes = (byte[]) mapMapper.getWaterwayMark(x, y, z);
            } else if (type.equals("yangtze")) {
                bytes = (byte[]) mapMapper.getRasterTile(x, y, z, "yangtzeTiles");
            } else throw new MyException(-99, "type参数错误");
            if (bytes == null) {
                in = new FileInputStream(visualDir + "blank.png");
                byte[] byteArray = new byte[1024];
                int len;
                while ((len = in.read(byteArray)) > -1) {
                    sos.write(byteArray, 0, len);
                }
                sos.flush();
                sos.close();
                in.close();
            } else {
                int off = 0;
                while (off < bytes.length) {
                    if (off + 1024 <= bytes.length) {
                        sos.write(bytes, off, 1024);
                    } else {
                        sos.write(bytes, off, bytes.length - off);
                    }
                    off += 1024;
                }
                sos.flush();
                sos.close();
            }


        } catch (Exception e) {
            log.error(e.getMessage());
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }


    @Override
    public List<Ship> getShipInfoByBoxAndTime(double top, double right, double bottom, double left, String startTime, String endTime) {
        String tableName = "locus20221207";
        startTime = "2022-12-07 " + startTime;
        endTime = "2022-12-07 " + endTime;
        List<Map<String, Object>> list = shipMapper.getShipInfoByBoxAndTime(tableName, top, right, bottom, left, startTime, endTime);
        List<Ship> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String mmsi = map.get("mmsi").toString();
            String name = map.get("name") == null ? "" : map.get("name").toString();
            String nameCn = map.get("name_cn") == null ? "" : map.get("name_cn").toString();
            String updateTime = map.get("update_time") == null ? "" : map.get("update_time").toString();
            Double longitude = (Double) map.get("lon");
            Double latitude = (Double) map.get("lat");
            String speed = map.get("velocity") == null ? "" : map.get("velocity").toString();
            String course = map.get("direction") == null ? "" : map.get("direction").toString();
            String draft = map.get("cbcs") == null ? "" : map.get("cbcs").toString();
            String length = map.get("length") == null ? "" : map.get("length").toString();
            String width = map.get("width") == null ? "" : map.get("width").toString();
            Ship ship = new Ship(mmsi, name, nameCn, updateTime, longitude, latitude, speed, course, draft, length, width, "0");
            result.add(ship);
        }
        return result;
    }

    @Override
    public List<Ship> queryBoxShip(double top, double right, double bottom, double left) {
        List<Ship> list = new ArrayList<>();
        JSONObject jsonObject;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accesstoken", "v7_q1SUWcRpfm2zVhAbjIkmtC6PpcaclC3k");
        String url = MessageFormat.format(AISUrl, left, bottom, right, top);
        jsonObject = InternetUtil.doGet(url, headerMap);
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        }

        if (jsonObject.getIntValue("status") == 200) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONObject json = jsonArray.getJSONObject(i);
                String mmsi = json.getString("mmsi");
                String name = json.getString("shipName") == null ? "" : json.getString("shipName");
                String nameCn = json.getString("chName") == null ? "" : json.getString("chName");
                String updateTime = json.getLong("utc") == null ? "" : sdf.format(new Date(json.getLongValue("utc")));
                Double longitude = json.getDouble("nLongitude");
                Double latitude = json.getDouble("nLatitude");
                String speed = json.getString("speed") == null ? "" : json.getString("speed");
                String course = json.getString("course") == null ? "" : json.getString("course");
                String draft = json.getDouble("draft") == null ? "" : json.getDouble("draft").toString();
                String length = json.getDouble("length") == null ? "" : json.getDouble("length").toString();
                String width = json.getDouble("width") == null ? "" : json.getDouble("width").toString();
                String classType = json.getString("classType") == null ? "0" : json.getString("classType");
                Ship ship = new Ship(mmsi, name, nameCn, updateTime, longitude, latitude, speed, course, draft, length, width, classType);
                list.add(ship);
            }
        } else {
            throw new MyException(-99, "接口请求过于频繁");
        }

        return list;
    }

    @Override
    public List<Station> getPredictionStation() {
        return waterwayMapper.getPredictionStation();
    }

    @Override
    public JSONObject getPredictionValue(String name) {
        String url = waterLevelAddress + "/prediction/getPrediction/" + name;
        JSONObject jsonObject = InternetUtil.GetRealData(url);
        if (jsonObject.getIntValue("code") == 0) {
            return jsonObject.getJSONObject("data");
        } throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
    }

    @Override
    public JSONArray getAllPredictionValue() {
        String url = waterLevelAddress + "/prediction/getAllPrediction";
        JSONObject jsonObject = InternetUtil.GetRealData(url);
        if (jsonObject.getIntValue("code") == 0) {
            return jsonObject.getJSONArray("data");
        } throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
    }
}
