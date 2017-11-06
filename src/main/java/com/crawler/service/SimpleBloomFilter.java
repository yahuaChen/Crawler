package com.crawler.service;

import com.crawler.bean.CrawlUrl;

import java.util.BitSet;

/**
 * 布隆过滤器，用于检测已访问的链接
 * Created by 金水信息 on 2017/11/2.
 */
public class SimpleBloomFilter implements VisitedFrontier{
    // 二进制为：‭0001000000000000000000000000‬
    // 十进制为：‭16777216‬
    private static final int DEFAULT_SIZE=2<<24;
    //随机种子
    private static final int[] seeds=new int[]{7,11,13,31,37,61};
    //一千六百万的向量
    private BitSet bits=new BitSet(DEFAULT_SIZE);
    //用于储存随机数产生器
    private SimpleHash[] func=new SimpleHash[seeds.length];

    public SimpleBloomFilter(){
       for (int i=0;i<seeds.length;i++){
           func[i]=new SimpleHash(DEFAULT_SIZE,seeds[i]);
       }
    }

    /**
     * 覆盖方法，把URL添加进来
     * @param value
     */
    public void add(CrawlUrl value){
        if(value!=null){
            add(value.getOriUrl());
        }
    }

    /**
     * 覆盖方法，把URL添加进来
     * @param value
     */
    public void add(String value){
        //录入信息指纹
        for(SimpleHash f:func){
            bits.set(f.hash(value),true);
        }
    }

    /**
     * 覆盖方法，是否包含URL
     * @param value
     * @return
     */
    public boolean contains(CrawlUrl value){
        return contains(value.getOriUrl());
    }

    /**
     * 覆盖方法，是否包含URL
     * @param value
     * @return
     */
    public boolean contains(String value){
        if(value==null){
            return false;
        }
        boolean ret=true;
        //比较指纹
        for(SimpleHash f:func){
            ret=ret&&bits.get(f.hash(value));
        }
        return ret;
    }

    /**
     * 随机数生成器
     */
    public static class SimpleHash{
        private int cap;
        private int seed;
        public SimpleHash(int cap,int seed){
            this.cap=cap;
            this.seed=seed;
        }
        public int hash(String value){
            int result=0;
            int len=value.length();
            //针对value，结合seed和cap生成一个数字。
            for(int i=0;i<len;i++){
                result=seed*result+value.charAt(i);
            }
            return (cap-1)&result;
        }
    }
}
