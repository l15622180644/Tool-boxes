package com.lzk.toolboxes.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * DateTime: 2016/9/18 21:04
 * 功能：文件操作工具
 * 思路：
 */
public class FileUtil {
    public static void main(String[] args) {
        delFolder("G:/demo/dira"); //删除dira文件夹下的文件，dira也会删除
        delAllFile("G:/demo/dira"); //只删除dira文件夹下的文件，dira不会删除
    }

    /**
     *创建目录
     * @param destDirName 目标目录名
     * @return
     */
    public static boolean createDir(String destDirName){
        File dir=new File(destDirName);
        if(dir.exists()){
            return  false;
        }
        if(!destDirName.endsWith(File.separator)){  //如果结尾没有文件分隔符
            destDirName+=File.separator;
        }
        //创建单个目录
        if(dir.mkdir()){
            return true;
        }else return false;

    }

    /**
     * 删除文件
     * @param filePathAndName   文件路径及名称 如c:/fqf.txt
     */
    public static void delFile(String filePathAndName){
        try {
            String filePath=filePathAndName;
            filePath=filePath.toString();
            File myDelFile=new File(filePath);
            myDelFile.delete();

        }catch ( Exception e ){
            System.out.println("删除文件出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path  文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path){
        boolean flag=false;
        File file=new File(path);
        if(!file.exists()){ //如果不存在
            return  flag;
        }
        if(!file.isDirectory()){    //如果不是文件夹
            return flag;
        }

        String[] tempList=file.list();
        File temp=null;

        for ( int i = 0; i <tempList.length ; i++ ) {

            if(path.endsWith(File.separator)){  //File.separator文件分隔符
                temp=new File(path+tempList[i]);
            }else {
                temp=new File(path+File.separator+tempList[i]);
            }

            if(temp.isFile()){
                temp.delete();
            }
            if(temp.isDirectory()){
                delAllFile(path+"/"+tempList[i]);//先删除文件夹里边的文件
                delFolder(path+"/"+tempList[i]);//再删除空文件夹
                flag=true;
            }
        }//end of  for ( int i = 0; i <tempList.length ; i++ ) {
        return flag;
    }

    /**
     * 只删除此路径的最末路径下所有文件和文件夹
     * @param folderPath 文件路径 (
     */
    private static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath=folderPath;
            filePath=filePath.toString();
            File myFilePath=new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * 读取到字节数组0
     * @param filePath  路径
     * @return
     * @throws IOException
     */
    public static byte[] getContent(String filePath) throws IOException{
        File file=new File(filePath);
        long fileSize=file.length();
        if(fileSize>Integer.MAX_VALUE){
            System.out.println("文件太大....");
            return null;
        }

        FileInputStream fi=new FileInputStream(file);
        byte[] buffer=new byte[(int)fileSize];
        int offset=0;
        int numRead=0;
        while ( offset<buffer.length &&(numRead=fi.read(buffer,offset,buffer.length-offset))>=0 ){
            offset+=numRead;
        }
        //确保所有数据均被读完
        if(offset!=buffer.length){
            throw new IOException("不能全部读取文件:"+file.getName());
        }
        fi.close();
        return  buffer;
    }

    /**
     * 读取到字节数组1
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException{
        File f=new File(filePath);
        if(!f.exists()){
            throw new FileNotFoundException("文件不能找到:"+filePath);
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream((int)f.length());
        BufferedInputStream in=null;

        try {
            in=new BufferedInputStream(new FileInputStream(f));
            int buf_size=1024;
            byte[] buffer=new byte[buf_size];
            int len=0;
            while ( -1!=(len=in.read(buffer,0,buf_size)) ){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }catch ( IOException e ){
            e.printStackTrace();
            throw e;
        }finally {
            in.close();
            bos.close();
        }

    }


    public static byte[] toByteArray2(String filePath) throws IOException{
        File f=new File(filePath);
        if(!f.exists()){
            throw new FileNotFoundException("文件不能找到:"+filePath);
        }
        FileChannel channel=null;   //Java NIO中的FileChannel是一个连接到文件的通道。可以通过文件通道读写文件。FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
        FileInputStream fs=null;
        try {
            fs=new FileInputStream(f);
            channel=fs.getChannel();
            ByteBuffer byteBuffer=ByteBuffer.allocate((int)channel.size());
            while ( (channel.read(byteBuffer)) >0 ){

            }
            return byteBuffer.array();
        }catch ( IOException e ){
            e.printStackTrace();
            throw e;
        }finally {
            channel.close();
            fs.close();
        }

    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filePath) throws IOException{
        FileChannel fc=null;
        RandomAccessFile rf=null;

        try {
            rf=new RandomAccessFile(filePath,"r");
            fc=rf.getChannel();
            MappedByteBuffer byteBuffer=fc.map(FileChannel.MapMode.READ_ONLY,0,fc.size()).load();
            byte[] result=new byte[(int)fc.size()];
            if(byteBuffer.remaining()>0){
                byteBuffer.get(result,0,byteBuffer.remaining());
            }
            return result;
        }catch ( IOException e ){
            e.printStackTrace();
            throw e;
        }finally {
            rf.close();
            fc.close();
        }

    }


}
