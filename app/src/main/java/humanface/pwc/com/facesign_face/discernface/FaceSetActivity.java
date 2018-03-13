package humanface.pwc.com.facesign_face.discernface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.R;
import humanface.pwc.com.facesign_face.UploadHttpImgActivity;
import humanface.pwc.com.facesign_face.adapter.ArrayFaceNameAdapter;
import humanface.pwc.com.facesign_face.bean.FaceSetDetail;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 26/10/2017.
 */

public class FaceSetActivity extends Activity implements View.OnClickListener{
    private Button buToUpload;
    private ListView mLv;
    private ArrayFaceNameAdapter mAdapter;
    private List<String> mList;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceset);
        buToUpload = (Button) findViewById(R.id.bu_toUpload_facesetAty);
        mLv = (ListView) findViewById(R.id.lv_facesetAty);
        mList = new ArrayList<>();
        mAdapter = new ArrayFaceNameAdapter(mList,this);
        mLv.setAdapter(mAdapter);
        buToUpload.setOnClickListener(this);
        pb = (ProgressBar) findViewById(R.id.pb_facesetAty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FaceHttp.getFaceSetFaceToken(setUseridCallback);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bu_toUpload_facesetAty:
                Intent intent = new Intent(this,UploadHttpImgActivity.class);
                startActivity(intent);
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
            Log.i("FaceSet","onSuccess result-->" + result);
            FaceSetDetail faceSetDetail =  JSON.parseObject(result,FaceSetDetail.class);
            List<String> list = faceSetDetail.getFace_tokens();
            if(list!=null){
                mList.clear();
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                buToUpload.setText("本地共有"+faceSetDetail.getFace_count()+",点击上传图片");
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            Toast.makeText(FaceSetActivity.this,ex.toString(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinished() {
            pb.setVisibility(View.GONE);
            super.onFinished();
        }
    };
}
