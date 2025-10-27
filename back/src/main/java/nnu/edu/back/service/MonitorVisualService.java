package nnu.edu.back.service;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.pojo.Locus;
import nnu.edu.back.pojo.Section;
import nnu.edu.back.pojo.Substrate;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/23/11:03
 * @Description:
 */
public interface MonitorVisualService {
    List<String> getLocusPoint(String projectId);

    List<Locus> getLocusTable(String projectId, String name);

    JSONObject getLocusShape(String projectId, String name);

    List<Section> getAllSection(String projectId);

    Map<String, Object> getSectionElevation(String projectId);

    List<Map<String, Object>> getFlux(String projectId);

    List<Substrate> getSubstrate(String projectId);



    List<Map<String, Object>> getSandTransport(String projectId);

    List<Map<String, Object>> getSpeedOrientationNameAndType(String projectId);

    Map<String, Object> getSpeed(String projectId, String name, String type);

    Map<String, Object> getOrientation(String projectId, String name, String type);

    List<String> getSandContentClass(String projectId);

    Map<String, Object> getSandContentValue(String projectId, String name);

    List<Map<String, Object>> test();


}
