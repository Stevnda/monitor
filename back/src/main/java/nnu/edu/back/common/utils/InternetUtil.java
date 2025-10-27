package nnu.edu.back.common.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/17/10:48
 * @Description:
 */
@Slf4j
public class InternetUtil {
    public static JSONObject doGet(String url) {
        StringBuilder response = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

        return JSONObject.parseObject(response.toString());
    }

    public static JSONObject doGet(String url, Map<String, String> map) {
        StringBuilder response = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            for (String key : map.keySet()) {
                connection.setRequestProperty(key, map.get(key));
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

        return JSONObject.parseObject(response.toString());
    }

    public static void downloadMeteorologyPng(String url, String path) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        FileOutputStream outs = null;
        try {
            outs = new FileOutputStream(path);
            outs.write(result.getBody());
            outs.flush();
            outs.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                if (outs != null) outs.close();
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }
        }
    }

    public static String encodeSpaceChinese(String str, String charset) throws UnsupportedEncodingException {
        //匹配中文和空格的正则表达式
        String zhPattern = "[\u4e00-\u9fa5 ]+";
        Pattern p = Pattern.compile(zhPattern);
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find())
        {
            m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
        }
        m.appendTail(b);
        return b.toString().replaceAll("\\+", "%20");
    }

    public static <T>T httpHandle(String url, HttpEntity httpEntity, Class<T> c, String method) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<T> result;
        if (method.equals("post")) {
            result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, c);
        } else if (method.equals("get")) {
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, c);
        } else if (method.equals("delete")) {
            result = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, c);
        } else if (method.equals("put")) {
            result = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, c);
        } else {
            throw new Exception();
        }
        return result.getBody();
    }

    public static JSONObject GetRealData(String url) {
        try {
            URL Url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                System.out.println("GET request failed with response code " + responseCode);
                return new JSONObject();
            }
            connection.disconnect();
            return JSONObject.parseObject(response.toString());
        } catch (Exception e) {
            System.out.println(e);
            return new JSONObject();
        }
    }
}
