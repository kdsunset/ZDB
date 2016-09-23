package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.ZipImage;

public class TakePhotosActivity extends Activity {
    ImageView showresult;
    String title,date,content;
    private static final int PICTURE_FROM_CAMERA = 0X32;
    private static final int PICTURE_FROM_GALLERY = 0X34;

    String path;
    private File file;//存储拍摄图片的文件
    private int itemid;
    private int paperid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);
        Intent lastIntent = getIntent();

        file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis()  +".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtils.i("PhotoActivity");

        path=file.toString();

        try {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent,PICTURE_FROM_CAMERA);
        } catch (Exception e) {

        }
		/*Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//启动相机的Action
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//设置图片拍摄后保存的位置
		startActivityForResult(intent, PICTURE_FROM_CAMERA);//启动相机，这里使用有返回结果的启动*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("onActivityResult");
        if (resultCode == Activity.RESULT_OK && requestCode == PICTURE_FROM_CAMERA){
            LogUtils.i("onActivityResult--RESULT_OK--PICTURE_FROM_CAMERA");




            //这里对图片进行了压缩，因为有些手机拍摄的照片过大，无法显示到ImageView中，所以
            // 我们将图片近行了压缩然后在进行显示
            String path= Uri.fromFile(file).getPath();
            if(path.startsWith("file:")){
                path = path.substring(5, path.length());
            }
            try {
                ZipImage.zipImage(path);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TakePhotosActivity.this,"保存图片时遇到问题",Toast.LENGTH_SHORT).show();
            }
            //将图片设置到ImageView中，这里使用setImageURI（）方法进行设置。
            Intent in=new Intent();
            in.putExtra("PATH",path);
            in.putExtra("id",itemid);
            in.putExtra("paperid",paperid);

            setResult(1001,in);



        }

        finish();






    }
}
