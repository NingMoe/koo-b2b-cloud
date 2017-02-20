package com.koolearn.cloud.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by haozipu on 2016/8/23.
 */
public class DrawUtil {

    static  String filePath ="d:\\canvas.jpg";
    static  String filePath1 ="d:\\canvas12.jpg";
    public static void main(String[] args) throws Exception{

        BufferedImage bimg=ImageIO.read(new FileInputStream(filePath));

        //得到Graphics2D 对象
        Graphics2D g2d=(Graphics2D)bimg.getGraphics();

        //设置颜色和画笔粗细
        g2d.setColor(Color.RED);

        //在图片上绘制 矩形 框
        drawCompositionCorrectRecord(g2d,100,100,240,160,3);

        //保存新图片
        ImageIO.write(bimg, "JPG", new FileOutputStream(filePath1));


    }


    /**
     * 绘制一个图片上的批改记录
     * @param g2d 绘图对象
     * @param x 矩形x坐标
     * @param y 矩形y坐标
     * @param w 矩形宽
     * @param h 矩形高
     * @param index 本图片的第几条批改记录
     */
    public static void drawCompositionCorrectRecord(Graphics2D g2d,int x,int y,int w,int h,int index){
        drawFang(g2d,x,y,w,h);
        drawYuan(g2d,x,y);
        drawNo(g2d,index,x,y);
    }

    /**
     * 绘制矩形
     * @param g2d
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public static void drawFang(Graphics2D g2d,int x,int y,int w,int h){
        //绘制图案或文字
        Rectangle2D rectangle = new Rectangle2D.Float(x, y, w, h);
        g2d.draw(rectangle);
    }

    /**
     * 绘制矩形左上角的角标
     * @param g2d
     * @param x
     * @param y
     */
    public static void drawYuan(Graphics2D g2d,int x,int y){
        //绘制图案或文字
        Ellipse2D  ellipse =new Ellipse2D.Double(x-15,y,15,15);//创建椭圆对象
        g2d.draw(ellipse);
    }

    /**
     * 绘制角标的序号
     * @param g2d
     * @param no
     * @param x
     * @param y
     */
    public static void drawNo(Graphics2D g2d,Integer no,int x,int y){
        g2d.drawString(no.toString(), x-15+4, y+12);
    }


}
