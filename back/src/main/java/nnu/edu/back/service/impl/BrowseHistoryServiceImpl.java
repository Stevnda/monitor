package nnu.edu.back.service.impl;

import nnu.edu.back.common.utils.CommonUtils;
import nnu.edu.back.dao.main.BrowseHistoryMapper;
import nnu.edu.back.pojo.BrowseHistory;
import nnu.edu.back.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:38
 * @Description:
 */
@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {
    @Autowired
    BrowseHistoryMapper browseHistoryMapper;

    @Override
    public List<Map<String, Object>> getDataGroupByDate(String dataId, int number) {
        String date = CommonUtils.getDate(number);
        return browseHistoryMapper.getDataGroupByDate(dataId, date);
    }
}
