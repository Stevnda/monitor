package nnu.edu.back.service;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.pojo.AnalysisCase;
import nnu.edu.back.pojo.AnalysisResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/17:16
 * @Description:
 */
public interface AnalysisService {
    void addAnalysis(AnalysisCase analysisCase, String email);

    void deleteAnalysisCase(String id, String email);

    Map<String, Object> pageQuery(String email, String keyword, int page, int size);

    AnalysisCase getAnalysisInfo(String id, String email);

    void addData(String id, List<Map<String, String>> list);

    List<Map<String, Object>> getData(String id);

    void delData(String caseId, String dataListId, String fileId);

    void updateLayer(String caseId, List<String> list);

    List<Object> getLayersInfo(String id);

    void updateProjectInfo(AnalysisCase analysisCase, String email);

    void deleteAnalysis(String caseId, String email);

    List<Map<String, Object>> findParameterByType(String type);

    List<AnalysisResult> getAnalysisResult(String caseId);

    String addDraw(JSONObject jsonObject, String email);

    void delAnalysisResult(String id, String email);

    String addSection(String caseId, String sectionId, String demId, String email, String fileName);

    String addSectionCompare(String caseId, String sectionId, String email, List<String> demList, String fileName);

    String addSectionFlush(String caseId, String sectionId, String benchmarkId, String referId, String email, String fileName);

    String addRegionFlush(String caseId, String regionId, String benchmarkId, String referId, String email, String fileName);

    String computeVolume(double deep, String caseId, String regionId, String demId, String email, String fileName);

    Map<String, Object> addElevationFlush(String caseId, String benchmarkId, String referId, String email, String fileName);

    Map<String, Object> addFlushContour(String caseId, String benchmarkId, String referId, String email, String fileName);

    Map<String, Object> addSlope(String caseId, String demId, String email, String fileName);

    AnalysisResult checkState(String key);

    void rename(String id, String name);

    void downloadAnalysisResult(String id, HttpServletResponse response);

    SseEmitter subscribe(String id) throws IOException;



    void over(String id, String email) throws IOException;

    Set<String> test();
}
