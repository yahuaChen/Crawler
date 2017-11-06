package com.crawler.dao;

import com.crawler.bean.CrawlUrl;
import com.crawler.service.Frontier;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by yahua on 2017/11/1.
 */
public class BDBFrontier extends AbstractFrontier implements Frontier{

    private StoredMap pendingUrisDB=null;

    /**
     * 构造方法初始化环境及数据库
     * @param homeDirectory
     * @throws DatabaseException
     * @throws FileNotFoundException
     */
    public BDBFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException {
        super(homeDirectory);
        EntryBinding keyBinding=new SerialBinding(javaCatalog, String.class);
        EntryBinding valueBinding=new SerialBinding(javaCatalog,CrawlUrl.class);
        pendingUrisDB=new StoredMap(database,keyBinding,valueBinding,true);
    }

    /**
     * 获取下一条记录
     * @return
     * @throws Exception
     */
    public CrawlUrl getNext() throws Exception {
        CrawlUrl result=null;
        if(!pendingUrisDB.isEmpty()){
            Map.Entry<String,CrawlUrl> entry=(Map.Entry<String,CrawlUrl>)pendingUrisDB.entrySet().iterator().next();
            result=entry.getValue();
            delete(entry.getKey());
        }
        return result;
    }

    /**
     * 存入URL
     * @param url
     * @return
     */
    public boolean putUrl(CrawlUrl url) {
        put(url.getOriUrl(),url);
        return true;
    }

    /**
     * 存入数据库的方法
     * @param key
     * @param value
     */
    protected void put(Object key, Object value) {
        pendingUrisDB.put(key,value);
    }

    /**
     * 取出
     * @param key
     * @return
     */
    protected Object get(Object key) {
        return pendingUrisDB.get(key);
    }

    /**
     * 删除
     * @param key
     * @return
     */
    protected Object delete(Object key) {
        return pendingUrisDB.remove(key);
    }

    /**
     * 根据URL计算键值，可以使用各种压缩算法，包括MD5等压缩算法。
     * @param url
     * @return
     */
    private String caculateUrl(String url){
        return url;
    }

    /**
     * 测试函数
     * @param args
     */
    public static void main(String[] args) {
        try {
            BDBFrontier bdbFrontier=new BDBFrontier("E:\\BerkeleyDBData");
            CrawlUrl url=new CrawlUrl();
            url.setOriUrl("http://www.163.com");
            bdbFrontier.putUrl(url);
            System.out.println(((CrawlUrl)bdbFrontier.getNext()).getOriUrl());
            bdbFrontier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
