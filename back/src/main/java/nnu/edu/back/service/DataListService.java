package nnu.edu.back.service;

import nnu.edu.back.pojo.DataList;
import nnu.edu.back.pojo.Station;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/0:20
 * @Description:
 */
public interface DataListService {
    void addDataList(DataList dataList);

    void updateList(DataList dataList);

    DataList getFileInfo(String id);


    void addWatchCount(String id, String email);

    Map<String, Object> fuzzyQuery(int page, int size, String keyword, String property, Boolean flag, String type);



    List<Map<String, Object>> getHot(int size);


    void downloadAll(String email, String id, HttpServletRequest request, HttpServletResponse response);

    List<Map<String, Object>> findFiles(String dataListId) throws IllegalAccessException;

    Map<String, Object> getSimilarData(String type, String id, int size, int page);

    List<Map<String, Object>> getIdAndDataListName(int size);

    void deleteDataList(String id, String role);

    Station getStationInfoByDataListId(String id);
}
