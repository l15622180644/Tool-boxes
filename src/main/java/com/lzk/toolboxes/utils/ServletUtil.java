package com.lzk.toolboxes.utils;

import com.alibaba.fastjson.JSONObject;
import com.lzk.toolboxes.config.base.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @module
 * @date 2021/5/25 17:17
 */
public class ServletUtil {

    private ServletUtil(){}

    private static Map<String,String> typeMap = new HashMap<>();

    static {
        typeMap.put("xls","application/vnd.ms-excel application/x-excel");
        typeMap.put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        typeMap.put("doc","application/msword");
        typeMap.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        typeMap.put("pdf","application/pdf");
        typeMap.put("rar","application/octet-stream");
        typeMap.put("tar","application/x-tar");
        typeMap.put("tgz","application/x-compressed");
        typeMap.put("zip","application/x-zip-compressed");
        typeMap.put("z","application/x-compress");
        typeMap.put("gif","image/gif");
        typeMap.put("png","image/png");
        typeMap.put("tiff","image/tiff");
        typeMap.put("tif","image/tiff");
        typeMap.put("jpg","image/jpeg");
        typeMap.put("jpeg","image/jpeg");
        typeMap.put("jpe","image/jpeg");
        typeMap.put("txt","text/plain");
        typeMap.put("xml","text/xml");
        typeMap.put("html","text/html");
        typeMap.put("css","text/css");
        typeMap.put("js","text/javascript");
        typeMap.put("ppt","application/vnd.ms-powerpoint");
        typeMap.put("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    public static ServletRequestAttributes getRequestAttributes(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }

    public static HttpServletResponse setDownloadHead(HttpServletResponse response,String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        try {
            //用于定义用户的浏览器或相关设备如何显示将要加载的数据
            if(typeMap.containsKey(suffix)){
                response.setContentType(typeMap.get(suffix));
            }else {
                response.setContentType("applicatoin/octet-stream");
            }
            //允许所有域名都可以访问
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    public static void returnJSON(Status status) {
        returnJSON(status,null);
    }

    public static void returnJSON(Status status, String msg) {
        HttpServletResponse response = getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", status.code());
        jsonObject.put("msg", msg!=null?msg:status.msg());
        String jsonString = jsonObject.toJSONString();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }


}
