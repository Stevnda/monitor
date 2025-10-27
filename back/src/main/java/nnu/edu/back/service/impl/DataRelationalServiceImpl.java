package nnu.edu.back.service.impl;

import nnu.edu.back.dao.main.DataRelationalMapper;
import nnu.edu.back.pojo.DataRelational;
import nnu.edu.back.service.DataRelationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:46
 * @Description:
 */
@Service
public class DataRelationalServiceImpl implements DataRelationalService {
    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Override
    public void addRelational(String dataListId, List<String> fileIdList) {
        if (fileIdList.size() != 0) {
            List<DataRelational> list = new ArrayList<>();
            for (String fileId : fileIdList) {
                list.add(new DataRelational(UUID.randomUUID().toString(), dataListId, fileId));
            }
            dataRelationalMapper.batchInsert(list);
        }
    }

    @Override
    public void updateRelational(String dataListId, List<String> fileList) {
        List<String> fileIdList = dataRelationalMapper.findFileIdByDataListId(dataListId);
        List<String> adds = new ArrayList<>();
        List<String> removes = new ArrayList<>();
        for(int i = 0; i < fileList.size(); i++) {
            for(int j = 0; j < fileIdList.size(); j++) {
                if(fileList.get(i).equals(fileIdList.get(j))) {
                    fileList.remove(i);
                    fileIdList.remove(j);
                    i = i - 1;
                    j = j - 1;
                    break;
                }
            }
        }
        for(int i = 0; i < fileIdList.size(); i++) {
            removes.add((String) fileIdList.get(i));
        }
        for(int i = 0; i < fileList.size(); i++) {
            adds.add(fileList.get(i));
        }
        if(adds.size() > 0) {
            addRelational(dataListId, adds);
        }
        if(removes.size() > 0) {
            dataRelationalMapper.batchDelete(removes, dataListId);
        }
    }
}
