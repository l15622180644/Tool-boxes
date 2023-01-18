package com.lzk.toolboxes.utils;



import com.alibaba.fastjson.JSON;
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
    public static <T> List<T> listToTree(Class<T> tClass,String jsonArray, String pidName, String idName, String childName, String sortName, long rootFlag){
        Map<String,Object> map = new HashMap<>();
        map.put("sortName",sortName);
        threadLocal.set(map);
        List<JSONObject> list = JSONArray.parseArray(jsonArray).toJavaList(JSONObject.class);
        List<JSONObject> rootList = getRootList(list, pidName, rootFlag);
        List<T> convertList = new ArrayList<>();
        rootList.forEach(v->{
            v.put(childName,getChildren(v.getLong(idName),list,pidName,idName,childName));
            convertList.add(v.toJavaObject(tClass));
        });
        return convertList;
    }


    private static List<JSONObject> getChildren(Long pid, List<JSONObject> list, String pidName, String idName, String childName){
        List<JSONObject> children = new ArrayList<>();
        Iterator<JSONObject> iterator = list.iterator();
        threadLocal.get().put("iterator",iterator);
        while ((iterator = (Iterator)(threadLocal.get().get("iterator"))).hasNext()){
            JSONObject jsonObject = iterator.next();
            if(jsonObject.getLongValue(pidName)==pid){
                children.add(jsonObject);
                iterator.remove();
                jsonObject.put(childName,getChildren(jsonObject.getLong(idName),list,pidName,idName,childName));
            }
        }
        threadLocal.get().put("iterator",list.iterator());//递归完需返回最新的迭代器
        return threadLocal.get().get("sortName")!=null?children.stream().sorted(Comparator.comparing(TreeUtils::comparingBySortName)).collect(Collectors.toList()):children;
    }

    private static List<JSONObject> getRootList(List<JSONObject> list, String pidName, long rootFlag){
        List<JSONObject> rootList = new ArrayList<>();
        Iterator<JSONObject> iterator = list.iterator();
        while (iterator.hasNext()){
            JSONObject jsonObject = iterator.next();
            if(jsonObject.getLongValue(pidName)==rootFlag){
                rootList.add(jsonObject);
                iterator.remove();
            }
        }
        return threadLocal.get().get("sortName")!=null?rootList.stream().sorted(Comparator.comparing(TreeUtils::comparingBySortName)).collect(Collectors.toList()):rootList;
    }

    private static Integer comparingBySortName(JSONObject jsonObject){
        return jsonObject.getIntValue(threadLocal.get().get("sortName").toString());
    }

    public static int getDeep(List<JSONObject> treeList, String childName){
        if(treeList==null||treeList.isEmpty()){
            return 0;
        }
        int deep = 1;
        for (int i = 0; i < treeList.size(); i++) {
            int floors = getDeepOfRoot(treeList.get(i),childName, 1, new ArrayList<>());
            if(floors > deep) deep = floors;
        }
        return deep;
    }

    private static int getDeepOfRoot(JSONObject jsonObject, String childName, int deep, List<Integer> array){
        List<JSONObject> children = jsonObject.getObject(childName, List.class);
        if(!children.isEmpty()) {
            deep++;
            for (JSONObject child : children) {
                getDeepOfRoot(child,childName,deep,array);
            }
        }else{
            array.add(deep);
        }
        return array.stream().mapToInt(Integer::intValue).max().getAsInt();
    }

    //获取最底部子个数
    public static <T> List<T> getFloorsChildNum(Class<T> tClass, List<JSONObject> list, String childName, String numName){
        list.forEach(v ->{
            getFloorsChildNumOfRoot(v,childName,numName,new ArrayList<>());
        });
        return JSONArray.parseArray(JSON.toJSONString(list),tClass);
    }

    public static void getFloorsChildNumOfRoot(JSONObject jsonObject, String childName, String numName, List<Integer> array){
        List<JSONObject> children = jsonObject.getObject(childName, List.class);
        if(children.isEmpty()){
            array.add(1);
        }else {
            for (JSONObject child : children) {
                getFloorsChildNumOfRoot(child,childName,numName,array);
                getFloorsChildNumOfRoot(child,childName,numName,new ArrayList<>());
            }
        }
        jsonObject.put(numName,array.stream().mapToInt(Integer::intValue).sum());
    }


}
