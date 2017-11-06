package com.crawler.service;

import com.crawler.bean.CrawlUrl;

/**
 * Created by 金水信息 on 2017/11/1.
 */
public interface Frontier {
    /**
     * 获取下一条记录
     * @return
     * @throws Exception
     */
    public CrawlUrl getNext()throws Exception;

    /**
     * 存入URL
     * @param url
     * @return
     */
    public boolean putUrl(CrawlUrl url);
}
