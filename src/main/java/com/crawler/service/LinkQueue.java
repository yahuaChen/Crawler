package com.crawler.service;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;

/**
 * 保存已经访问过的URL
 * Created by 金水信息 on 2017/11/1.
 */
public class LinkQueue {
    //已访问过的URL队列
    private static Set visitedUrl=new HashSet();
    //待访问的URL队列
    private static Queue unVisitedUrl=new PriorityQueue();

    /**
     * 获得待访问的URL队列
     * @return
     */
    public static Queue getUnVisitedUrl(){
        return  unVisitedUrl;
    }

    /**
     * 添加到访问过的URL队列
     * @param url
     */
    public static void addVisitedUrl(String url){
        visitedUrl.add(url);
    }

    /**
     * 移除待访问的URL
     * @param url
     */
    public static void removeVisitedUrl(String url){
        visitedUrl.remove(url);
    }

    /**
     * 待访问的URL出列
     * @return
     */
    public static Object unVisitedUrlDeQueue(){
        return unVisitedUrl.poll();
    }


    /**
     * 添加到待访问的URL队列，保证每个URL只被访问一次
     * @param url
     * @return
     */
    public static boolean addUnvisitedUrl(String url){
        if(url!=null&&!url.trim().equals("")&&!visitedUrl.contains(url)&&!unVisitedUrl.contains(url)){
            unVisitedUrl.add(url);
            return true;
        }
        return false;
    }

    /**
     * 获得已访问的URL数目
     * @return
     */
    public static int getVisitedUrlNum(){
        return visitedUrl.size();
    }

    /**
     * 获得待访问的URL数目
     * @return
     */
    public static int getUnVisitedUrlNum(){
        return unVisitedUrl.size();
    }

    /**
     * 判断待访问的URL队列是否为空
     * @return
     */
    public static boolean unVisitedUrlIsEmpty(){
        return unVisitedUrl.isEmpty();
    }
}
