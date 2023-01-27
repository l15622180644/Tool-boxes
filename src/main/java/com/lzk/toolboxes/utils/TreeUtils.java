package com.lzk.toolboxes.utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzk.toolboxes.entity.Eval;
import com.lzk.toolboxes.entity.ExcelTreeName;

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

    //获取树层数
    public static int getDeep(List<JSONObject> treeList, String childName){
        if(treeList==null||treeList.isEmpty()){
            return 0;
        }
        int deep = 1;
        for (int i = 0; i < treeList.size(); i++) {
            int floors = getDeepOfRoot(treeList.get(i),childName);
            if(floors > deep) deep = floors;
        }
        return deep;
    }

    public static int getDeepOfRoot(JSONObject jsonObject, String childName){
        return getDeepOfRoot(jsonObject,childName,1,new ArrayList<>());
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

    //获取最底部页子个数
    public static int getFloorsChildNumOfRoot(JSONObject jsonObject, String childName){
        List<JSONObject> children = jsonObject.getObject(childName, List.class);
        if(children.isEmpty()){
            return 0;
        }
        return getFloorsChildNumOfRoot(jsonObject, childName, new ArrayList<>());
    }

    private static int getFloorsChildNumOfRoot(JSONObject jsonObject, String childName, List<Integer> array){
        List<JSONObject> children = jsonObject.getObject(childName, List.class);
        if(children.isEmpty()){
            array.add(1);
        }else {
            for (JSONObject child : children) {
                getFloorsChildNumOfRoot(child,childName,array);
                getFloorsChildNumOfRoot(child,childName,new ArrayList<>());
            }
        }
        return array.stream().mapToInt(Integer::intValue).sum();
    }

    //树型转list
    public static <T> List<T> treeToListForExcel(Class<T> tClass,List<T> treeList,int treeDeep,String childName,String treeName,String treeNameListName){
        List<T> convertList = new ArrayList<>();
        treeList.forEach(v -> {
            List<JSONObject> result = new ArrayList<>();
            treeToListForExcelOfRoot(JSONObject.parseObject(JSON.toJSONString(v)),new ArrayList(),result,childName,treeName,treeNameListName);
            result.forEach(v1 -> {
                List<ExcelTreeName> treeNameList = v1.getObject(treeNameListName, List.class);
                ExcelTreeName excelTreeName = treeNameList.get(treeNameList.size() - 1);
                if(treeNameList.size() < treeDeep) excelTreeName.setMergeAcross(treeDeep - treeNameList.size());
                convertList.add(v1.toJavaObject(tClass));
            });
        });
        return convertList;
    }

    private static void treeToListForExcelOfRoot(JSONObject root, List<ExcelTreeName> nameArray, List result, String childName, String treeName, String treeNameListName){

        List<JSONObject> children = root.getObject(childName,List.class);
        if(children.isEmpty()){
            List<ExcelTreeName> newArray = new ArrayList();
            newArray.addAll(nameArray);
            newArray.add(new ExcelTreeName(
                    root.getString(treeName),
                    getFloorsChildNumOfRoot(root,childName)
            ));
            root.put(treeNameListName,newArray);
            result.add(root);
        }else{
            for (int i = 0; i < children.size(); i++) {
                List<ExcelTreeName> newArray = new ArrayList();
                newArray.addAll(nameArray);
                if(i==0){
                    int num = getFloorsChildNumOfRoot(root, childName);
                    newArray.add(new ExcelTreeName(
                            root.getString(treeName),
                            num > 0 ? num-1 : num
                    ));
                }else {
                    newArray.add(new ExcelTreeName(
                            root.getString(treeName),
                            0
                    ));
                }
                treeToListForExcelOfRoot(children.get(i),newArray,result,childName,treeName,treeNameListName);
            }
        }
    }


}
