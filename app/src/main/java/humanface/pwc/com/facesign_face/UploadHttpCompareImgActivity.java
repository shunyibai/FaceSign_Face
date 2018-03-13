package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.adapter.ArrayFaceNameAdapter;
import humanface.pwc.com.facesign_face.bean.AddFaceSet;
import humanface.pwc.com.facesign_face.bean.Face;
import humanface.pwc.com.facesign_face.bean.FaceResponse;
import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.StrUtils;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class UploadHttpCompareImgActivity extends Activity implements View.OnClickListener {
    private Button  buOneStaffid;
    private EditText etStaffid;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_compare);
        buOneStaffid = (Button) findViewById(R.id.bu_compare_compareAty);
        etStaffid = (EditText) findViewById(R.id.bu_staffid_compareAty);
        buOneStaffid.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv_detail_comparAty);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_compare_compareAty:
                String staff = etStaffid.getText().toString();
                StringBuilder str = new StringBuilder(Util.IMA_PATH);
                str.append(staff);
                FaceHttp.searchImage_url(str.toString(),setUseridCallback);
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
                showT("返回空");
                return;
            }
            Log.i("Upload","onSuccess result-->" + result);
            tv.setText(StrUtils.formatString(result));
            Util.toLogAty(UploadHttpCompareImgActivity.this,result);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            showT("请求失败");
        }
    };

    public void showT(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
