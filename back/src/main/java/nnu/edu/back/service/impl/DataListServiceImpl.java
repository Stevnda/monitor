package nnu.edu.back.service.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.CommonUtils;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.dao.main.*;
import nnu.edu.back.dao.waterway.WaterwayMapper;
import nnu.edu.back.pojo.*;
import nnu.edu.back.service.DataListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/0:21
 * @Description:
 */
@Service
@Slf4j
public class DataListServiceImpl implements DataListService {
    @Autowired
    DataListMapper dataListMapper;

    @Value("${avatarDir}")
    String avatarDir;


    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Value("${tempDir}")
    String tempDir;

    @Value("${baseDir}")
    String baseDir;

    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Autowired
    BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    WaterwayMapper waterwayMapper;

    @Override
    public void addDataList(DataList dataList) {
        dataListMapper.addDataList(dataList);
    }

    @Override
    public void updateList(DataList dataList) {
        dataListMapper.updateDataList(dataList);
    }

    @Override
    public DataList getFileInfo(String id) {
        return dataListMapper.getFileInfo(id);
    }

    @Override
    public void addWatchCount(String id, String email) {
        dataListMapper.addWatchCount(id);
        BrowseHistory browseHistory = new BrowseHistory(UUID.randomUUID().toString(), email, null, id);
        browseHistoryMapper.addHistory(browseHistory);
    }

    @Override
    public Map<String, Object> fuzzyQuery(int page, int size, String keyword, String property, Boolean flag, String type) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        int total = dataListMapper.countFuzzyQuery(keyword, type);

        List<DataList> list = dataListMapper.fuzzyQuery(size * page, size, keyword, property, flag, type);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }



    @Override
    public List<Map<String, Object>> getHot(int size) {
        return dataListMapper.getHot(size);
    }


    @Override
    public void downloadAll(String email, String id, HttpServletRequest request, HttpServletResponse response) {
        List<Files> list = dataRelationalMapper.findFilesByDataListId(id);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Files files : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("address", baseDir + files.getAddress());
            map.put("fileName", files.getFileName());
            mapList.add(map);
        }
        String destination = tempDir + id + ".zip";
        int code = FileUtil.compressFile(destination, mapList);
        if (code == -1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(id + ".zip", "UTF-8"));
            in = new FileInputStream(destination);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
            downloadHistoryMapper.addHistory(new DownloadHistory(UUID.randomUUID().toString(), email, null, id, "all"));
            dataListMapper.addDownloadCount(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if(in != null) {
                    in.close();
                }
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<Map<String, Object>> findFiles(String dataListId) throws IllegalAccessException {
        DataList dataList = dataListMapper.getFileInfo(dataListId);
        List<Map<String, Object>> list = new ArrayList<>();
        if (dataList.getType().equals("实时水位")) {
            List<String> fileIds = dataRelationalMapper.findFileIdByDataListId(dataListId);
            if (fileIds.size() > 1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            else {
                Map<String, Object> map = new HashMap<>();
                map.put("stationId", fileIds.get(0));
                list.add(map);
            }
        } else {
            List<Files> filesList = dataRelationalMapper.findFilesByDataListId(dataListId);
            for (Files files : filesList) {
                Map<String, Object> map = CommonUtils.objectToMap(files);
                if (!files.getVisualType().equals("")) {
                    map.put("view", visualFileMapper.getView(map.get("visualId").toString()));
                }
                list.add(map);
            }

        }
        return list;
    }


    @Override
    public Map<String, Object> getSimilarData(String type, String id, int size, int page) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = dataListMapper.getSimilarData(type, id, size, size * page);
        int total = dataListMapper.getSimilarCount(type);
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public List<Map<String, Object>> getIdAndDataListName(int size) {
        return dataListMapper.getRandom(size);
    }

    @Override
    public void deleteDataList(String id, String role) {
        if (role.equals("admin")) {
            dataListMapper.deleteById(id);
            dataRelationalMapper.deleteByDataListId(id);
        }
    }

    @Override
    public Station getStationInfoByDataListId(String id) {
        List<String> list = dataRelationalMapper.findFileIdByDataListId(id);
        if (list.size() > 1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);

        return waterwayMapper.getStationInfoByStationId(list.get(0));
    }
}
