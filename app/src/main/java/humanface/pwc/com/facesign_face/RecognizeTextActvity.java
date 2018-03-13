package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import humanface.pwc.com.facesign_face.bean.Face;
import humanface.pwc.com.facesign_face.bean.FaceResponse;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 09/02/2018.
 */

public class RecognizeTextActvity extends Activity{
    private Button buSelect,buUploadImg;
    private ImageView iv;
    int  IMAGE_MAX_SIZE = 1024;
    private  List<Face> mList;
    private ProgressBar pb;
    private String mImagePath;
    private EditText tv;
    private String path = "/storage/emulated/0/Android/data/humanface.pwc.com.facesign_face/files/Pictures/Search/text.png";
    private StringBuilder mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        buSelect = (Button) findViewById(R.id.bu_selectImg_detectAty);
        buSelect.setVisibility(View.GONE);
        buUploadImg = (Button) findViewById(R.id.bu_uploadImg_detectAty);
        iv = (ImageView) findViewById(R.id.iv_album_detectAty);
        pb = (ProgressBar) findViewById(R.id.pb_detectAty);
        tv = (EditText) findViewById(R.id.tv_userid_detectAty);
        buSelect.setOnClickListener(clickListener);
        buUploadImg.setOnClickListener(clickListener);
        mUserId = new StringBuilder();
        mList = new ArrayList<>();
        Bitmap uri = decodeFile(new File(path));
        iv.setImageBitmap(uri);


    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bu_selectImg_detectAty:
                    selectAlbum();
                    break;
                case R.id.bu_uploadImg_detectAty:
                    pb.setVisibility(View.VISIBLE);
                    File file = new File(path);
                    FaceHttp.recognizeText(file,createFaceokenCallback);
                    break;
            }
        }
    };
    private void selectAlbum(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(albumIntent,1);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        Uri uri = data.getData();
        mImagePath =  handleImageOnKitKat(data);
        Log.i("Detect","path-->"+mImagePath);
        iv.setImageURI(uri);
        if(!TextUtils.isEmpty(mImagePath)){
            buUploadImg.setVisibility(View.VISIBLE);
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String  handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }
    private XutilsCallback createFaceokenCallback = new XutilsCallback(){

        @Override
        public void onSuccess(String result){
            pb.setVisibility(View.GONE);
            L.i(result);
//            Util.toLogAty(RecognizeTextActvity.this,result);
            if(TextUtils.isEmpty(result)){
                return ;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsoni = jsonArray.getJSONObject(i);
                    String value = jsoni.optString("value");
                    String type  = jsoni.optString("type");
                    if(type.equals("textline")){
                        mUserId.append("\n");
                    }
                    mUserId.append(value);
                }
                tv.setText(mUserId.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinished() {
            Log.e("onFinished", "createFaceokenCallback");
            super.onFinished();
        }
    };

    private void showT(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT);
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

}
