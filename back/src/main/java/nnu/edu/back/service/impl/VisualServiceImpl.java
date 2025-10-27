package nnu.edu.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.common.utils.InternetUtil;
import nnu.edu.back.common.utils.TileUtil;
import nnu.edu.back.dao.main.AnalysisResultMapper;
import nnu.edu.back.dao.main.FilesMapper;
import nnu.edu.back.dao.main.VisualFileMapper;
import nnu.edu.back.dao.map.MapMapper;
import nnu.edu.back.dao.shp.VectorTileMapper;
import nnu.edu.back.pojo.AnalysisResult;
import nnu.edu.back.pojo.Files;
import nnu.edu.back.pojo.TileBox;
import nnu.edu.back.pojo.VisualFile;
import nnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/22:53
 * @Description:
 */
@Service
@Slf4j
public class VisualServiceImpl implements VisualService {
    @Value("${avatarDir}")
    String avatarDir;

    @Value("${visualDir}")
    String visualDir;

    @Value("${baseDir}")
    String baseDir;

    @Value("${mapDir}")
    String mapDir;

    @Value("${tempDir}")
    String tempDir;

    @Value("${analysisDir}")
    String analysisDir;

    @Value("${fileViewUrl}")
    String fileViewUrl;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Autowired
    VectorTileMapper vectorTileMapper;

    @Autowired
    FilesMapper filesMapper;

    @Autowired
    AnalysisResultMapper analysisResultMapper;

    @Autowired
    MapMapper mapMapper;



