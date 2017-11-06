package com.crawler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * byte[]数组转换为MD5加密后的字符串
 * Created by 金水信息 on 2017/11/1.
 */
public class MD5 {
    public static String getMD5(byte[] source){
        String s=null;
        //用来将字节转换成十六进制表示的字符
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(source);
            //MD5的计算结果是一个128位的长整数，用字节表示就是16个字节
            byte[]tmp=messageDigest.digest();
            //用十六进制表示，一个字节占两个字符长度。
            char str[]=new char[16*2];
            //转换结果中对应的字符位置
            int k=0;
            //从第一个字节开始，将MD5的每一个字节转换成十六进制字符
            for(int i=0;i<16;i++){
                //取出第i个字节
                byte byte0=tmp[i];
                //>>>逻辑右移，将符号位一起右移
                str[k++]=hexDigits[byte0>>>4&0xf];
                //取字节中低４位的数字转换
                str[k++]=hexDigits[byte0&0xf];
            }
            //将换后的结果转换为字符串
            s=new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
}
