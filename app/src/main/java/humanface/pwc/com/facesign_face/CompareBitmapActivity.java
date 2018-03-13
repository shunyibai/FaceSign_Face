package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 17/11/2017.
 */

public class CompareBitmapActivity extends Activity implements View.OnClickListener{
    private static final int LEFT_IMG = 11;
    private static final int RIGHT_IMG = 12;
    private Button buCompare;
    private ImageView icLeft,ivRight;
    private File mPictureFile;
    private File leftFile,rightFile;
    private int IMAGE_MAX_SIZE = 720;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparebitmap);
        buCompare = (Button) findViewById(R.id.bu_compare_comparBitmapAty);
        icLeft = (ImageView) findViewById(R.id.iv_left_compareBitmapAty);
        ivRight = (ImageView) findViewById(R.id.iv_right_compareBitmapAty);
        buCompare.setOnClickListener(this);
//        ivRight.setImageResource(R.drawable.shunyibairight);
//        icLeft.setImageResource(R.drawable.shunyibaileft);
        icLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bu_compare_comparBitmapAty:
                compareHttp();
                break;
            case R.id.iv_left_compareBitmapAty:
//                toCamera(LEFT_IMG);
                break;
            case R.id.iv_right_compareBitmapAty:
//                toCamera(RIGHT_IMG);
                break;
        }
    }
   private void toCamera(int requestCode){
       // 设置相机拍照后照片保存路径
       mPictureFile = new File(Environment.getExternalStorageDirectory(),
               "faceSing" + System.currentTimeMillis()/1000 + ".jpg");
       // 启动拍照,并保存到临时文件
       Intent mIntent = new Intent();
       mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
       mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
       mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
       startActivityForResult(mIntent, requestCode);
   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode==LEFT_IMG){
            leftFile = mPictureFile;
            if(leftFile!=null){
                Bitmap bi1 = decodeFile(leftFile);
                if(bi1!=null){
                    icLeft.setImageBitmap(bi1);
                }
            }
        }else
        if(requestCode==RIGHT_IMG){
            rightFile = mPictureFile;
            Bitmap bi2 = decodeFile(rightFile);
            if(bi2!=null){
                ivRight.setImageBitmap(bi2);
            }
        }
    }
    private void compareHttp(){
        Bitmap bitmapLeft =  BitmapFactory.decodeResource(getResources(),R.drawable.shunyibaileft);
        byte[] imgdata1sLeft = Bitmap2Bytes(bitmapLeft);
        String imgLeft = Base64.encodeToString(imgdata1sLeft, Base64.DEFAULT);

        Bitmap bitmapRight =  BitmapFactory.decodeResource(getResources(),R.drawable.shunyibairight);
        byte[] imgdata1Right = Bitmap2Bytes(bitmapRight);
        String imgRight = Base64.encodeToString(imgdata1Right, Base64.DEFAULT);
        FaceHttp.compareFace(imgLeft,imgRight,callback);
    }
    private String bitmapToBase64(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imgStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imgStr;
    }
    private  byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
    private Bitmap decodeFile(File f){
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
    private XutilsCallback callback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            if(TextUtils.isEmpty(result)){
                result = "null";
            }
            L.i("result-->"+result);

        }
    };
}
