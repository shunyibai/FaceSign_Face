package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.util.L;
import humanface.pwc.com.facesign_face.util.Util;
import humanface.pwc.com.facesign_face.util.XutilsCallback;

/**
 * Created by Shunyi Bai on 03/11/2017.
 */

public class CreatePeopleActivity extends Activity implements View.OnClickListener {
    private Button buSubmit, buSelect;
    private EditText etGuesName, etPhone, etEmail, etJobtile;
    private ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpeople);
        buSubmit = (Button) findViewById(R.id.bu_subimt_createAty);
        ivIcon = (ImageView) findViewById(R.id.iv_icon_createPeople);
        buSelect = (Button) findViewById(R.id.bu_select_createAty);

        etPhone = (EditText) findViewById(R.id.et_Phone_createAty);
        etEmail = (EditText) findViewById(R.id.et_Email_createAty);
        etJobtile = (EditText) findViewById(R.id.et_JobTitle_createAty);
        etGuesName = (EditText) findViewById(R.id.et_GuestName_createAty);

        buSubmit.setOnClickListener(this);
        buSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_subimt_createAty:
                submitUtil();
                break;
            case R.id.bu_select_createAty:
                select();
                break;
        }
    }

    private void submitUtil2() {
        RequestParams params = new RequestParams("http://smartofficedev.pwchk.com/visitor/api/mobile/CreateVisit");
        JSONObject j = new JSONObject();
        params.addBodyParameter("VisitType", "1");
        params.addBodyParameter("GroupType", "0");
        params.addBodyParameter("Purpose", "TEST");
        params.addBodyParameter("CompanyName", "普华永道");
        params.addBodyParameter("Contact", "白顺义");
        params.addBodyParameter("Sources", "IPAD");
        params.addBodyParameter("IsSignIn", "Y");
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ivIcon.setImageBitmap(bitmap);
        byte[] img = Bitmap2Bytes(bitmap);
        List<File> iList = refreshFileList(MyApplication.getInstance().getImgPath());
        L.i("img-->" + img.length);
        try {
            object.put("GuestName", etGuesName.getText().toString() + "");
            object.put("Email", etEmail.getText().toString() + "");
            object.put("Status", "WaitPrint");
            object.put("Phone", etPhone.getText().toString() + "");
            object.put("JobTitle", etJobtile.getText().toString() + "");
            object.put("Avatar", iList.get(0));
            jsonArray.put(object);
            L.i("JSON-->" + j.toString());
            params.addParameter("Guests", jsonArray);
//            params.setBodyContent(j.toString());
            x.http().post(params, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private RequestParams requestParams = new RequestParams(){

    };
    private void submitUtil() {
        RequestParams params = new RequestParams("http://smartofficedev.pwchk.com/visitor/api/mobile/CreateVisit");
        JSONObject j = new JSONObject();
        try {
            j.put("VisitType", "1");
            j.put("GroupType", "0");
            j.put("Purpose", "TEST");
            j.put("CompanyName", "普华永道");
            j.put("Contact", "白顺义");
            j.put("Sources", "IPAD");
            j.put("IsSignIn", "Y");
            JSONArray jsonArray = new JSONArray();
            JSONObject object = new JSONObject();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ivIcon.setImageBitmap(bitmap);
            byte[] img = Bitmap2Bytes(bitmap);
            JSONArray jimg = new JSONArray();
            for(int i=0;i<img.length;i++){
                jimg.put((int)img[i]);
            }
            object.put("GuestName", etGuesName.getText().toString() + "");
            object.put("Email", etEmail.getText().toString() + "");
            object.put("Status", "WaitPrint");
            object.put("Phone", etPhone.getText().toString() + "");
            object.put("JobTitle", etJobtile.getText().toString() + "");
            object.put("Avatar", jimg);
            jsonArray.put(object);
            j.put("Guests", jsonArray);
            params.setBodyContent(j.toString());
            x.http().post(params, callback);

            L.i("JSON-->" + j.toString());
            L.i("img-->" + img.length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private XutilsCallback callback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            if (TextUtils.isEmpty(result)) {
                result = "null";
            }
            L.i("result" + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String error = jsonObject.optString("error_message");
                if (TextUtils.isEmpty(error)) {
                    Toast.makeText(CreatePeopleActivity.this, "成功" + result, Toast.LENGTH_SHORT).show();
                    L.i("Creater+-->" + result);
                } else {
                    Toast.makeText(CreatePeopleActivity.this, error, Toast.LENGTH_SHORT).show();
                    L.i("Creater+-->" + result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void select() {
        RequestParams params = new RequestParams("http://smartofficedev.pwchk.com/visitor/api/mobile/FindOneVisitByIdentity");
        params.addBodyParameter("Identity", etPhone.getText().toString() + "");
        x.http().post(params, selectCallback);
    }

    private XutilsCallback selectCallback = new XutilsCallback() {
        @Override
        public void onSuccess(String result) {
            if (TextUtils.isEmpty(result)) {
                result = "null";
            }
            L.i(result);
            L.i("result" + result);
            Toast.makeText(CreatePeopleActivity.this, result, Toast.LENGTH_SHORT).show();

        }
    };

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
}
