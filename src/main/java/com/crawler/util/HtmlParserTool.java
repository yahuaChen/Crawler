package com.crawler.util;

import com.crawler.bean.LinkFilter;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 金水信息 on 2017/11/1.
 */
public class HtmlParserTool {
    /**
     * 获取一个网页上的链接
     * @param url
     * @param filter 用于过滤链接
     * @return
     */
    public static Set<String> extracLinks(String url, LinkFilter filter){
        Set<String> links=new HashSet<String>();
        try {
            Parser parser=new Parser(url);
            parser.setEncoding("UTF-8");


            //过滤<frame>标签的filter，用来提取frame标签里面的src属性
            NodeFilter frameFilter=new NodeFilter() {
                public boolean accept(Node node) {
                    if(node.getText().startsWith("frame src=")||node.getText().startsWith("img src=")){
                        return true;
                    }else{
                        return false;
                    }
                }
            };

            //设置<a>和<frame>标签过滤
            OrFilter linkFilter=new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);

            //得到所有经过过滤的标签
            NodeList list=parser.extractAllNodesThatMatch(linkFilter);
            for (int i=0;i<list.size();i++){
                Node tag=list.elementAt(i);
                if(tag instanceof LinkTag){
                    //<a>标签
                    LinkTag link=(LinkTag)tag;
                    if(filter.accept(link.getLink())){
                        links.add(link.getLink());
                        //System.out.println("添加一个a标签路径");
                    }
                }else if(tag.getText().startsWith("img src=")){
                    String img=tag.getText().substring(tag.getText().indexOf("src=")+4);
                    links.add(img.replaceAll("\"",""));
                }else{
                    //<frame>标签
                    String frame=tag.getText().substring(tag.getText().indexOf("src="));
                    int end=frame.indexOf(" ");
                    if(end==-1){
                        end=frame.indexOf(">");
                    }
                    String frameUrl=frame.substring(5,end-1);
                    if(filter.accept(frameUrl)){
                        links.add(frameUrl);
                        //System.out.println("添加一个frame标签路径");
                    }
                }
            }

        } catch (ParserException e) {
            e.printStackTrace();
        }
        return links;
    }
}
