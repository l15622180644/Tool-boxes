import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzk.toolboxes.entity.Eval;
import com.lzk.toolboxes.mapper.EvalMapper;
import com.lzk.toolboxes.utils.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author
 * @module
 * @date 2021/6/19 14:28
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ToolBoxesApplication.class)
public class Test<T> {

    @Resource
    private EvalMapper evalMapper;
    @Resource
    private Configuration configuration;

    @org.junit.Test
    public void show() {
        Map map = new HashMap();
        map.put("version", "2.0.56");
        map.put("log", Arrays.asList(
                "1.新增专项任务文书签章功能。"
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
        System.out.println(new Date().getTime() / 1000);
    }

    @org.junit.Test
    public void show2() throws ParseException {
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动服务器....");
            while (true) {
                Socket s = ss.accept();
                System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");

                new Thread(() -> {
                    BufferedReader br = null;
                    BufferedWriter bw = null;
                    try {
                        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        //读取客户端发送来的消息
                        String mess = br.readLine();
                        System.out.println("客户端：" + mess);
                        int i = 0;
                        while (!s.isClosed()) {
                            Thread.sleep(2000);
                            i += 1;
                            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            bw.write("嘿嘿嘿" + i + "\n");
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
    public void setData2() {
        Map map = new HashMap();
        System.out.println(map.get("!"));
    }

    @org.junit.Test
    public void show5() {
        System.out.println(AesUtil.enCode("JPW_admin"));
        System.out.println(AesUtil.enCode("JPW123@321"));
        System.out.println(AesUtil.enCode("admin"));
        System.out.println(AesUtil.enCode("123456"));
    }

    @org.junit.Test
    public void show39(){
        List<Eval> evals = evalMapper.selectList(Wrappers.query());
        List<Eval> tree = TreeUtils.listToTree(Eval.class, JSON.toJSONString(evals), "parentId", "id", "children", "sort", -1);
        List<JSONObject> list = JSONArray.parseArray(JSON.toJSONString(tree), JSONObject.class);
        List<Eval> floorsChildNum = TreeUtils.getFloorsChildNum(Eval.class, list, "children", "floorsNum");
        int deep = TreeUtils.getDeep(list, "children");
        Map<String,Object> map = new HashMap<>();
        map.put("tree",floorsChildNum);
        map.put("treeDeep",deep>0 ? deep-1 : 0);
        try {
            FileWriter writer = new FileWriter("E:\\lzk\\代码生成\\rubbish\\hello.xlsx");
            Template template = configuration.getTemplate("parent.ftl");
            template.process(map,writer);
            writer.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }





}
