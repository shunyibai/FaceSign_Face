package humanface.pwc.com.facesign_face.bean;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class Presen {
//    public void haveFace( byte[] imgData) {
//            Log.i("tag","发起比对");
//            Bitmap bitmap = toJepeByte(imgData);
//            bitmap = ImageFilePath.rotateMyBitmap(bitmap);
//            bitmap = ImageSizeUtils.ratio(bitmap,400,400);
//            byte[] imgdata1s = ImageFilePath.Bitmap2Bytes(bitmap);
//            String imgStr = Base64.encodeToString(imgdata1s, Base64.DEFAULT);
//
//            RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/search");
//            params.addBodyParameter("api_key", Util.API_KEY);
//            params.addBodyParameter("api_secret", Util.API_SECRET);
//            params.addBodyParameter("image_base64", imgStr);
//            params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
//            Log.i("img","image-->"+imgStr);
//            x.http().post(params,xCompareCallBack);
//            changeLayout(LAYOUT_COMPARISON);
//            return;
//    }
//    private  Callback.CommonCallback<String> xCompareCallBack = new Callback.CommonCallback<String>(){
//
//        @Override
//        public void onSuccess(String result) {
//            Log.i("Face比对返回 result-->"+result);
//            if(TextUtils.isEmpty(result)){
//                showT("服务器错误 501");
//                return;
//            }
//            try {
//                JSONObject jsonObject =  new JSONObject(result);
//                String results =  jsonObject.optString("results");
//                if(TextUtils.isEmpty(results)){
//                    showT("没有检测到人脸");
//                    return;
//                }
//                JSONArray array = new JSONArray(results);
//                if(array==null||array.length()<=0){
//                    showT("没有检测到人脸数组");
//                    return;
//                }
//                JSONObject jsonObject1 = array.getJSONObject(0);
//                String confidece = jsonObject1.optString("confidence");
//                String face_token = jsonObject1.optString("face_token");
//                if(TextUtils.isEmpty(confidece)||TextUtils.isEmpty(face_token)){
//                    return;
//                }
//                float confideceF = Float.parseFloat(confidece);
//                if(confideceF<70){
//                    showT("没有存包！"+confidece);
//                    return;
//                }
//                mFace_token = face_token;
//                passwordDialog.showDialog();
//                showT("检测成功！"+confidece);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError(Throwable ex, boolean isOnCallback) {
//            showT("服务器错误 502"+ex.toString());
//            L.i("onError result-->"+ex.toString());
//        }
//
//        @Override
//        public void onCancelled(CancelledException cex) {
//            L.i("onCancelled result-->"+cex.toString());
//        }
//
//        @Override
//        public void onFinished() {
//
//        }
//    };
}
