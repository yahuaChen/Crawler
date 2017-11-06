package com.crawler.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下载网络文件
 * Created by 金水信息 on 2017/11/1.
 */
public class DownLoadFile {
    private static String FILE_DOWNLOAD_PATH="E:\\CrawlerFiles\\";
    /**
     * 根据URL和资源类型生成需要保存的资源文件名。
     * @param url
     * @param contentType
     * @return
     */
    public String getFileNameByUrl(String url,String contentType){
        //移除 http://
        url=url.substring(7);
        //去除URL中非文件名字符正则表达式
        String regex="[\\?/:*|<>\"]";
        if(contentType.indexOf("html")!=-1){
            //text/html类型
            url=url.replaceAll(regex,"_")+".html";
            return url;
        }else{
            //如application/pdf类型
            return url.replaceAll(regex,"_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
        }
    }

    /**
     * 保存字节数组到文件
     * @param data
     * @param filePath
     */
    private void saveToPath(byte[] data,String filePath){
        try {
            File file=new File(FILE_DOWNLOAD_PATH+filePath);
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            DataOutputStream out=new DataOutputStream(new FileOutputStream(file));
            out.write(data);
            out.flush();
            out.close();
            System.out.println("保存文件："+filePath.substring(filePath.indexOf("\\")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int downloadFile(String url){
        int statusCode=-1;
        try{

            HttpClient httpClient=new HttpClient();
            //超时时间五秒钟
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            GetMethod getMethod=new GetMethod(url);
            //GET请求超时时间5秒钟
            getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,5000);
            //设置请求重试处理
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());

            try {
                statusCode=httpClient.executeMethod(getMethod);
                if(statusCode== HttpStatus.SC_OK){
                    //处理HTTP响应内容
                    byte[]responseBody=getMethod.getResponseBody();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日爬取的数据");
                    String filePath=simpleDateFormat.format(new Date())+"\\"+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
                    saveToPath(responseBody,filePath);
                }else{
                    System.err.println("Method failed:"+getMethod.getStatusLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return statusCode;
    }
}
