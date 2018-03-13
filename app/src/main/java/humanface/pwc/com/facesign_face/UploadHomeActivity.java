package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.discernface.MainActivity;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 02/11/2017.
 */

public class UploadHomeActivity extends Activity implements View.OnClickListener{
    private Button buFile,buStaffid,buCreate,buClear,buSelect,buClearOne,buPhotograph
            ,buCompare,buMegive,buCompareBitmap,buSearchImg,buEtCler,buError,buDetect,buRecognizeText;
    private LinearLayout llDelete;
    private EditText etStaffid;
    private ImgHelper db;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadhome);
        buFile = (Button) findViewById(R.id.bu_file_uploadHomeAty);
        buStaffid = (Button) findViewById(R.id.bu_staffid_uploadHomeAty);
        buCreate = (Button) findViewById(R.id.bu_createFaceset_uploadHomeAty);
        buClear = (Button) findViewById(R.id.bu_clearFace_uploadHomeAty);
        buSelect = (Button) findViewById(R.id.bu_select_uploadHomeAty);
        buClearOne = (Button) findViewById(R.id.bu_clearOneFace_uploadHomeAty);
        etStaffid = (EditText) findViewById(R.id.et_staffid_uploadHomeAty);
        llDelete = (LinearLayout) findViewById(R.id.ll_delete_uploadHomeAty);
        buPhotograph = (Button) findViewById(R.id.bu_face_photograph_uploadHomeAty);
        buCompare = (Button) findViewById(R.id.bu_face_compare_uploadHomeAty);
        buMegive = (Button) findViewById(R.id.bu_toMegive_uploadHomeAty);
        buCompareBitmap = (Button) findViewById(R.id.bu_face_comparebitmap_uploadHomeAty);
        buSearchImg = (Button) findViewById(R.id.bu_searchFile_uploadHomeAty);
        buEtCler = (Button) findViewById(R.id.bu_cleartext_uploadHomeAty);
        buError = (Button) findViewById(R.id.bu_error_uploadHomeAty);
        buDetect = (Button) findViewById(R.id.bu_detect_uploadHomeAty);
        buRecognizeText = (Button)findViewById(R.id.bu_recoginze_uploadHomeAty);
        buCreate.setOnClickListener(this);
        buStaffid.setOnClickListener(this);
        buFile.setOnClickListener(this);
        buClear.setOnClickListener(this);
        buSelect.setOnClickListener(this);
        buClearOne.setOnClickListener(this);
        buPhotograph.setOnClickListener(this);
        buCompare.setOnClickListener(this);
        buMegive.setOnClickListener(this);
        buCompareBitmap.setOnClickListener(this);
        buSearchImg.setOnClickListener(this);
        buEtCler.setOnClickListener(this);
        buError.setOnClickListener(this);
        buDetect.setOnClickListener(this);
        buRecognizeText.setOnClickListener(this);
        buClear.setVisibility(View.GONE);//清除当前人脸集合
        buCreate.setVisibility(View.GONE);//创建一个集合
