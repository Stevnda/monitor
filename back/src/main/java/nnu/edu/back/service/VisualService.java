package nnu.edu.back.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/22:52
 * @Description:
 */
public interface VisualService {
    void getAvatar(String fileName, HttpServletResponse response);

    void getRaster(String visualId, int x, int y, int z, HttpServletResponse response);

    void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response);

    void getPhoto(String fileId, HttpServletResponse response);


    JSONArray getCoordinates(String visualId);

    void getPngResource(String visualId, HttpServletResponse response);

    JSONObject getContent(String visualId);

    JSONObject getTide(String visualId);

    JSONObject getRateDirection(String visualId);

    JSONObject getSandContent(String visualId);

    JSONObject getFlowSand_Z(String visualId);

    JSONObject getSalinity(String visualId);

    JSONObject getSuspension(String visualId);


    JSONObject getGeoJson(String fileId);

    JSONObject getAnalysisGeoJson(String fileId);

    Map<String, Object> getSection(String fileId);

    List<List<Double>> getSectionContrast(String fileId);

    Map<String, Object> getSectionFlush(String fileId);

    JSONObject getVolume(String fileId);

    JSONObject getTianDiTu();

    JSONObject getTianDiTuImage();

    void video(String id, HttpServletRequest request, HttpServletResponse response);

    void uploadFileView(String id) throws Exception;
}
