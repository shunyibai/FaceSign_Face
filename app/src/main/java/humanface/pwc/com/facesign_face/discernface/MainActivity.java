package humanface.pwc.com.facesign_face.discernface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import humanface.pwc.com.facesign_face.DetailLogActivity;
import humanface.pwc.com.facesign_face.R;
import humanface.pwc.com.facesign_face.SearchFileImgActivity;
import humanface.pwc.com.facesign_face.util.DrawFacesView;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.ImageUtil;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

public class MainActivity extends Activity implements View.OnClickListener {
    private SurfaceView surfaceView;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private DrawFacesView facesView;
    private Button bu;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_mainAty);
        facesView = (DrawFacesView) findViewById(R.id.surfaceView_drawface_mainAty);
        iv = (ImageView) findViewById(R.id.iv);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(callback);
        bu = (Button) findViewById(R.id.bu_start);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bu_start:
                if(mCamera!=null){
                    mCamera.takePicture(null,null,mPicture);
                }else{
                    L.e("Camera==null");
                }
                break;
        }
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder){
            Log.i("tag", "surfaceCreated");
            if (mCamera == null) {
                mCamera = Camera.open(0);
                try {
                    mCamera.setFaceDetectionListener(new FaceDetectorListener());
                    mCamera.setPreviewDisplay(mHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startFaceDetector();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2){
            if (mHolder.getSurface() == null){
                Log.e("tag", "mHolder.getSurface() == null");
                return;
            }
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                Log.e("tag", "Error stopping camera preview: " + e.getMessage());
            }
            try {
                mCamera.setPreviewDisplay(mHolder);
                setCameraParmes(mCamera);
                mCamera.startPreview();
                startFaceDetector(); // re-start face detection feature
            } catch (Exception e) {
                Log.d("tag", "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder){
            Log.i("tag", "surfaceDestroyed");
            if(mCamera!=null){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                mHolder = null;
            }
        }
    };

    /**
     * 设计相机参数
     *
     * @param camera
     */
    private void setCameraParmes(Camera camera) {
        Camera.Parameters parames = camera.getParameters();
        parames.setJpegQuality(100);//对焦
        if (parames.getSupportedFocusModes().contains(parames.FOCUS_MODE_CONTINUOUS_VIDEO)){
            parames.setFocusMode(parames.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.cancelAutoFocus();
        camera.setDisplayOrientation(90);
        camera.setParameters(parames);
    }

    private void startFaceDetector(){
        Camera.Parameters parameters = mCamera.getParameters();
        int num = parameters.getMaxNumDetectedFaces();
        if (num > 0) {
            mCamera.startFaceDetection();
        } else {
            Log.e("tag", "【FaceDetectorActivity】类的方法：【startFaceDetection】: " + "不支持");
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==1){
                bu.setOnClickListener(MainActivity.this);
                bu.setVisibility(View.VISIBLE);
            }else{
                bu.setOnClickListener(null);
                bu.setVisibility(View.GONE);
            }
        }
    };
    private class FaceDetectorListener implements Camera.FaceDetectionListener {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length > 0) {
                Camera.Face face = faces[0];
                Rect rect = face.rect;
                handler.sendEmptyMessage(1);
                Log.d("FaceDetection", "可信度：" + face.score + "face detected: " + faces.length +
                        " Face 1 Location X: " + rect.centerX() +
                        "Y: " + rect.centerY() + "   " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom);
                Log.e("tag", "【FaceDetectorListener】类的方法：【onFaceDetection】: ");
                Matrix matrix = updateFaceRect();
                facesView.updateFaces(matrix, faces);
            } else {
                // 只会执行一次
                handler.sendEmptyMessage(0);
                Log.e("tag", "【FaceDetectorListener】类的方法：【onFaceDetection】: " + "没有脸部");
                facesView.removeRect();
            }
        }
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera){
            if (null != mCamera) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = ImageUtil.rotateMyBitmap(bitmap);
            bitmap = ImageUtil.ratio(bitmap, 800, 800);
            iv.setVisibility(View.VISIBLE);
            iv.setImageBitmap(bitmap);
            byte[] imgbyte = ImageUtil.Bitmap2Bytes(bitmap);
            String imgStr = Base64.encodeToString(imgbyte, Base64.DEFAULT);
            FaceHttp.searchFace(imgStr,searchFaceokenCallback);
        }
    };
    private XutilsCallback searchFaceokenCallback = new XutilsCallback() {

        @Override
        public void onSuccess(String result) {
            Log.i("Upload", "onSuccess result-->" + result);
            if (TextUtils.isEmpty(result)) {
                return;
            }
            Intent intent = new Intent(MainActivity.this, DetailLogActivity.class);
            intent.putExtra("JSON", result);
            startActivity(intent);
            finish();
        }

        @Override
        public void onFinished() {
            Log.e("onFinished", "createFaceokenCallback");
            super.onFinished();
        }
    };

    /**
     * 因为对摄像头进行了旋转，所以同时也旋转画板矩阵
     *
     * @return
     */
    private Matrix updateFaceRect() {
        Matrix matrix = new Matrix();
        Camera.CameraInfo info = new Camera.CameraInfo();
        boolean mirror = (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
        matrix.setScale(mirror ? -1 : 1, 1);
        matrix.postRotate(90);
        matrix.postScale(surfaceView.getWidth() / 2000f, surfaceView.getHeight() / 2000f);
        matrix.postTranslate(surfaceView.getWidth() / 2f, surfaceView.getHeight() / 2f);
        return matrix;
    }
}