//        llDelete.setVisibility(View.GONE);//根据userToken ,删除某张图片
        db = new ImgHelper();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bu_toMegive_uploadHomeAty:
                toMegive();
                break;
            case R.id.bu_select_uploadHomeAty:
                buSelectClick();
                break;
            case R.id.bu_file_uploadHomeAty:
                buFileClick();
                break;
            case R.id.bu_staffid_uploadHomeAty:
                buStaffidClick();
                break;
            case R.id.bu_createFaceset_uploadHomeAty:
                buCreateClick();
                break;
            case R.id.bu_clearFace_uploadHomeAty:
                clearClear();
                break;
            case R.id.bu_clearOneFace_uploadHomeAty:
                clearClearOneClick();
                break;
            case R.id.bu_face_photograph_uploadHomeAty:
                toFacePhotgraph();//
                text("com.pwc.innovation");
                break;
            case R.id.bu_face_compare_uploadHomeAty:
                Intent intent = new Intent(this,UploadHttpCompareImgActivity.class);//根据staffid搜索
                startActivity(intent);
                break;
            case R.id.bu_face_comparebitmap_uploadHomeAty:
                toCompareBimap();
                break;
            case R.id.bu_searchFile_uploadHomeAty:
                toSearchFile();
                break;
            case R.id.bu_cleartext_uploadHomeAty:
                etStaffid.setText("");
                break;
            case R.id.bu_error_uploadHomeAty:
                buToError();
                break;
            case R.id.bu_detect_uploadHomeAty:
                toDetectImg();
                break;
            case R.id.bu_recoginze_uploadHomeAty:
                toRecognizeText();
                break;

        }
    }
    private void toDetectImg(){
        Intent intent = new Intent(this,DetectImgActvity.class);
        startActivity(intent);
    }
    private void text(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            Toast.makeText(
                    this.getApplicationContext(),
                    "Please go to PwC AppStore to install the application.",
                    Toast.LENGTH_SHORT).show();
        } else {
            PackageManager packageManager = this
                    .getPackageManager();

            //TODO
            Intent intent = packageManager
                    .getLaunchIntentForPackage(packageName);
            this.startActivity(intent);
        }
    }
    private void toCompareBimap(){
        Intent intent = new Intent(this,CompareBitmapActivity.class);
        startActivity(intent);
    }
    private void toMegive(){
        Intent intent = new Intent(this, MegiveActivity.class);
        startActivity(intent);
    }

    /**
     * 跳照相
     */
    private void toFacePhotgraph(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void buSelectClick(){
        Intent intent = new Intent(this,UploadSelectActivity.class);
        startActivity(intent);
    }
    private void buFileClick(){
        Intent intent = new Intent(this,UploadActivity.class);
        startActivity(intent);
    }
    private void buStaffidClick(){
        Intent intent = new Intent(this,UploadHttpImgActivity.class);
        startActivity(intent);

    }

    /**
     * 跳到上传错误界面
     */
    private void buToError(){
        Intent intent = new Intent(this,ErrorActivity.class);
        startActivity(intent);
    }
    private void buCreateClick(){
        FaceHttp.createFaceSet(callback);
    }
    private void clearClear(){
        FaceHttp.clearFaceSet(callback);
    }
    private void toSearchFile(){
        Intent intent = new Intent(this,SearchFileImgActivity.class);
        startActivity(intent);
    }

    /**
     * 调到检测图片
     */
    private void toRecognizeText(){
        Intent intent = new Intent(this,RecognizeTextActvity.class);
        startActivity(intent);
    }
    /**
     * 清楚某一张人脸
     */
    private void clearClearOneClick(){
        String face_token = etStaffid.getText().toString();
        if(TextUtils.isEmpty(face_token)){
            L.i("staffid==null");
            return;
        }
//        String face_token = db.selectUserToken(staffid);
        FaceHttp.clearFaceSetOne(face_token,deletecallback);
    }
    private XutilsCallback callback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            if(TextUtils.isEmpty(result)){
                result = "null";
            }
            Util.toLogAty(UploadHomeActivity.this,result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String error = jsonObject.optString("error_message");
                if(TextUtils.isEmpty(error)){
                    Toast.makeText(UploadHomeActivity.this,"成功"+result,Toast.LENGTH_SHORT).show();
                    L.i("Creater+-->"+result);
                }else{
                    Toast.makeText(UploadHomeActivity.this,error,Toast.LENGTH_SHORT).show();
                    L.i("Creater+-->"+result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    private XutilsCallback deletecallback = new XutilsCallback() {
        @Override
        public void onSuccess(String result){
            if(TextUtils.isEmpty(result)){
                result = "null";
            }
            Util.toLogAty(UploadHomeActivity.this,result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String error = jsonObject.optString("error_message");
                if(TextUtils.isEmpty(error)){
                    Toast.makeText(UploadHomeActivity.this,"成功"+result,Toast.LENGTH_SHORT).show();
                    String staffid = etStaffid.getText().toString();
                    db.deleteImgStaffid(staffid);
                    L.i("Creater+-->"+result);
                }else{
                    Toast.makeText(UploadHomeActivity.this,error,Toast.LENGTH_SHORT).show();
                    L.i("Creater+-->"+result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
