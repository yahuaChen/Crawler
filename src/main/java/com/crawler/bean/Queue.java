package com.crawler.bean;

import java.util.LinkedList;

/**
 * 保存将要访问的URL
 * Created by 金水信息 on 2017/11/1.
 */
public class Queue{
    private LinkedList queue=new LinkedList();

    /**
     * 入队列
     * @param obj
     */
    public void enQueue(Object obj){
        queue.addLast(obj);
    }

    /**
     * 出队列
     * @return
     */
    public Object deQueue(){
        return queue.removeFirst();
    }

    /**
     * 判断是否为空
     * @return
     */
    public boolean isEmpty(){
        return queue.isEmpty();
    }

    /**
     * 判断是否包含目标对象
     * @param obj
     * @return
     */
    public boolean contians(Object obj){
        return queue.contains(obj);
    }

    public int size(){
        return queue.size();
    }
}
