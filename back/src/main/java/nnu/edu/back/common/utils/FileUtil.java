package nnu.edu.back.common.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/14/11:04
 * @Description:
 */
@Slf4j
public class FileUtil {
    public static int uploadFile(MultipartFile multipartFile, String fileName, String address) {
        File file = new File(address);
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            fileOutputStream = new FileOutputStream(address + "/" + fileName);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            return 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (inputStream != null) inputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (Exception exception) {
               log.error(exception.getMessage());
            }
            return -1;
        }
    }

    public static int mergeFile(String from, String to, int total) {
        File ft = new File(to);
        FileInputStream in = null;
        FileChannel inChannel = null;
        FileOutputStream out = null;
        FileChannel outChannel = null;
        try {
            out = new FileOutputStream(ft, true);
            outChannel = out.getChannel();
            long start = 0;
            for (int i = 0; i < total; i++) {
                File file = new File(from + "/" + i);
                in = new FileInputStream(file);
                inChannel = in.getChannel();

                // 从inChannel中读取file.length()长度的数据，写入outChannel的start处
                outChannel.transferFrom(inChannel, start, file.length());
                start += file.length();
                in.close();
                inChannel.close();
            }
            out.flush();
            out.close();
            outChannel.close();
            return 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (in != null) in.close();
                if (inChannel != null) inChannel.close();
                if (out != null) out.close();
                if (outChannel != null) outChannel.close();
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }
            return -1;
        }
    }

    public static int compressFile(String destination, List<Map<String, Object>> fileList) {
        ZipFile zipFile = new ZipFile(destination);
        try {
            List<File> files = new ArrayList<>();
            Map<String, String> maps = new HashMap<>();
            for(Map<String, Object> map : fileList) {
                String address = (String) map.get("address");
                String name = address.split("/")[address.split("/").length - 1];
                files.add(new File(address));
                maps.put(name, (String) map.get("fileName"));
            }
            zipFile.addFiles(files);
            zipFile.renameFiles(maps);
            return 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }

    }

    public static int unpack(String destination, String to) {
        File file = new File(destination);
        if (!file.exists()) {
            return -1;
        }
        ZipFile zipFile = new ZipFile(destination);
        zipFile.setCharset(Charset.forName("GBK"));
        try {
            zipFile.extractAll(to);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
        return 0;
    }

    public static boolean deleteFolder(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return false;
        } else {
            if(file.isFile()) {
                return deleteFile(path);
            } else {
                return deleteDirectory(path);
            }
        }
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    private static boolean deleteDirectory(String path) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for(File f : files) {
            if(f.isFile()) {
                flag = deleteFile(f.getAbsolutePath());
                if(!flag) break;
            } else {
                flag = deleteDirectory(f.getAbsolutePath());
                if(!flag) break;
            }
        }
        if(!flag) return false;

        if(dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static JSONObject readJson(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            return jsonObject;
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
    }

    public static JSONArray readJsonArray(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            JSONArray jsonArray = JSONArray.parseArray(jsonString);
            return jsonArray;
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
    }

    public static String readTextFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            return jsonString;
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            return "";
        }
    }

    public static String formatFileSize(Long fileLength) {

        String fileSizeString = "";
        if (fileLength == null) {
            return fileSizeString;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "K";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;

    }
}
