package com.lzk.toolboxes.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SseServer {

    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId){
        try {
            //0不过期，单位：毫秒
            SseEmitter sseEmitter = new SseEmitter(10000L);
            //设置客户端重新连接时间，单位：毫秒
            sseEmitter.send(SseEmitter.event().reconnectTime(3000L).data("连接成功"));
            //关闭回调
            sseEmitter.onCompletion(() -> close(userId));
            //异常回调
            sseEmitter.onError(Throwable::printStackTrace);
            //超时回调
            sseEmitter.onTimeout(() -> {});
            sseEmitterMap.put(userId,sseEmitter);
            return sseEmitter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendMsgToOne(String userId,String msg){
        if(sseEmitterMap.containsKey(userId)){
            try {
                sseEmitterMap.get(userId).send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMsgToAll(String  msg){
        sseEmitterMap.values().forEach(sseEmitter -> {
            try {
                sseEmitter.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void close(String userId){
        if(sseEmitterMap.containsKey(userId)){
            sseEmitterMap.get(userId).complete();
            sseEmitterMap.remove(userId);
        }
    }

}
