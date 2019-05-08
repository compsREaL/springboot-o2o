package com.real.o2o.util;

import com.real.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author: mabin
 * @create: 2019/4/13 9:34
 */
public class ImageUtil {

    private static String basePath = PathUtil.getImgBasePath();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    //缩略图存放位置
    public static String generateThumbnail(ImageHolder imageHolder, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(imageHolder.getImgName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            //C:\Users\84428\IdeaProjects\o2o\src\test\resources\water.png
//            Thumbnails.of(imageHolder.getInputStream()).size(200,200).watermark(Positions.BOTTOM_RIGHT,
//                    ImageIO.read(new File(basePath+"/water.jpg")),0.25f).outputQuality(0.8).toFile(dest);
            Thumbnails.of(imageHolder.getInputStream()).size(200, 200).watermark(Positions.BOTTOM_RIGHT,
                    ImageIO.read(new File(basePath + "/water.png")), 0.25f).outputQuality(0.8).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒+五位随机数
     *
     * @return
     */
    private static String getRandomFileName() {
        //获取随机五位数
        int randomNum = random.nextInt(89999) + 10000;
        String nowTimeStr = simpleDateFormat.format(new Date());
        return nowTimeStr + randomNum;
    }

//    public static void main(String[] args) throws IOException {
//        Thumbnails.of(new File("C:/project/xiaohuangren.jpg")).size(200,200).watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File("C:\\Users\\84428\\IdeaProjects\\o2o\\src\\main\\resources\\water.png")),
//                0.25f).outputQuality(0.8f).toFile(new File("C:/project/xiaohuangrennew.jpg"));
//    }

    /**
     * 如果storePath为文件路径，则删除该文件
     * 如果storePath为目录路径，则删除该目录下的所有文件
     *
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + "/" + storePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();
                for(File file:files){
                    file.delete();
                }
            }
            fileOrPath.delete();
        }
    }

    /**
     * 处理详情图，返回新生成图片的相对值路径
     * @param imageHolder
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder imageHolder, String targetAddr) {
        //获取不重复的随机名称
        String realFileName = getRandomFileName();
        //获取文件的扩展名
        String extension = getFileExtension(imageHolder.getImgName());
        //如果目标路径不存在，则自动创建该路径
        makeDirPath(targetAddr);
        //获取文件存储的相对路径
        String relativeAddr = targetAddr+realFileName+extension;
        logger.debug("current relative addr is :" + relativeAddr);
        //获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath()+relativeAddr);
        //调用Thumbnails生成带水印的图片
        System.out.println(basePath);
        try {
            Thumbnails.of(imageHolder.getInputStream()).size(337,640).watermark(Positions.BOTTOM_RIGHT,ImageIO.read(
                    new File(basePath+"/water.png")),0.25f).outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建图片失败"+e.toString());
        }
        return relativeAddr;
    }
}
