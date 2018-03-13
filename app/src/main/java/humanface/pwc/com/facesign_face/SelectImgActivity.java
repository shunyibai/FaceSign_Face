package humanface.pwc.com.facesign_face;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;

import humanface.pwc.com.facesign_face.util.AuthService;
import humanface.pwc.com.facesign_face.util.L;

/**
 * Created by Shunyi Bai on 09/03/2018.
 */

public class SelectImgActivity extends Activity implements View.OnClickListener{
    private ImageView iv;
    int  IMAGE_MAX_SIZE = 1024;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimg);
        iv = (ImageView) findViewById(R.id.iv_photo_selectImgAty);
        Button button = (Button) findViewById(R.id.bu_submit_selectImgAty);
        button.setOnClickListener(this);
        iv.setOnClickListener(this);
        requestAllPermissionsIfNeed();
        new UsertokenAsyncTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        String[] pro = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,pro,null,null,null);
        int cou = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filepath = cursor.getString(cou);
        File file = new File(filepath);
        Bitmap bitmap = decodeFile(file);
        iv.setImageBitmap(bitmap);


    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestAllPermissionsIfNeed() {
        // Camera
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            // 申请相机权限成功，打开相机
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_photo_selectImgAty:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.bu_submit_selectImgAty:
                showDialogFragment("你好");
                break;
        }
    }
    private void showDialogFragment(String text){
        if(TextUtils.isEmpty(text)){
            return;
        }
        DialogFragment dialogFragment = new DialogFragment(){
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = inflater.inflate(R.layout.dialog_fragment_selectphoto,container);
                return view;
            }
        };
        dialogFragment.show(getFragmentManager(),text);
    }
    class UsertokenAsyncTask extends AsyncTask<String,ObjectInput,String>{
        @Override
        protected void onPostExecute(String s) {
            if(TextUtils.isEmpty(s)){
                L.i("access_token为空");
                return;
            }
            access_token = s;
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonStr = AuthService.getAuth();
            String access_token = "";
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                access_token = jsonObject.optString("access_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return access_token;
        }

        @Override
        protected void onPreExecute() {
            L.i("开始请求");
            super.onPreExecute();
        }
    };
    public static String encodeBase64File(String path) throws Exception {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }


}
