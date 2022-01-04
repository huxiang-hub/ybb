package com.yb.statis.utils;

import com.yb.statis.vo.ReachImageVO;
import org.jxls.util.Util;

import java.io.IOException;
import java.io.InputStream;

public class AddImageUtils {

    private String imagePath = "images/";
    /**
     * 根据达成率读取不同的图片
     * @param rateNum
     * @return
     */
    public byte[] addImage(Double rateNum){
        InputStream imageInputStream = null;//图片
        String image;
        try {
            if(rateNum < 0.6){
                image = "1.png";
                imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + image);
            }
            if(0.8 > rateNum && rateNum >= 0.6){
                image = "2.png";
                imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + image);
            }
            if(0.9 > rateNum && rateNum >= 0.8){
                image = "3.png";
                imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + image);
            }
            if(rateNum >= 0.9){
                image = "4.png";
                imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + image);
            }
            return Util.toByteArray(imageInputStream);
        }catch (IOException e){
            System.err.println("图片导入错误");
            e.printStackTrace();
        }finally {
            try {
                if(imageInputStream != null){
                    imageInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 达成率规则图片,红旗,笑脸
     * @return
     */
    public ReachImageVO saveImageReach(){
        InputStream imageInputStream = null;//图片
        ReachImageVO reachImageVO = new ReachImageVO();
        try {
            imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + "1.png");
            reachImageVO.setBomb(Util.toByteArray(imageInputStream));//炸弹图片
            imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + "2.png");
            reachImageVO.setSorrily(Util.toByteArray(imageInputStream));//难过图片
            imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + "3.png");
            reachImageVO.setSmile(Util.toByteArray(imageInputStream));//微笑图片
            imageInputStream = this.getClass().getClassLoader().getResourceAsStream(imagePath + "4.png");
            reachImageVO.setRedBanner(Util.toByteArray(imageInputStream));//红旗图片
            return reachImageVO;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (imageInputStream != null) {
                    imageInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
