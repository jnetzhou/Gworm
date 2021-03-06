package com.gyak.gworm;

import com.google.gson.JsonObject;
import com.gyak.url.UrlGeneration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用以进行并发爬取
 * @author  <a href="http://guiyanakuang.com">geek'喵</a>
 * on 2015-05-31.
 */
public abstract class GwormAction {

    private int taskNum;
    private UrlGeneration urlGeneration;
    private GwormCoordinate coordinate;

    /**
     * GwormAction构造函数
     * @param taskNum 并发数
     * @param urlGeneration url生成器
     * @param coordinate 爬取参数坐标
     */
    public GwormAction(int taskNum, UrlGeneration urlGeneration, GwormCoordinate coordinate) {
        this.taskNum = taskNum;
        this.urlGeneration = urlGeneration;
        this.coordinate = coordinate;
    }

    /**
     * 启动并发
     */
    public void work() {
        ExecutorService exec = Executors.newFixedThreadPool(taskNum);
        for(int i=urlGeneration.getStart();i<=urlGeneration.getEnd();i++) {
            final Object bind = urlGeneration.getCurrentbindObj();
            exec.execute(new GwormCallBack(coordinate, bind) {
                @Override
                void callBack(JsonObject json, Object bind) {
                    action(json, bind);
                }

            });
            urlGeneration.next();
        }
        exec.shutdown();
    }


    public abstract void action(JsonObject json, Object bindObj);
}
