package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.adapter.ArrayUserNameAdapter;
import humanface.pwc.com.facesign_face.bean.User;
import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class UploadSelectActivity extends Activity implements View.OnClickListener {
    private Button  buDetection,buUpdateFacesetToken;
    private TextView tvDetail;
    private EditText etFacesetToken;
    private ListView lv;
    private ArrayUserNameAdapter mAdapter;
    private List<User> mList;
    private ProgressBar pb;
    private ImgHelper deHepler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadlist);
        deHepler = new ImgHelper();
        buDetection = (Button) findViewById(R.id.bu_detection_uploadAty);
        lv = (ListView) findViewById(R.id.lv_uploadAty);
        pb = (ProgressBar) findViewById(R.id.pb_uploadAty);
        tvDetail = (TextView) findViewById(R.id.tv_faceDetail_uploadListAty);
        buUpdateFacesetToken = (Button) findViewById(R.id.bu_updete_faceset_token_uploadlistAty);
        etFacesetToken = (EditText) findViewById(R.id.et_faceset_token_uploadlistAty);
        tvDetail.setVisibility(View.GONE);
        mList = new ArrayList<>();
        mAdapter = new ArrayUserNameAdapter(mList,this);
        List<User> iList = deHepler.selectAllAuthid();
        mList.addAll(iList);
        lv.setAdapter(mAdapter);
        buDetection.setOnClickListener(this);
        String facesettoken = Util.getFacesetToken(this);
        if(TextUtils.isEmpty(facesettoken)){
            facesettoken = Util.FACESET_TOKEN;
        }
        updateFacesetToken(facesettoken);
        buUpdateFacesetToken.setOnClickListener(this);
        FaceHttp.getFaceSetFaceToken(callback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_detection_uploadAty:
                pb.setVisibility(View.VISIBLE);
                FaceHttp.getFaceSetFaceToken(callback);
                mList.clear();
                List<User> iList = deHepler.selectAllAuthid();
                if(iList!=null&&iList.size()>0){
                    mList.addAll(iList);
                    mAdapter.notifyDataSetChanged();
                }
                buDetection.setText("共"+mList.size());
                break;
            case R.id.bu_updete_faceset_token_uploadlistAty:
                String facesetToken = etFacesetToken.getText().toString();
                updateFacesetToken(facesetToken);
                break;

        }
    }
    private void updateFacesetToken(String facesetToken){
        if(TextUtils.isEmpty(facesetToken)){
            return;
        }
        etFacesetToken.setText(facesetToken);
        Util.setFacesetToken(this,facesetToken);
        Util.FACESET_TOKEN = facesetToken;
        Toast.makeText(this,"修改成功！",Toast.LENGTH_SHORT).show();
    }
    private XutilsCallback callback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            pb.setVisibility(View.GONE);
            if(TextUtils.isEmpty(result)){
                result = "null";
            }
            tvDetail.setText(result);
            showT(result);
            L.i("Creater+-->"+result);
        }
    };
    public void showT(String str){
        L.i(str);
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
