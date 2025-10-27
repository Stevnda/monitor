package nnu.edu.back.service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:45
 * @Description:
 */
public interface DataRelationalService {
    void addRelational(String dataListId, List<String> fileIdList);

    void updateRelational(String dataListId, List<String> fileList);
}
