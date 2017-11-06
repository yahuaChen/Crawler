package com.crawler.demo;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 金水信息 on 2017/11/1.
 */
public class RetrivePage {
    //创建一个客户端，类似于浏览器
    private static HttpClient httpClient=new HttpClient();

    static{
        //设置代理服务器的IP和端口号
        //httpClient.getHostConfiguration().setProxy("192.168.0.103",8080);
    }

    /**
     * 下载文件
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean downloadPage(String path)throws IOException{
        //得到post方法
        PostMethod postMethod=new PostMethod(path);
        //设置post方法的参数
        //NameValuePair[] postData=new NameValuePair[2];
        //postData[0]=new NameValuePair("name","yahua");
        //postData[1]=new NameValuePair("password","123456");
        //postMethod.addParameters(postData);
        //执行，返回状态码
        int statusCode=httpClient.executeMethod(postMethod);
        //处理状态码200的操作
        if(statusCode==HttpStatus.SC_OK){
            handler200Response(postMethod,path);
        }else if(statusCode==HttpStatus.SC_MOVED_TEMPORARILY){
            handler3XXResponse(postMethod,path);
        }

        return false;
    }

    public static boolean handler200Response(PostMethod postMethod,String path) throws IOException {
        InputStream inputStream=postMethod.getResponseBodyAsStream();
        OutputStream outputStream=new FileOutputStream(path.substring(path.lastIndexOf("/")+1));

        //输出到文件
        int tempSize=-1;
        byte[] tempData=new byte[1024];
        while((tempSize=inputStream.read(tempData))>0){
            if(tempSize!=tempData.length){
                byte[] newTempData=new byte[tempSize];
                System.arraycopy(tempData,0,newTempData,0,tempSize);
                outputStream.write(newTempData);
            }else{
                outputStream.write(tempData);
            }
        }

        //关闭流
        inputStream.close();
        outputStream.close();
        return true;
    }

    public static boolean handler3XXResponse(PostMethod postMethod,String path) throws IOException {
        //读取新的URL地址
        Header header=postMethod.getResponseHeader("location");
        if(header!=null){
            String newUrl=header.getValue();
            if(newUrl!=null&&!newUrl.equals("")){
                PostMethod redirect=new PostMethod(newUrl);
                int statusCode=httpClient.executeMethod(redirect);
                if(statusCode==HttpStatus.SC_MOVED_TEMPORARILY){
                    handler3XXResponse(redirect,path);
                }else if(statusCode==HttpStatus.SC_OK){
                    handler200Response(redirect,path);
                }
            }
        }else{

        }
        return true;
    }

    public static void main(String[] args) {
        try {
            RetrivePage.downloadPage("https://www.baidu.com");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
