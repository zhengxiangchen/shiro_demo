package com.czx.shiro_demo;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.TimeIntervalAspect;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.SystemUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroDemoApplicationTests {

    @Test
    public void contextLoads() {

        //生成二维码
//        qrcode_test();

        //获取当前时间的00:00:00
//        dayBegin_test();

        //向后推几天
//        day_after_test();


        //保存图片
        //save_pic();

    }



    //保存图片
    private void save_pic() {

        try{
            FileInputStream fis = new FileInputStream("E://picture_save_path/file/pic/20191113/virtual_20191113112753.png");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            // 得到图片的字节数组
            byte[] result = bos.toByteArray();

            OutputStream os = new FileOutputStream("E://picture_save_path/file/pic/20191113/1.png");
            os.write(result, 0, result.length);
            os.flush();
            os.close();

        }catch (Exception e){
            e.getMessage();
        }

    }

    //向后推几天
    private void day_after_test() {

        DateTime dateTime = DateUtil.offsetWeek(new Date(), 1);
        Long time = DateUtil.beginOfDay(dateTime).getTime() / 1000;

        System.out.println(time);
    }


    //获取当前时间的00:00:00
    private void dayBegin_test() {

        Long dayBegin = DateUtil.beginOfDay(new Date()).getTime() / 1000;

        System.out.println(dayBegin);
    }


    //生成二维码
    private void qrcode_test() {
        QRCodeUtil.zxingCodeCreate(IdUtil.objectId(),500,"http://file.suparking.cn:8080/pic/010000/20191023/192.168.1.203_20191023155828824_image_.thumbnail.jpg", "E://520/123.png");
        System.out.println("完成");
    }


    public static class QRCodeUtil {

        // 二维码颜色==黑色
        private static final int BLACK = 0xFF000000;
        // 二维码颜色==白色
        private static final int WHITE = 0xFFFFFFFF;

        /**
         * zxing方式生成二维码
         * 注意：
         * 1,文本生成二维码的方法独立出来,返回image流的形式,可以输出到页面
         * 2,设置容错率为最高,一般容错率越高,图片越不清晰, 但是只有将容错率设置高一点才能兼容logo图片
         * 3,logo图片默认占二维码图片的20%,设置太大会导致无法解析
         *
         * @param content  二维码包含的内容，文本或网址
         * @param size     生成的二维码图片尺寸 可以自定义或者默认（250）
         * @param logoPath logo的存放位置
         * @param absolutePath 生成的二维码存放位置
         */
        public static void zxingCodeCreate(String content, Integer size, String logoPath, String absolutePath) {
            try {
                //获取保存文件的上级路径
                String path = absolutePath.substring(0, absolutePath.lastIndexOf("/"));

                File targetFile = new File(path);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //图片类型
                String imageType = "png";
                //获取二维码流的形式，写入到目录文件中
                BufferedImage image = getBufferedImage(content, size, logoPath);
                File outPutImage = new File(absolutePath);
                ImageIO.write(image, imageType, outPutImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        /**
         * 二维码流的形式，包含文本内容
         *
         * @param content  二维码文本内容
         * @param size     二维码尺寸
         * @param logoPath logo的存放位置
         * @return
         */
        public static BufferedImage getBufferedImage(String content, Integer size, String logoPath) {
            if (size == null || size <= 0) {
                size = 250;
            }
            BufferedImage image = null;
            try {
                // 设置编码字符集
                Map<EncodeHintType, Object> hints = new HashMap<>();
                //设置编码
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                //设置容错率最高
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                hints.put(EncodeHintType.MARGIN, 1);
                // 1、生成二维码
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                // 2、获取二维码宽高
                int codeWidth = bitMatrix.getWidth();
                int codeHeight = bitMatrix.getHeight();
                // 3、将二维码放入缓冲流
                image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < codeWidth; i++) {
                    for (int j = 0; j < codeHeight; j++) {
                        // 4、循环将二维码内容定入图片
                        image.setRGB(i, j, bitMatrix.get(i, j) ? BLACK : WHITE);
                    }
                }
                //判断是否写入logo图片
                if (logoPath != null && logoPath.length() > 0) {
                    Graphics2D g = image.createGraphics();
                    BufferedImage logo = ImageIO.read(new URL(logoPath));
                    int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null);
                    int heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getHeight(null);
                    int x = (image.getWidth() - widthLogo) / 2;
                    int y = (image.getHeight() - heightLogo) / 2;
                    // 开始绘制图片
                    g.drawImage(logo, x, y, widthLogo, heightLogo, null);
                    g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
                    //边框宽度
                    g.setStroke(new BasicStroke(2));
                    //边框颜色
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, widthLogo, heightLogo);
                    g.dispose();
                    logo.flush();
                    image.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }


    }

}
