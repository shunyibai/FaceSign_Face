package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.adapter.ArrayFileImgAdapter;
import humanface.pwc.com.facesign_face.adapter.ArrayFileNameAdapter;
import humanface.pwc.com.facesign_face.bean.AddFaceSet;
import humanface.pwc.com.facesign_face.bean.Face;
import humanface.pwc.com.facesign_face.bean.FaceResponse;
import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.util.FaceHttp;
import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * 本地上传
 * Created by Shunyi Bai on 25/10/2017.
 */

public class SearchFileImgActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mLv;
    private List<File> mListFile;
    private Button buSearch;
    private ArrayFileImgAdapter mAdapter;
    private ProgressBar pb;
    private AlertDialog.Builder dialog;
    private int mPos;
    private ImgHelper deHepler;
    private String mFacetoken;
    private String mUserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_listimg);
        mLv = (ListView) findViewById(R.id.lv_content_searchFileAty);
        buSearch = (Button) findViewById(R.id.bu_search_file_searchFileAty);
        buSearch.setOnClickListener(this);
        mListFile = new ArrayList<>();
        mAdapter = new ArrayFileImgAdapter(mListFile, this);
        List<File> list = refreshFileList(MyApplication.getInstance().getImgPathSearch());
        if (list != null && list.size() > 0) {
            mListFile.clear();
            mListFile.addAll(list);
        }
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
        pb = (ProgressBar) findViewById(R.id.pb_searchFileAty);
        dialog = new AlertDialog.Builder(this);
        String item[] = new String[]{"集合搜索", "上传集合"};
        deHepler = new ImgHelper();
        dialog.setItems(item, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                switch (i) {
                    case 0:
                        searchFace(mPos);
                        break;
                    case 1:
                        uploadFaceSet(mPos);
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_search_file_searchFileAty:
                mListFile.clear();
                List<File> list = refreshFileList(MyApplication.getInstance().getImgPathSearch());
                if (list != null && list.size() > 0) {
                    mListFile.addAll(list);
                }
                mAdapter.notifyDataSetChanged();
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
        for (int i = 0; i < files.length; i++){
            if (!files[i].isDirectory()){
                filename = files[i].getName();
                int j = filename.lastIndexOf(".");
                suf = filename.substring(j + 1);//得到文件后缀
                wechats.add(files[i]);//对于文件才把它的路径加到filelist中
            }
        }
        return wechats;
    }

    private XutilsCallback searchFaceokenCallback = new XutilsCallback() {

        @Override
        public void onSuccess(String result){
            Log.i("Upload", "onSuccess result-->" + result);
            pb.setVisibility(View.GONE);
            if (TextUtils.isEmpty(result)){
                showT("服务器错误 501");
                return;
            }
            Intent intent = new Intent(SearchFileImgActivity.this, DetailLogActivity.class);
            intent.putExtra("JSON", result);
            startActivity(intent);
        }

        @Override
        public void onFinished(){
            Log.e("onFinished", "createFaceokenCallback");
            super.onFinished();
        }
    };


    public void showT(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
        mPos = i;
        dialog.create().show();
    }

    /**
     * 发起搜索
     *
     * @param i
     */
    private void searchFace(int i){
        if (i >= mListFile.size()){
            showT("下标越界");
            return;
        }
        File file = mListFile.get(i);
        L.i(file.toString());
        if (file == null){
            showT("文件为空");
            return;
        }
        String filename = file.getName();
        int j = filename.lastIndexOf(".");
        String userid = filename.substring(0, j);//得到文件后缀
        Log.i("MyUser_id", userid);
        FaceHttp.searchImage_file(file, searchFaceokenCallback);//添加人脸
        pb.setVisibility(View.VISIBLE);
    }

    /**
     * 上传到人脸集合
     *
     * @param pos
     */
    private void uploadFaceSet(int pos){
        if (pos >= mListFile.size()) {
            showT("uploadFaceSet下标越界");
            return;
        }
        File file = mListFile.get(pos);
        String filename = file.getName();
        int j = filename.lastIndexOf(".");
        mUserid = filename.substring(0, j);//得到文件后缀
        Log.i("MyUser_id", mUserid);
        FaceHttp.addImgFile(file, createFaceokenCallback);//添加人脸
        pb.setVisibility(View.VISIBLE);
    }

    private XutilsCallback createFaceokenCallback = new XutilsCallback(){

        @Override
        public void onSuccess(String result){
            pb.setVisibility(View.GONE);
            Log.i("Upload", "onSuccess result-->" + result);
            if (TextUtils.isEmpty(result)){
                showT("服务器错误 501");
                return;
            }
            FaceResponse response = JSON.parseObject(result, FaceResponse.class);
            if (response == null){
                showT("服务器错误 503");
                return;
            }
            String faces = response.getFaces();
            if (TextUtils.isEmpty(faces)){
                showT("没有查询到人脸");
                return;
            }
            List<Face> list = JSON.parseArray(faces, Face.class);
            if (list == null || list.size() < 1){
                showT("没有查询到关键人脸");
                return;
            }
            if(list.size()>1){
                showT("人脸数"+list.size()+"，人脸无法添加userid");
                return;
            }
            Face face = list.get(0);
            if (face == null){
                showT("没有查询到人脸信息");
                return;
            }
            mFacetoken = face.getFace_token();
            if (TextUtils.isEmpty(mFacetoken)){
                showT("没有查询到人脸faceToken");
                return;
            }
            FaceHttp.setUseridFace(mUserid, mFacetoken, setUseridCallback);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinished() {
            Log.e("onFinished", "createFaceokenCallback");
            super.onFinished();
        }
    };
    /**
     * 给人脸添加userid
     */
    private XutilsCallback setUseridCallback = new XutilsCallback() {

        @Override
        public void onSuccess(String result){
            pb.setVisibility(View.GONE);
            if (TextUtils.isEmpty(result)) {
                showT("添加userid失败");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String error = jsonObject.optString("error_message");
                if (!TextUtils.isEmpty(error)) {
                    showT("添加userid失败");
                } else {
                    FaceHttp.addFaceSet(mFacetoken, addFaceSetCallback);
                    pb.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    /**
     * 添加到人脸集合
     */
    private XutilsCallback addFaceSetCallback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            pb.setVisibility(View.GONE);
            Log.i("Upload", "onSuccess result-->" + result);
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
            if (!TextUtils.isEmpty(faceset_token)) {
                deHepler.replace(mFacetoken, mUserid);
                showT("上传成功" + mUserid);
            }
        }

        @Override
        public void onFinished() {
            super.onFinished();
        }
    };
}
