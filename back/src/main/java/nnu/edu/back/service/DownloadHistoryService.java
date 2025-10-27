package nnu.edu.back.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:07
 * @Description:
 */
public interface DownloadHistoryService {
    Map<String, Object> pageQuery(int size, int page, String dataListId);
}
