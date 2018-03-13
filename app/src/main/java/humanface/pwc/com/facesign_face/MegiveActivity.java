package humanface.pwc.com.facesign_face;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.meglive.LivenessActivity;
import humanface.pwc.com.meglive.LivenessActivityOne;
import humanface.pwc.com.meglive.util.ConUtil;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Shunyi Bai on 16/11/2017.
 */

public class MegiveActivity extends Activity implements View.OnClickListener{
    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    private static final int PAGE_INTO_LIVENESS = 100;
    private Button buToLiveness;
    private ImageView ivFace,ivFace2;
    private String uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megive);
        buToLiveness = (Button) findViewById(R.id.bu_liveness_megiveAty);
        ivFace = (ImageView) findViewById(R.id.iv_img_megiveAty);
        ivFace2 = (ImageView) findViewById(R.id.iv_img2_megiveAty);

        new Thread(runnable).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bu_liveness_megiveAty:
                requestCameraPerm();
                break;
        }
    }
    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                enterNextPage();
            }
        } else {
            enterNextPage();
        }
    }

    private void enterNextPage() {
        startActivityForResult(new Intent(this, LivenessActivityOne.class), PAGE_INTO_LIVENESS);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Manager manager = new Manager(MegiveActivity.this);
            LivenessLicenseManager licenseManager = new LivenessLicenseManager(MegiveActivity.this);
            manager.registerLicenseManager(licenseManager);
            uuid = ConUtil.getUUIDString(MegiveActivity.this);
            manager.takeLicenseFromNetwork(uuid);
            if (licenseManager.checkCachedLicense() > 0){
                mHandler.sendEmptyMessage(1);
            } else{
                mHandler.sendEmptyMessage(2);
            }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    buToLiveness.setOnClickListener(MegiveActivity.this);
                    break;
                case 2:
                    buToLiveness.setText("授权失败");
                    break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK){
            Bundle bundle=data.getExtras();
            jsonBundle(bundle);
        }
    }
    private void jsonBundle(Bundle bundle){
        String resultOBJ = bundle.getString("result");

        try {
            JSONObject result = new JSONObject(resultOBJ);
            int resID = result.getInt("resultcode");
            if (resID == R.string.verify_success) {
//                doPlay(R.raw.meglive_success);
            } else if (resID == R.string.liveness_detection_failed_not_video) {
//                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed_timeout) {
//                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed) {
//                doPlay(R.raw.meglive_failed);
            } else {
//                doPlay(R.raw.meglive_failed);
            }

            boolean isSuccess = result.getString("result").equals(
                    getResources().getString(R.string.verify_success));
            String strT = isSuccess ? "成功": "失败";
            Toast.makeText(this,strT,Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                String delta = bundle.getString("delta");
                L.i("delta-->"+delta);
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                if (images.containsKey("image_best")) {//这是最好的一张图片
                    byte[] bestImg = images.get("image_best");
                    if (bestImg != null && bestImg.length > 0) {
                        Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
                        ivFace.setImageBitmap(bestBitMap);
                    }
                }
                if (images.containsKey("image_env")) {// 这是一张全景图
                    byte[] envImg = images.get("image_env");
                    if (envImg != null && envImg.length > 0) {
                        Bitmap envBitMap = BitmapFactory.decodeByteArray(envImg, 0, envImg.length);
                        ivFace2.setImageBitmap(envBitMap);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
