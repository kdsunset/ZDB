package cn.zhudai.zin.zhudaibao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.zhudai.zin.zhudaibao.R;

/**
 * Created by ZIN on 2016/4/16.
 */
public class ImageUtils {

    public static void setImg4ViewFromLocal(ImageView imageView, String path) {
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //  .showImageOnLoading(R.drawable.ic_stub)
               // .showImageOnFail(R.drawable.ic_loadimg_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        String imagePath = path;
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(imagePath);
        LogUtils.i("setImg4ViewFromLocal"+imagePath);
        LogUtils.i("setImg4ViewFromLocal"+imageUrl);
        /* setImgForImageView /storage/emulated/0/1461152381274paperid=0quesid=8.jpg
           setImgForImageView file:///storage/emulated/0/1461152381274paperid=0quesid=8.jpg*/
//      String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }
    public static void setImg4ViewFromNet(ImageView imageView, String imageUrl){

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                 .showImageOnLoading(R.drawable.loading_anim)
                .showImageOnFail(R.drawable.load_img_fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveBitmapToSDCard(Bitmap bitmap, File file)
    {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                LogUtils.i("图片保存成功！");
                return  file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

       return null;
    }
}
