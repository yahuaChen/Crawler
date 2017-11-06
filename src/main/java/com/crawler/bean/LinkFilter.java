package com.crawler.bean;

/**
 * URL内容过滤器
 * Created by 金水信息 on 2017/11/1.
 */
public interface LinkFilter {
    public boolean accept(String url);
}
