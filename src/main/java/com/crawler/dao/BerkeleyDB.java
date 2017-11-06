package com.crawler.dao;

import com.crawler.main.Crawler;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import java.io.File;

/**
 * Created by 金水信息 on 2017/11/1.
 */
public class BerkeleyDB {
    /**
     * 数据库文件和日志文件指定目录
     */
    private static final String ENV_DIR="E:\\BerkeleyDBData\\";
    private static Environment environment;
    private static StoredClassCatalog catalog;

    static{
        EnvironmentConfig envConfig=new EnvironmentConfig();
        //当数据库环境不存在的时候，创建一个数据库环境，默认为false.
        envConfig.setTransactional(true);
        //当数据库环境不存在的时候，创建一个数据库环境，默认为false.
        envConfig.setAllowCreate(true);

        environment=new Environment(new File(ENV_DIR),envConfig);
        System.out.println("Env Config: " + environment.getConfig());
//        environment.sync();
//        environment.close();
//        environment=null;
    }

    /**
     * 创建数据库
     */
    private void createDataBase(){
        String dataBaseName="ToDoTaskList.do";
        DatabaseConfig dbConfig=new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(false);


        Database db=environment.openDatabase(null,"classDb",dbConfig);
        catalog=new StoredClassCatalog(db);
        TupleBinding keyBingding=TupleBinding.getPrimitiveBinding(String.class);
        SerialBinding valueBingding=new SerialBinding(catalog,Crawler.class);
    }

}