    @Override
    public void getAvatar(String fileName, HttpServletResponse response) {
        String pictureAddress = avatarDir + fileName;
        File file = new File(pictureAddress);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (outputStream != null) outputStream.close();;
            } catch (Exception exception) {
                log.error(e.getMessage());
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public void getRaster(String visualId, int x, int y, int z, HttpServletResponse response) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        y = (int) Math.pow(2, z) - y - 1;
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
            sos = response.getOutputStream();
            byte[] bytes = (byte[]) mapMapper.getRasterTile(String.valueOf(x), String.valueOf(y), String.valueOf(z), visualFile.getContent());
            if (bytes == null) {
                in = new FileInputStream(visualDir + "blank.png");
                byte[] byteArray = new byte[1024];
                int len;
                while ((len = in.read(byteArray)) > -1) {
                    sos.write(byteArray, 0, len);
                }
                sos.flush();
                sos.close();
                in.close();
            } else {
                int off = 0;
                while (off < bytes.length) {
                    if (off + 1024 <= bytes.length) {
                        sos.write(bytes, off, 1024);
                    } else {
                        sos.write(bytes, off, bytes.length - off);
                    }
                    off += 1024;
                }
                sos.flush();
                sos.close();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
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
    public void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, (String) visualFile.getContent());
        tileBox.setVisualId(visualId);
        byte[] bytes;
        if (visualFile.getType().equals("pointVectorTile") || visualFile.getType().equals("lineVectorTile") || visualFile.getType().equals("polygonVectorTile")) {
            bytes = (byte[]) vectorTileMapper.getVictorTile(tileBox);
        } else {
            bytes = (byte[]) vectorTileMapper.getVictorTile3D(tileBox);
        }
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            sos = response.getOutputStream();
            sos.write(bytes);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (sos != null) {
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
    public void getPhoto(String fileId, HttpServletResponse response) {
        Files files = filesMapper.findInfoById(fileId);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(baseDir + files.getAddress());
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
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
    public JSONArray getCoordinates(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String content = visualFile.getContent();
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getJSONArray("coordinates");
    }

    @Override
    public void getPngResource(String visualId, HttpServletResponse response) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String content = visualFile.getContent();
        JSONObject jsonObject = JSONObject.parseObject(content);
        String address = visualDir + jsonObject.getString("address");
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(address);
            sos = response.getOutputStream();
            response.setContentType("image/png");
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
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
    public JSONObject getContent(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        return JSONObject.parseObject(visualFile.getContent());
    }

    @Override
    public JSONObject getTide(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getRateDirection(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSandContent(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getFlowSand_Z(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSalinity(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSuspension(String visualId) {
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String path = visualDir + visualFile.getContent();
        return FileUtil.readJson(path);
    }



    @Override
    public JSONObject getGeoJson(String fileId) {
        String path = visualDir + "geoJson/" + fileId + ".json";
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getAnalysisGeoJson(String fileId) {
        AnalysisResult analysisResult = analysisResultMapper.getInfoById(fileId);
        String path = analysisDir + analysisResult.getCaseId() + "/" + analysisResult.getAddress();
        return FileUtil.readJson(path);
    }

    @Override
    public Map<String, Object> getSection(String fileId) {
        AnalysisResult analysisResult = analysisResultMapper.getInfoById(fileId);
        String address = analysisResult.getAddress();
        String path = analysisDir + analysisResult.getCaseId() + "/" + address;
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        List<Double> list = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));

            String value = "";
            while ((value = br.readLine()) != null) {
                if (!value.equals("-3.4028235e+38")) {
                    list.add(Double.valueOf(value));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }

    @Override
    public List<List<Double>> getSectionContrast(String fileId) {
        AnalysisResult analysisResult = analysisResultMapper.getInfoById(fileId);
        String address = analysisResult.getAddress();
        String path = analysisDir + analysisResult.getCaseId() + "/" + address;
        File file = new File(path);
        List<List<Double>> result = new ArrayList<>();
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            int number = Integer.parseInt(br.readLine());
            for (int i = 0; i < number; i++) {
                List<Double> list = new ArrayList<>();
                String value = "";
                while ((value = br.readLine()) != null) {
                    if (value.equals("")) {
                        break;
                    }
                    if (!value.equals("-3.4028235e+38")) {
                        list.add(Double.valueOf(value));
                    } else {
                        list.add(0.0);
                    }
                }
                result.add(list);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        return result;
    }

    @Override
    public Map<String, Object> getSectionFlush(String fileId) {
        AnalysisResult analysisResult = analysisResultMapper.getInfoById(fileId);
        String address = analysisResult.getAddress();
        String path = analysisDir + analysisResult.getCaseId() + "/" + address;
        File file = new File(path);
        Map<String, Object> result = new HashMap<>();
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

            List<List<Double>> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                List<Double> temp = new ArrayList<>();
                String value = "";
                while ((value = br.readLine()) != null) {
                    if (value.equals("")) {
                        break;
                    }
                    if (!value.equals("-3.4028235e+38")) {
                        temp.add(Double.valueOf(value));
                    } else {
                        temp.add(0.0);
                    }
                }
                list.add(temp);
            }
            br.close();
            result.put("benchmark", list.get(0));
            result.put("refer", list.get(1));
            result.put("flush", list.get(2));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public JSONObject getVolume(String fileId) {
        AnalysisResult analysisResult = analysisResultMapper.getInfoById(fileId);
        String visualId = analysisResult.getVisualId();
        VisualFile visualFile = visualFileMapper.findById(visualId);
        String address = visualFile.getContent();
        String path = visualDir + address;
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getTianDiTu() {
//        String jsonString = "{\"version\":8,\"sources\":{\"tdtVec\":{\"type\":\"raster\",\"tiles\":[\"http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles\"],\"tileSize\":256},\"txt\":{\"type\":\"raster\",\"tiles\":[\"http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles\"],\"tileSize\":256}},\"layers\":[{\"id\":\"tdtVec\",\"type\":\"raster\",\"source\":\"tdtVec\"},{\"id\":\"txt\",\"type\":\"raster\",\"source\":\"txt\"}]}";
        String path = mapDir + "tianditu.json";
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getTianDiTuImage() {
        String path = mapDir + "tianditu-image.json";
        return FileUtil.readJson(path);
    }




    @Override
    public void video(String id, HttpServletRequest request, HttpServletResponse response) {
        Files files = filesMapper.findInfoById(id);
        String path = baseDir + files.getAddress();
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        long fileLength = file.length();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            String rangeString = request.getHeader("Range");
            long range=0;
            if (StrUtil.isNotBlank(rangeString)) {
                range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-Type", "video/mp4");
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            randomAccessFile.seek(range);
            byte[] bytes = new byte[1024 * 1024 * 2];
            int len = randomAccessFile.read(bytes);
            response.setContentLength(len);
            response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
            outputStream.write(bytes, 0, len);
            outputStream.close();
            randomAccessFile.close();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public void uploadFileView(String id) throws Exception {
        Files files = filesMapper.findInfoById(id);
        String address = Paths.get(baseDir, files.getAddress()).toString();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(address));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        JSONObject jsonObject = InternetUtil.httpHandle(fileViewUrl, requestEntity, JSONObject.class, "post");
        if (!jsonObject.getBoolean("success")) throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
    }
}
