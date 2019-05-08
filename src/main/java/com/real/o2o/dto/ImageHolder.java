package com.real.o2o.dto;

import java.io.InputStream;

/**
 * @author: mabin
 * @create: 2019/4/15 14:33
 */
public class ImageHolder {

    //图片的输入流
    private InputStream inputStream;
    //图片名称
    private String imgName;

    public ImageHolder(String imgName,InputStream inputStream){
        this.imgName=imgName;
        this.inputStream=inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
