package nnu.edu.back.service;

import nnu.edu.back.pojo.BrowseHistory;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:38
 * @Description:
 */
public interface BrowseHistoryService {


    List<Map<String, Object>> getDataGroupByDate(String dataId, int number);
}
