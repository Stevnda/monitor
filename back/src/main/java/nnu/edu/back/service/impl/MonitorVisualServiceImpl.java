package nnu.edu.back.service.impl;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.dao.dynamic.DynamicMapper;
import nnu.edu.back.pojo.*;
import nnu.edu.back.service.MonitorVisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/23/11:03
 * @Description:
 */
@Service
public class MonitorVisualServiceImpl implements MonitorVisualService {
    @Autowired
    DynamicMapper dynamicMapper;

    @Value("${baseDir}")
    String baseDir;

    @Value("${projectDir}")
    String projectDir;

    @Override
    public List<String> getLocusPoint(String projectId) {
        return dynamicMapper.getLocusPoint(projectId);
    }

    @Override
    public List<Locus> getLocusTable(String projectId, String name) {
        return dynamicMapper.getLocusTable(projectId, name);
    }

    @Override
    public JSONObject getLocusShape(String projectId, String name) {
        String address = Paths.get(projectDir, projectId, "out", name + ".geojson").toString();
        return FileUtil.readJson(address);
    }

    @Override
    public List<Section> getAllSection(String projectId) {
        return dynamicMapper.getAllSection(projectId);
    }

    @Override
    public Map<String, Object> getSectionElevation(String projectId) {
        Map<String, Object> map = new HashMap<>();
        List<Section> sections = dynamicMapper.getAllSection(projectId);
        for (Section section : sections) {
            List<List<Double>> list = new ArrayList<>();
            List<Map<String, Object>> m = dynamicMapper.getDistanceAndElevationByName(projectId, section.getName());
            for (Map<String, Object> elevation : m) {
                List<Double> values = new ArrayList<>();
                values.add((Double) elevation.get("distance"));
                values.add((Double) elevation.get("elevation"));
                list.add(values);
            }
            map.put(section.getName(), list);
        }
        return map;
    }


    @Override
    public List<Map<String, Object>> getFlux(String projectId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<String> tableIdList = dynamicMapper.getTableIdList(projectId);
        for (String tableId : tableIdList) {
            Map<String, Object> map = new HashMap<>();
            List<String> timeList = dynamicMapper.getTimeListByTableId(projectId, tableId);
            List<String> nameList = dynamicMapper.getNameListByTableId(projectId, tableId);
            List<List<Double>> value = new ArrayList<>();
            for (String name : nameList) {
                List<Double> valueList = dynamicMapper.getFluxValueByTableIdAndName(projectId, tableId, name);
                value.add(valueList);
            }
            map.put("type", dynamicMapper.getTypeByTableId(projectId, tableId));
            map.put("time", timeList);
            map.put("name", nameList);
            map.put("value", value);
            res.add(map);
        }
        return res;


    }

    @Override
    public List<Substrate> getSubstrate(String projectId) {
        List<Substrate> substrateList = dynamicMapper.getAllSubstrate(projectId);
        return substrateList;
    }

    @Override
    public List<Map<String, Object>> getSandTransport(String projectId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<String> tableIdList = dynamicMapper.getSandTransportTableList(projectId);
        for (String tableId : tableIdList) {
            Map<String, Object> map = new HashMap<>();
            List<String> timeList = dynamicMapper.getSandTransportTimeListByTableId(projectId, tableId);
            List<String> nameList = dynamicMapper.getSandTransportNameListByTableId(projectId, tableId);
            List<List<Double>> value = new ArrayList<>();
            for (String name : nameList) {
                List<Double> valueList = dynamicMapper.getSandTransportValueByTableIdAndName(projectId, tableId, name);
                value.add(valueList);
            }
            map.put("type", dynamicMapper.getSandTransportTypeByTableId(projectId, tableId));
            map.put("time", timeList);
            map.put("name", nameList);
            map.put("value", value);
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getSpeedOrientationNameAndType(String projectId) {
        return dynamicMapper.getSpeedOrientationNameAndType(projectId);
    }

    @Override
    public Map<String, Object> getSpeed(String projectId, String name, String type) {
        if (type.equals("small")) type = "小潮";
        else if (type.equals("large")) type = "大潮";
        else throw new MyException(ResultEnum.QUERY_TYPE_ERROR);
        List<String> times = dynamicMapper.getTime(projectId, name, type);
        List<String> nameList = dynamicMapper.getSectionSegment(projectId, name, type);
        List<List<Double>> value = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("time", times);
        res.put("nameList", nameList);
        for (String str : nameList) {
            value.add(dynamicMapper.getSpeedByNameAndTypeAndDistance(projectId, name, type, str));
        }
        res.put("value", value);
        return res;
    }

    @Override
    public Map<String, Object> getOrientation(String projectId, String name, String type) {
        if (type.equals("small")) type = "小潮";
        else if (type.equals("large")) type = "大潮";
        else throw new MyException(ResultEnum.QUERY_TYPE_ERROR);
        List<String> times = dynamicMapper.getTime(projectId, name, type);
        List<String> nameList = dynamicMapper.getSectionSegment(projectId, name, type);
        List<List<Double>> value = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("time", times);
        res.put("nameList", nameList);
        for (String str : nameList) {
            value.add(dynamicMapper.getOrientationByNameAndType(projectId, name, type, str));
        }
        res.put("value", value);
        return res;
    }

    @Override
    public List<String> getSandContentClass(String projectId) {
        return dynamicMapper.getSandContentClass(projectId);
    }

    @Override
    public Map<String, Object> getSandContentValue(String projectId, String name) {
        List<String> timeList = dynamicMapper.getSandContentTime(projectId, name);
        List<Double> valueList = dynamicMapper.getSandContentValue(projectId, name);
        Map<String, Object> res = new HashMap<>();
        res.put("time", timeList);
        res.put("value", valueList);
        return res;
    }

    @Override
    public List<Map<String, Object>> test() {
        String sql = "select * from section";
        return dynamicMapper.test("c5221577-6578-9e72-67dd-a66914ee7afd", sql);
    }
}
