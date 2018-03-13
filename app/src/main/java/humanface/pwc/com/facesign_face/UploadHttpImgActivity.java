package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.adapter.ArrayFaceNameAdapter;
import humanface.pwc.com.facesign_face.bean.AddFaceSet;
import humanface.pwc.com.facesign_face.bean.Face;
import humanface.pwc.com.facesign_face.bean.FaceResponse;
import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * staffid上传
 * Created by Shunyi Bai on 25/10/2017.
 */

public class UploadHttpImgActivity extends Activity implements View.OnClickListener {
    private Button buUpload, buOneStaffid;
    private ListView lv;
    private ArrayFaceNameAdapter mAdapter;
    private List<String> mList;
    private String mFacetoken,mUserid;
    private boolean isSucess = false;//服务器是否成功
    private int post = 0;//长传图片数组的顺序
    private ProgressBar pb;
    private String[] mStaffid;
    private EditText etStaffid,etUploadName;
    private ImgHelper deHepler;
    //上传名
    private String setName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        buOneStaffid = (Button) findViewById(R.id.bu_oneStaffid_uploadAty);
        buUpload = (Button) findViewById(R.id.bu_upload_uploadAty);
        etStaffid = (EditText) findViewById(R.id.et_staffidOne_uploadAty);
        etUploadName = (EditText) findViewById(R.id.et_uploadName_uploadStaffidAty);
        lv = (ListView) findViewById(R.id.lv_uploadAty);
        pb = (ProgressBar) findViewById(R.id.pb_uploadAty);
        mList = new ArrayList<>();
        mAdapter = new ArrayFaceNameAdapter(mList,this);
        lv.setAdapter(mAdapter);
        buUpload.setOnClickListener(this);
        buOneStaffid.setOnClickListener(this);
       int ss =  Util.gts20171227_staffid.split(",").length;
        L.e("size=="+ss);
        deHepler = new ImgHelper();//5a99963b43fb28964d16b182e0e69945

        buUpload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_oneStaffid_uploadAty:
                //单个上传
                mList.clear();
                String staffid = etStaffid.getText().toString();
                if(TextUtils.isEmpty(staffid)){
                    showT("Staffid为空");
                    return;
                }
                mStaffid = new String[]{staffid};
                pb.setVisibility(View.VISIBLE);
                newFace(post);
                break;
            case R.id.bu_upload_uploadAty:
                setName = etUploadName.getText().toString();
                if(TextUtils.isEmpty(setName)){
                    showT("请填写上传备注");
                    return;
                }
                //批量上传
                mStaffid = Util.gts_fox_2018013_staffid.split(",");
                pb.setVisibility(View.VISIBLE);
                newFace(post);
                break;
        }
    }
    private void getStaffidList(){
        Util.gts_fox_2018013_staffid.split(",");
    }
    private XutilsCallback createFaceokenCallback = new XutilsCallback(){

        @Override
        public void onSuccess(String result) {
            Log.i("Upload","onSuccess result-->" + result);
            if (TextUtils.isEmpty(result)) {
                showT("服务器错误 501");
                return;
            }
            FaceResponse response = JSON.parseObject(result, FaceResponse.class);
            if (response == null) {
                showT("服务器错误 503");
                return;
            }
            String faces = response.getFaces();
            if (TextUtils.isEmpty(faces)) {
                showT("没有查询到人脸");
                return;
            }
            List<Face> list = JSON.parseArray(faces, Face.class);
            if (list == null || list.size() != 1) {
                showT("没有查询到关键人脸");
                return;
            }
            Face face = list.get(0);
            if (face == null) {
                showT("没有查询到人脸信息");
                return;
            }
            mFacetoken = face.getFace_token();
            if (TextUtils.isEmpty(mFacetoken)) {
                showT("没有查询到人脸faceToken");
                return;
            }
            isSucess = true;
        }
        @Override
        public void onFinished() {
            //无论成功还是失败，都会调用此方法
            Log.e("onFinished","createFaceokenCallback");
            super.onFinished();
            if(!isSucess){
                deHepler.replaceERROR("人脸上传失败",mUserid,setName);
            }
            nextStep(1);

        }
    };
    /**
     * 给人脸添加userid
     */
    private XutilsCallback setUseridCallback = new XutilsCallback(){

        @Override
        public void onSuccess(String result) {
            Log.i("Upload","onSuccess result-->" + result);
            try {
                JSONObject json = new JSONObject(result);
                String error = json.optString("error_message");
                if(TextUtils.isEmpty(error)){
                    isSucess = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinished() {
            super.onFinished();
            if(!isSucess){
                deHepler.replaceERROR("给人脸添加userid失败",mUserid,setName);
            }
            nextStep(2);
        }
    };
    /**
     * 添加到人脸集合
     */
    private XutilsCallback addFaceSetCallback = new XutilsCallback(){
        @Override
        public void onSuccess(String result) {
            Log.i("Upload","onSuccess result-->" + result);
            if (TextUtils.isEmpty(result)) {
                showT("服务器错误 501");
                deHepler.replaceERROR("添加到人脸集合501",mUserid,setName);
                return;
            }
            AddFaceSet response = JSON.parseObject(result, AddFaceSet.class);
            if (response == null) {
                deHepler.replaceERROR("添加到人脸集合503",mUserid,setName);
                showT("服务器错误 503");
                return;
            }
            String faceset_token = response.getFaceset_token();
            if(TextUtils.isEmpty(faceset_token)){
                deHepler.replaceERROR("添加到人脸集合503",mUserid,setName);
                return;
            }
            mList.add(mUserid);
            mAdapter.notifyDataSetChanged();
            showT("上传成功"+mUserid);
            deHepler.replace(mFacetoken,mUserid);//向数据库插入数据，表示成功
            buUpload.setText("共"+mList.size());
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            deHepler.replaceERROR("添加到人脸集合503",mUserid,setName);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            nextStep(3);
        }
    };


    /**
     *下一步
     * @param item
     */
    public void nextStep(int item){
        if(isSucess){
            //如果f==true，那么是通过onSuccess返回的，不需要进行下一个；
            isSucess = false;
            switch (item){
                case 1://添加信息
                    FaceHttp.setUseridFace(mUserid,mFacetoken,setUseridCallback);
                    break;
                case 2://添加到集合
                    FaceHttp.addFaceSet(mFacetoken,addFaceSetCallback);
                    break;
            }
        }else{
            //某一步失败了
            post++;
            mFacetoken = null;
            if(post<mStaffid.length){
                newFace(post);
            }else{
                pb.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 上传人脸
     * @param post
     */
    private void newFace(int post){
        if(post>=mStaffid.length){
            showT("已经传完了！");
            return;
        }
        String staffid = mStaffid[post];
        StringBuilder path = new StringBuilder(Util.IMA_PATH);
        path.append(staffid);
        mUserid = staffid;
        FaceHttp.addImgUrl(path.toString(),createFaceokenCallback);//添加人脸
    }
    public void showT(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
