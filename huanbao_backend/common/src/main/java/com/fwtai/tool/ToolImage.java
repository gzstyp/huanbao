package com.fwtai.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ToolImage{

    public static void getJpg(final String value){
        final int width = 31;
        final int height = 30;
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();
        // ---------- 增加下面的代码使得背景透明 -----------------
        //image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        // ---------- 背景透明代码结束 -----------------
        // 画图
        g2d.setColor(new Color(226,196,196));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawString(value,1,20);
        //释放对象
        g2d.dispose();
        // 保存文件
        try{
            ImageIO.write(image,"png",new File("c:/"+width+"x"+height+".jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 指定路径及文件名生成png透明背景的图片
     * @param fullPath 带/结尾的完整路径
     * @param fileName 文件名,无需带后缀名,默认的后缀名的.png
     * @param value 噪音值
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/5/24 16:56
    */
    public static String getPng(final String fullPath,final String fileName,final String value){
        final int width = 31;
        final int height = 30;
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(width, height, Transparency.TRANSLUCENT);
        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();
        g2d.dispose();
        g2d = image.createGraphics();
        // ---------- 背景透明代码结束 -----------------
        // 画图
        g2d.setColor(new Color(5,0,0));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawString(value,1,20);
        //释放对象
        g2d.dispose();
        // 保存文件
        try{
            final File file = new File(fullPath);
            if(!file.exists()){
                file.mkdirs();
            }
            ImageIO.write(image,"png",new File(fullPath+fileName+".png"));
        }catch(final IOException e){
            e.printStackTrace();
            return null;
        }
        return fullPath + fileName + ".png";
    }
}