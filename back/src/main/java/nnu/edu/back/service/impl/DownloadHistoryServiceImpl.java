package nnu.edu.back.service.impl;

import nnu.edu.back.dao.main.DownloadHistoryMapper;
import nnu.edu.back.service.DownloadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:07
 * @Description:
 */
@Service
public class DownloadHistoryServiceImpl implements DownloadHistoryService {
    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Override
    public Map<String, Object> pageQuery(int size, int page, String dataListId) {
        int start = size * page;
        Map<String, Object> result = new HashMap<>();
        result.put("total", downloadHistoryMapper.countByDataId(dataListId));
        result.put("list", downloadHistoryMapper.pageQuery(size, start, dataListId));
        return result;
    }
}
