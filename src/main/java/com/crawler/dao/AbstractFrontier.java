package com.crawler.dao;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 操作BerkeleyDB
 * Created by 金水信息 on 2017/11/1.
 */
public abstract class AbstractFrontier {
    private Environment env;
    private static final String CLASS_CATALOG="java_class_catalog";
    protected StoredClassCatalog javaCatalog;
    protected Database catalogdatabase;
    protected Database database;

    /**
     * 构造方法初始化环境及数据库
     * @param homeDirectory
     * @throws DatabaseException
     * @throws FileNotFoundException
     */
    public AbstractFrontier(String homeDirectory) throws DatabaseException,FileNotFoundException{
        //打开env
        EnvironmentConfig envConfig=new EnvironmentConfig();
        //当数据库环境不存在的时候，创建一个数据库环境，默认为false.
        envConfig.setTransactional(true);
        //当数据库环境不存在的时候，创建一个数据库环境，默认为false.
        envConfig.setAllowCreate(true);
        env=new Environment(new File(homeDirectory),envConfig);

        //设置DatabaseConfig
        DatabaseConfig dbConfig=new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(true);
        //不要求能够存储重复的关键字
        dbConfig.setSortedDuplicates(false);
        //打开
        catalogdatabase=env.openDatabase(null,CLASS_CATALOG,dbConfig);
        javaCatalog=new StoredClassCatalog(catalogdatabase);
        //设置DatabaseConfig
        DatabaseConfig dbConfig0=new DatabaseConfig();
        dbConfig0.setTransactional(true);
        dbConfig0.setAllowCreate(true);
        //打开
        database=env.openDatabase(null,"URL",dbConfig0);
    }

    /**
     * 关闭数据库，关闭环境
     * @throws DatabaseException
     */
    public void close()throws  DatabaseException{
        database.close();
        javaCatalog.close();
        env.close();
    }

    /**
     * put方法
     * @param key
     * @param value
     */
    protected abstract  void put(Object key,Object value);

    /**
     * get方法
     * @param key
     * @return
     */
    protected abstract Object get(Object key);

    /**
     * delete方法
     * @param key
     * @return
     */
    protected abstract Object delete(Object key);
}
