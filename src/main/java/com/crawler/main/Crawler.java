package com.crawler.main;

import com.crawler.bean.LinkFilter;
import com.crawler.service.LinkQueue;
import com.crawler.util.DownLoadFile;
import com.crawler.util.HtmlParserTool;
import org.apache.commons.httpclient.HttpStatus;

import java.util.Set;

/**
 * Created by 金水信息 on 2017/11/1.
 */
public class Crawler {
    /**
     * 使用种子初始化URL队列
     * @param seeds
     */
    private void initCrawlerWithSeeds(String[] seeds){
        for(String link:seeds){
            LinkQueue.addUnvisitedUrl(link);
        }
    }

    public void crawling(String[] seeds){
        LinkFilter filter=new LinkFilter() {
            public boolean accept(String url) {
                if(url.startsWith("http://www.csdn.net/"))
                    return true;
                else
                    return false;
            }
        };

        initCrawlerWithSeeds(seeds);

        while (!LinkQueue.unVisitedUrlIsEmpty()){
            String unVisitedUrl= (String) LinkQueue.unVisitedUrlDeQueue();
            DownLoadFile downLoader=new DownLoadFile();
            int statusCode=downLoader.downloadFile(unVisitedUrl);
            LinkQueue.addVisitedUrl(unVisitedUrl);
            if(statusCode== HttpStatus.SC_OK){
                Set<String> links= HtmlParserTool.extracLinks(unVisitedUrl,filter);
                int addNum=0;
                for(String link:links){
                    if(LinkQueue.addUnvisitedUrl(link)){
                        addNum++;
                    }
                }
                System.out.println("提取["+unVisitedUrl+"]页面中"+addNum+"个新的链接。");
                System.out.println("当前未处理链接："+LinkQueue.getUnVisitedUrlNum()+"个。");
            }
        }
    }

    public static void main(String[] args) {
        Crawler crawler=new Crawler();
        crawler.crawling(new String[]{"http://www.csdn.net/"});
    }
}
