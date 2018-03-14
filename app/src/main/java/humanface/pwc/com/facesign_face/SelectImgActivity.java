package humanface.pwc.com.facesign_face;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.List;

import humanface.pwc.com.facesign_face.bean.WordBaidu;
import humanface.pwc.com.facesign_face.util.AuthService;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.XutilsCallback;
import humanface.pwc.com.facesign_face.view.CameraOrAlbumDialog;

/**
 * Created by Shunyi Bai on 09/03/2018.
 */

public class SelectImgActivity extends Activity implements View.OnClickListener,CameraOrAlbumDialog.OnCameraOnClick{
    private ImageView iv;
    int  IMAGE_MAX_SIZE = 1024;
    private String access_token;
    private String path;
    private TextView tvWait;
    private CameraOrAlbumDialog cameraOrAlbumDialog;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimg);
        iv = (ImageView) findViewById(R.id.iv_photo_selectImgAty);
        Button button = (Button) findViewById(R.id.bu_submit_selectImgAty);
        tvWait = (TextView)findViewById(R.id.tv_wait_selectImgAty);
        button.setOnClickListener(this);
        iv.setOnClickListener(this);
        requestAllPermissionsIfNeed();
        cameraOrAlbumDialog = new CameraOrAlbumDialog();
        SharedPreferences sharedPreferences = getSharedPreferences("BaiduAcctoken",MODE_PRIVATE);
        access_token = sharedPreferences.getString("access_token","");
        if(TextUtils.isEmpty(access_token)){
            new UsertokenAsyncTask().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case 1:
                Uri uri = data.getData();
                String[] pro = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,pro,null,null,null);
                int cou = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(cou);
                File file = new File(path);
                Bitmap bitmap = decodeFile(file);
                iv.setImageBitmap(bitmap);
                break;
            case 2:
                if(imgFile==null){
                    return;
                }
                path = imgFile.toString();
                Bitmap bitmap1 = decodeFile(imgFile);
                iv.setImageBitmap(bitmap1);
                break;
        }



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
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
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
                cameraOrAlbumDialog.show(getFragmentManager(),"ca");
                break;
            case R.id.bu_submit_selectImgAty:
//                showDialogFragment("你好");
                if(TextUtils.isEmpty(path)){
                    return;
                }
                tvWait.setVisibility(View.VISIBLE);
                String base64img = encodeBase64File(path);
                FaceHttp.recognizeBaiduText(access_token,base64img,setUseridCallback);
                break;
        }
    }
    /**
     * 给人脸添加userid
     */
    private XutilsCallback setUseridCallback = new XutilsCallback(){

        @Override
        public void onSuccess(String result) {
            if(TextUtils.isEmpty(result)){
                return;
            }
            StringBuilder contentStr = new StringBuilder();
            Log.i("Upload","onSuccess result-->" + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int wordnum = jsonObject.optInt("words_result_num");
                if(wordnum>0){
                    String wordsStr = jsonObject.optString("words_result");
                    List<WordBaidu> wordList = JSON.parseArray(wordsStr, WordBaidu.class);
                    for(WordBaidu word: wordList){
                        if(word!=null){
                            String words = word.getWords();
                            if(!TextUtils.isEmpty(words)){
                                contentStr.append(words);
                                contentStr.append("\n");
                            }
                        }
                    }
                    showDialogFragment(contentStr.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinished() {
            tvWait.setVisibility(View.GONE);
            super.onFinished();
        }
    };
    private void showDialogFragment(final String text){
        if(TextUtils.isEmpty(text)){
            return;
        }
        DialogFragment dialogFragment = new DialogFragment(){
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = inflater.inflate(R.layout.dialog_fragment_selectphoto,container);
                TextView tv = (TextView) view.findViewById(R.id.tv_cardText_dialogFrag);
                tv.setText(text);
                return view;
            }
        };
        dialogFragment.show(getFragmentManager(),text);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
                toCamera();
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 1);
                break;
        }
    }
    private void toCamera(){
//        if(!hasPermissionInManifest(this,"android.permission-group.CAMERA")){
//            return;
//        }
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        imgFile = new File(path,"baiduimgpath.png");
        Uri uriImg = null;
        if(Build.VERSION.SDK_INT>=24){
            uriImg = FileProvider.getUriForFile(this,"humanface.pwc.com.facesign_face.fileProvider",imgFile);
        }else{
            uriImg = Uri.fromFile(imgFile);
        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //授权应用的
        List<ResolveInfo> resInfoList = getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uriImg, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImg);
        startActivityForResult(intent, 2);
    }
    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }
    class UsertokenAsyncTask extends AsyncTask<String,ObjectInput,String>{
        @Override
        protected void onPostExecute(String s) {
            if(TextUtils.isEmpty(s)){
                L.i("access_token为空");
                return;
            }
            access_token = s;
            SharedPreferences sharedPreferences = getSharedPreferences("BaiduAcctoken",MODE_PRIVATE);
            SharedPreferences.Editor editor  = sharedPreferences.edit();
            editor.putString("access_token",access_token);
            editor.commit();
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
                access_token = jsonStr;
            }
            return access_token;
        }

        @Override
        protected void onPreExecute() {
            L.i("开始请求");
            super.onPreExecute();
        }
    };
    public static String encodeBase64File(String path) {
        File  file = new File(path);
        byte[] buffer = null;
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }


}
