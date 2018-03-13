package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.adapter.ArrayFileNameAdapter;
import humanface.pwc.com.facesign_face.bean.AddFaceSet;
import humanface.pwc.com.facesign_face.bean.Face;
import humanface.pwc.com.facesign_face.bean.FaceResponse;
import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.db.iConnectDBHelper;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * 本地上传
 * Created by Shunyi Bai on 25/10/2017.
 */

public class UploadActivity extends Activity implements View.OnClickListener {
    private Button buUpload, buDetection;
    private ListView lv;
    private ArrayFileNameAdapter mAdapter;
    private List<File> mList;
    private String mFacetoken,mUserid;
    private boolean isSucess = false;//服务器是否成功
    private int post = 0;//长传图片数组的顺序
    private ProgressBar pb;
    private ImgHelper deHepler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfile);
        buDetection = (Button) findViewById(R.id.bu_detection_uploadAty);
        buUpload = (Button) findViewById(R.id.bu_upload_uploadAty);
        lv = (ListView) findViewById(R.id.lv_uploadAty);
        pb = (ProgressBar) findViewById(R.id.pb_uploadAty);
        mList = new ArrayList<>();
        mAdapter = new ArrayFileNameAdapter(mList,this);
        lv.setAdapter(mAdapter);
        buUpload.setOnClickListener(this);
        buDetection.setOnClickListener(this);

        deHepler = new ImgHelper();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_detection_uploadAty:
                mList.clear();
                List<File> iList = refreshFileList(MyApplication.getInstance().getImgPath());
                if(iList!=null&&iList.size()>0){
                    mList.addAll(iList);
                    mAdapter.notifyDataSetChanged();
                }
                buDetection.setText("共"+mList.size());
                break;
            case R.id.bu_upload_uploadAty:
                if(mList.size()==0){
                    showT("数据为空，不能上传");
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                newFace(post);
                break;
        }
    }

    public ArrayList<File> refreshFileList(File strPath) {
        String filename;//文件名
        String suf;//文件后缀
        File dir = strPath;//文件夹dir
        File[] files = dir.listFiles();//文件夹下的所有文件或文件夹
        if (files == null)
            return null;
        ArrayList<File> wechats = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                filename = files[i].getName();
                int j = filename.lastIndexOf(".");
                suf = filename.substring(j + 1);//得到文件后缀
                wechats.add(files[i]);//对于文件才把它的路径加到filelist中
            }
        }
        return wechats;
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
            Log.e("onFinished","createFaceokenCallback");
            super.onFinished();
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
            isSucess = true;
        }

        @Override
        public void onFinished() {
            super.onFinished();
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
                return;
            }
            AddFaceSet response = JSON.parseObject(result, AddFaceSet.class);
            if (response == null) {
                showT("服务器错误 503");
                return;
            }
            String faceset_token = response.getFaceset_token();
            if(!TextUtils.isEmpty(faceset_token)){

            }
            deHepler.replace(mFacetoken,mUserid);
            showT("上传成功"+mUserid);

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
            post++;
            mFacetoken = null;
            if(post<mList.size()){
                newFace(post);
            }else{
                pb.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 开始上传人脸
     * @param post
     */
    private void newFace(int post){
        if(post>=mList.size()){
            showT("已经传完了！");
            return;
        }
        File file = mList.get(post);
        String filename = file.getName();
        int j = filename.lastIndexOf(".");
        mUserid = filename.substring(0,j);//得到文件后缀
        Log.i("MyUser_id",mUserid);
        FaceHttp.addImgFile(file,createFaceokenCallback);//添加人脸
    }
    public void showT(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
