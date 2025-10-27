package nnu.edu.back.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.common.utils.ProcessUtil;
import nnu.edu.back.dao.main.DataRelationalMapper;
import nnu.edu.back.dao.main.FilesMapper;
import nnu.edu.back.dao.main.VisualFileMapper;
import nnu.edu.back.pojo.Files;
import nnu.edu.back.pojo.Folder;
import nnu.edu.back.pojo.UploadRecord;
import nnu.edu.back.pojo.VisualFile;
import nnu.edu.back.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/21:36
 * @Description:
 */
@Slf4j
@Service
public class FilesServiceImpl implements FilesService {
    @Autowired
    FilesMapper filesMapper;

    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Value("${tempDir}")
    String tempDir;

    @Value("${visualDir}")
    String visualDir;

    @Value("${pgPath}")
    String pgPath;

    @Value("${baseDir}")
    String baseDir;

    @Override
    public String addFiles(Files file) {
        String uuid = UUID.randomUUID().toString();
        file.setId(uuid);
        filesMapper.addFile(file);
        return uuid;
    }

    @Override
    public void downloadFile(String id, HttpServletResponse response) {
        Files files = filesMapper.findInfoById(id);
        String address = baseDir + files.getAddress();
        File file = new File(address);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(files.getFileName(), "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = in.read(b)) > 0) {
                sos.write(b, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (in != null) in.close();
                if (sos != null) sos.close();
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public String bindVisualData(JSONObject jsonObject) {
        String fileId = jsonObject.getString("id");
        String fileName = jsonObject.getString("fileName");
        String type = jsonObject.getString("type");
        JSONArray jsonArray = jsonObject.getJSONArray("coordinates");
        String srid = jsonObject.getString("srid");
        JSONObject view = jsonObject.getJSONObject("view");
        String viewStr = "";
        if (view != null) {
            viewStr = JSONObject.toJSONString(view);
        }
        String content = "";

        if (type.equals("png") || type.equals("movePng")) {
            JSONObject json = new JSONObject();
            json.put("address", "png/" + fileName);
            json.put("coordinates", jsonArray);
            content = JSONObject.toJSONString(json);
        } else if (type.equals("rateDirection") || type.equals("sandContent") || type.equals("salinity") || type.equals("suspension") || type.equals("flowSand_Z") || type.equals("tide")) {
            if (type.equals("flowSand_Z")) {
                content = "flowSand/" + fileName;
            } else {
                content = type + "/" + fileName;
            }
        } else if (type.equals("rasterTile")) {
            String folderName = fileName.substring(0, fileName.lastIndexOf("."));
            content = "rasterTile/" + folderName + "/tiles";
            FileUtil.unpack(tempDir + fileName, visualDir + "rasterTile/" + folderName + "/tiles");
            fileName = folderName;
        } else if (type.equals("pointVectorTile") || type.equals("pointVectorTile3D") || type.equals("lineVectorTile") || type.equals("lineVectorTile3D") || type.equals("polygonVectorTile") || type.equals("polygonVectorTile3D")) {
            content = fileName.substring(0, fileName.lastIndexOf("."));
            FileUtil.unpack(tempDir + content + ".zip", tempDir + content);
            String shpName = "";
            java.io.File folder = new java.io.File(tempDir + content);
            String fileList[] = folder.list();
            for (String name : fileList) {
                String suffix = name.substring(name.lastIndexOf("."));
                if (suffix.equals(".shp")) {
                    shpName = name;
                    break;
                }
            }
            if (shpName.equals("")) {
                throw new MyException(-99, "文件内容错误");
            }
            List<String> commands = new ArrayList<>();
            String command = "D: & cd " + pgPath + " & shp2pgsql -s " + srid + " -d " + tempDir + content + "/" + shpName + " " + content + " | psql -h localhost -U postgres -d shp_dataset";
            commands.add("cmd");
            commands.add("/c");
            commands.add(command);
            try {
                Process process = ProcessUtil.cmdShp2Pgsql(commands);
                ProcessUtil.readProcessOutput(process.getInputStream(), System.out);
                int state = process.exitValue();
                FileUtil.deleteFolder(tempDir + content + ".zip");
                if (state != 0) {
                    throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else if (type.equals("photo") || type.equals("video")) {
            filesMapper.updateVisualIdAndType(fileId, "", type);
            return "";
        } else {
            throw new MyException(ResultEnum.QUERY_TYPE_ERROR);
        }
        String id = UUID.randomUUID().toString();
        VisualFile visualFile = new VisualFile(id, fileName, type, content, viewStr);
        visualFileMapper.addVisualFile(visualFile);
        filesMapper.updateVisualIdAndType(fileId, id, type);
        return id;
    }

    @Override
    public void cancelVisualBind(String id) {
        filesMapper.updateVisualIdAndType(id, "", "");
    }

    @Override
    public List<Object> findByFolderId(String parentId, String role) {
        if (role.equals("admin")) {
            if (parentId.equals("-1")) parentId = "";
            List<Object> res = new ArrayList<>();
            res.addAll(filesMapper.findFolderByParentId(parentId));
            res.addAll(filesMapper.findFilesByParentId(parentId));
            return res;
        } else throw new MyException(ResultEnum.NO_ACCESS);
    }

    @Override
    public String addFolder(String folderName, String parentId, String role) {
        if (role.equals("admin")) {
            String uuid = UUID.randomUUID().toString();
            Folder folder = new Folder(uuid, folderName, parentId);
            filesMapper.addFolder(folder);
            return uuid;
        } else throw new MyException(ResultEnum.NO_ACCESS);
    }

    @Override
    public VisualFile getVisualFileByVisualId(String visualId) {
        return filesMapper.getVisualFileByVisualId(visualId);
    }

    @Override
    public void deleteFilesOrFolders(JSONObject jsonObject, String role) {
        if (role.equals("admin")) {
            List<String> files = (List<String>) jsonObject.get("files");
            List<String> folders = (List<String>) jsonObject.get("folders");
            if (files.size() > 0) {
                filesMapper.batchDelete(files);
                dataRelationalMapper.batchDeleteByFileId(files);
            }
            if (folders.size() > 0) {
                dataRelationalMapper.recursionDeleteFileId(folders);
                filesMapper.recursionDeleteFile(folders);
                filesMapper.recursionDeleteFolder(folders);
            }
        } else throw new MyException(ResultEnum.NO_ACCESS);
    }

    @Override
    public List<UploadRecord> getUploadRecord(String role) {
        if (role.equals("admin")) {
            return filesMapper.getUploadRecord();
        } throw new MyException(ResultEnum.NO_ACCESS);

    }

    @Override
    public void uploadChunks(MultipartFile file, String number, String id) {
        String address = Paths.get(tempDir, id).toString();
        int code = FileUtil.uploadFile(file, number, address);
        if (code == -1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
    }

    @Override
    public UploadRecord mergeChunks(String parentId, String id, int total, String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf(".")) + UUID.randomUUID().toString().substring(0, 8);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String address = Paths.get(tempDir, id).toString();
        String to = Paths.get(baseDir, name + suffix).toString();
        FileUtil.mergeFile(address, to, total);
        File file = new File(address);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        String[] fileNames = file.list();
        if (fileNames.length == total) {
            FileUtil.mergeFile(address, to, total);
            Files f = new Files();
            f.setId(UUID.randomUUID().toString());
            f.setFileName(fileName);
            f.setAddress(name);
            f.setSize(FileUtil.formatFileSize(new File(to).length()));
            f.setVisualId("");
            f.setVisualType("");
            f.setParentId(parentId);
            filesMapper.addFile(f);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            UploadRecord uploadRecord = new UploadRecord(UUID.randomUUID().toString(), fileName, format.format(new Date()), f.getSize());
            filesMapper.addUploadRecord(uploadRecord);
            return uploadRecord;
        } else throw new MyException(-99, "数据缺损!");
    }

    @Override
    public String visualFileMerge(String id, int total, String type, String name) {
        String address = Paths.get(tempDir, id).toString();
        File folder = new File(address);
        if (!folder.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } else {
            if (folder.list().length != total) {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        if (type.equals("png") || type.equals("movePng")) {
            String suffix = name.substring(name.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            String to = Paths.get(visualDir, "png", fileName).toString();
            FileUtil.mergeFile(address, to, total);
            return fileName;
        } else if (type.equals("rasterTile")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long number = timestamp.getTime();
            String suffix = name.substring(name.lastIndexOf("."));
            String to = Paths.get(tempDir, "rasterTile", number + suffix).toString();
            FileUtil.mergeFile(address, to, total);
            return "rasterTile" + number + suffix;
        } else if (type.equals("polygonVectorTile3D") || type.equals("pointVectorTile3D") || type.equals("lineVectorTile3D") || type.equals("polygonVectorTile") || type.equals("pointVectorTile") || type.equals("lineVectorTile")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long number = timestamp.getTime();
            String suffix = name.substring(name.lastIndexOf("."));
            String to = Paths.get(tempDir, "shp", number + suffix).toString();
            FileUtil.mergeFile(address, to, total);
            return "shp" + number + ".shp";
        } else {
            String suffix = name.substring(name.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            String to;
            if (type.equals("flowSand_Z")) {
                to = Paths.get(tempDir, "flowSand", fileName).toString();
            } else {
                to = Paths.get(tempDir, type, fileName).toString();
            }
            FileUtil.mergeFile(address, to, total);
            return fileName;
        }
    }

    @Override
    public void delAllRecord(String role) {
        if (role.equals("admin")) {
            filesMapper.delAllRecord();
        } else throw new MyException(ResultEnum.NO_ACCESS);
    }

    @Override
    public void delRecord(String id, String role) {
        if (role.equals("admin")) {
            filesMapper.delRecord(id);
        } else throw new MyException(ResultEnum.NO_ACCESS);
    }
}
