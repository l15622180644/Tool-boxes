import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzk.toolboxes.ToolBoxesApplication;
import com.lzk.toolboxes.config.base.BaseParam;
import com.lzk.toolboxes.entity.ExportEntity;
import com.lzk.toolboxes.entity.ExportEntity2;
import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.*;
import com.lzk.toolboxes.utils.easyExcel.EasyExcelUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author
 * @module
 * @date 2021/6/19 14:28
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ToolBoxesApplication.class)
public class Test<T> {


    @org.junit.Test
    public void show(){
        Map map = new HashMap();
        map.put("version","2.0.33");
        map.put("log",Arrays.asList(
            "1.解决简易办案违法行为输入受限问题。"
        ));
        System.out.println(JSON.toJSONString(map));
    }

    @org.junit.Test
    public void show1() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(1640966400 * 1000L);
//        Date date = format.parse("2022-05-11 10:00:00");
        System.out.println(format.format(date));
    }

    @org.junit.Test
    public void show2() throws ParseException {
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动服务器....");
            while (true){
                Socket s = ss.accept();
                System.out.println("客户端:"+s.getInetAddress().getLocalHost()+"已连接到服务器");

                new Thread(() ->{
                    BufferedReader br = null;
                    BufferedWriter bw = null;
                    try {
                        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        //读取客户端发送来的消息
                        String mess = br.readLine();
                        System.out.println("客户端："+mess);
                        int i=0;
                        while (!s.isClosed()){
                            Thread.sleep(2000);
                            i+=1;
                            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            bw.write("嘿嘿嘿"+i+"\n");
                            bw.flush();
                        }
                    } catch (InterruptedException | IOException e) {
                    } finally {
                        try {
                            br.close();
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void show3() throws IOException {
        ThreadUtil.concurrencyTest(30,()->{
            try {
                HttpResponse response = HttpUtils.doPost("http://localhost:8090", "/userService/t", "POST", new HashMap<>(), null, new HashMap<>());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @org.junit.Test
    public void show4() throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream("E:\\lzk\\代码生成\\" + System.currentTimeMillis() + ".xlsx");
        EasyExcelUtil.writeExcelWithSheets(outputStream,new String[]{"a","b"},ExportEntity.class,new List<?>[]{setData(),setData()});

    }

    private List<?> setData(){
        List<ExportEntity> data = new ArrayList<>();
        for(int i=0;i<10;i++){
            data.add(new ExportEntity(i+"_1",i+"_2",i+"_3"));
        }
        return data;
    }

    private List<?> setData2(){
        List<ExportEntity2> data = new ArrayList<>();
        for(int i=0;i<10;i++){
            data.add(new ExportEntity2(i+"_1",i+"_2",i+"_3"));
        }
        return data;
    }

    @org.junit.Test
    public void show5(){
        ArrayList<String> list = new ArrayList<>();
        list.add("002");
        list.add("001");
        List<String> collect = list.stream().sorted().collect(Collectors.toList());
        System.out.println(collect);
    }

    @org.junit.Test
    public void show6(){
        System.out.println(AesUtil.enCode("jp102"));
        System.out.println(AesUtil.enCode("123456"));
    }


}
