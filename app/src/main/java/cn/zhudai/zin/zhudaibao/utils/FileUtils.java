package cn.zhudai.zin.zhudaibao.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ZIN on 2016/4/20.
 */
public class FileUtils {


    public static boolean isSDCardExist(){
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    public static String getAppFilePath(Context context){
      //  /data/data/com.example.fileoperation/files
        return context. getApplicationContext().getFilesDir().getAbsolutePath();
    }

    public  static String getSDCardRootPath(){

        return  Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 尝试在SD卡下创建filePath目录下的fileName文件，没有则在应用的目录下创建，返回文件的路径Sting
     */
    public static String createPath(Context context,String filePath,String fileName) {
        //String filename="/"+"headimg"+System.currentTimeMillis() +".jpg";
        //String filename="/opticalTest/images";


        String path = "";
        String rootPath;

        if (FileUtils.isSDCardExist()) {
            rootPath = FileUtils.getSDCardRootPath();
            File sdAppPath = new File(rootPath + filePath);
            if (!sdAppPath.exists()) {
                sdAppPath.mkdirs();
                Log.i("", "在SDCard内目录文件夹创建成功");
            }

            File file = new File(sdAppPath, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    path = file.getPath();

                } catch (IOException e) {
                    e.printStackTrace();


                }
            }

        } else {
            rootPath = FileUtils.getAppFilePath(context);
            path = rootPath + fileName;
        }
        return path;
    }
    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
}
