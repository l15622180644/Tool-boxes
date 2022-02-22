package com.lzk.toolboxes.utils;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtils {

    private TreeUtils(){}

    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal();

    /**
     * list转树结构
     * @param jsonArray     数据集合
     * @param pidName       【parentId】字段名
     * @param idName        【主键】字段名
     * @param childName     【子】字段名
     * @param sortName      【排序】字段名，不排则传null
     * @param rootFlag      根节点标记
     * @return
     */
    public static List<JSONObject> listToTree(String jsonArray,String pidName,String idName,String childName,String sortName,long rootFlag){
        Map<String,Object> map = new HashMap<>();
        map.put("sortName",sortName);
        threadLocal.set(map);
        List<JSONObject> list = JSONArray.parseArray(jsonArray).toJavaList(JSONObject.class);
        List<JSONObject> rootList = getRootList(list, pidName, rootFlag);
        rootList.forEach(v->{
            v.put(childName,getChildren(v.getLong(idName),list,pidName,idName,childName));
        });
        return rootList;
    }


    private static List<JSONObject> getChildren(Long pid,List<JSONObject> list,String pidName,String idName,String childName){
        List<JSONObject> children = new ArrayList<>();
        Iterator<JSONObject> iterator = list.iterator();
        threadLocal.get().put("iterator",iterator);
        while ((iterator = (Iterator)(threadLocal.get().get("iterator"))).hasNext()){
            JSONObject jsonObject = iterator.next();
            if(jsonObject.getLongValue(pidName)==pid){
                children.add(jsonObject);
                iterator.remove();//减少循环次数提升性能
                jsonObject.put(childName,getChildren(jsonObject.getLong(idName),list,pidName,idName,childName));
            }
        }
        threadLocal.get().put("iterator",list.iterator());//递归完需返回最新的迭代器
        return threadLocal.get().get("sortName")!=null?children.stream().sorted(Comparator.comparing(TreeUtils::comparingBySortName)).collect(Collectors.toList()):children;
    }

    private static List<JSONObject> getRootList(List<JSONObject> list,String pidName,long rootFlag){
        List<JSONObject> rootList = new ArrayList<>();
        Iterator<JSONObject> iterator = list.iterator();
        while (iterator.hasNext()){
            JSONObject jsonObject = iterator.next();
            if(jsonObject.getLongValue(pidName)==rootFlag){
                rootList.add(jsonObject);
                iterator.remove();//减少循环次数提升性能
            }
        }
        return threadLocal.get().get("sortName")!=null?rootList.stream().sorted(Comparator.comparing(TreeUtils::comparingBySortName)).collect(Collectors.toList()):rootList;
    }

    private static Integer comparingBySortName(JSONObject jsonObject){
        return jsonObject.getIntValue(threadLocal.get().get("sortName").toString());
    }


}
