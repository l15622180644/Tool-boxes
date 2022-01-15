package com.lzk.toolboxes.utils;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaptchaUtils {

    private CaptchaUtils(){}

    private static final List<Color> myColorList = new ArrayList<>();

    private static final Random random;

    static {
        random = new Random();
        myColorList.add(Color.WHITE);
        myColorList.add(new Color(255,255,204));
        myColorList.add(new Color(255,204,204));
        myColorList.add(new Color(204,255,255));
        myColorList.add(new Color(204,255,204));
        myColorList.add(new Color(204,255,204));
        myColorList.add(new Color(255,153,255));
        myColorList.add(new Color(204,236,255));
        myColorList.add(new Color(255,204,153));
        myColorList.add(new Color(255,204,102));
    }


    public static BufferedImage create(int width, int height, String code){
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        Color backgroundColor = myColorList.get(random.nextInt(myColorList.size()));
        graphics.setColor(backgroundColor);//设置画笔颜色-验证码背景色
        graphics.fillRect(0,0,width,height);//填充背景
        graphics.setFont(new Font("Algerian",Font.BOLD,(int)((double)height * 0.75D)));

        //VALUE_ANTIALIAS_ON：使用抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = getCenterY(graphics, height);

        char[] chars = code.toCharArray();
        int len = chars.length;
        int charWidth = width / len;
        for(int i=0;i<len;i++){
            graphics.setColor(getRandomColor());
            int x = i*charWidth;
            //设置字体旋转角度
            int degree = random.nextInt() % 30;  //角度小于30度
            //正向旋转
            graphics.rotate(degree * Math.PI / 180, x==0?0.5:x, y);
            graphics.drawString(String.valueOf(chars[i]), (float) (x==0?0.5:x), y);
            //反向旋转（会累计旋转 所以要转回去）
            graphics.rotate(-degree * Math.PI / 180, x==0?0.5:x, y);
        }

        int area = width*height;
        int lines = (int)(area*0.00125);

        //画干扰线
        for (int i=0; i<lines; i++) {
            // 设置随机颜色
            graphics.setColor(getRandomColor());
            // 随机画线
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.drawLine(x1, y1, x1+random.nextInt(width), y1+random.nextInt(height));//加上x1、y1是为了防止变成一个点
        }

        int noise = (int)(area*0.0025);

        //添加噪点
        for(int i=0;i<noise;i++){
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2,2);
        }

        //释放资源
        graphics.dispose();

        return image;
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Color color = new Color(random.nextInt(256),
                random.nextInt(256), random.nextInt(256));
        return color;
    }

    private static int getCenterY(Graphics g, int backgroundHeight) {
        FontMetrics metrics = null;
        try {
            metrics = g.getFontMetrics();
        } catch (Exception var4) {
        }
        int y;
        if (null != metrics) {
            y = (backgroundHeight - metrics.getHeight()) / 2 + metrics.getAscent();
        } else {
            y = backgroundHeight / 3;
        }
        return y;
    }





}
