package humanface.pwc.com.facesign_face.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Shunyi Bai on 20/07/2017.
 */

public class FaceHttp {
    /**
     * 创建一个人脸集合
     * @param xCompareCallBack
     */
    public static void createFaceSet(Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/faceset/create");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 清除一个人脸结合中所有人脸
     * @param xCompareCallBack
     */
    public static void clearFaceSet(Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("face_tokens", "RemoveAllFaceTokens");
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 清除一个人脸结合中所有人脸
     * @param xCompareCallBack
     */
    public static void clearFaceSetOne(String fasceToken,Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("face_tokens", fasceToken);
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 人脸比较
     * @param imgStr
     * @param xCompareCallBack
     */
    public static void searchFace(String imgStr, Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/search");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_base64", imgStr);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("return_result_count","5");
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 人脸比较
     * @param face_token
     * @param xCompareCallBack
     */
    public static void searchFaceToken(String face_token, Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/search");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("face_token", face_token);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 人脸比较
     * @param xCompareCallBack
     */
    public static void searchImage_url(String image_url, Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/search");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_url", image_url);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("return_result_count","3");
        x.http().post(params,xCompareCallBack);
    }

    /**
     * 人脸比较
     * @param xCompareCallBack
     */
    public static void searchImage_file(File file, Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/search");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_file", file);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("return_result_count","5");
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 向集合中添加人脸
     * @param faceToken
     * @param xAddFaceCallBack
     */
    public static void addFaceSet(String faceToken, Callback.CommonCallback<String> xAddFaceCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/faceset/addface");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        params.addBodyParameter("face_tokens", faceToken);
        x.http().post(params, xAddFaceCallBack);
    }

    /**
     * 向Face库中添加人脸，base64
     * @param photo
     * @param xCreateUserTokenCallBack
     */
    public static void addImg(String photo, Callback.CommonCallback<String> xCreateUserTokenCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/detect");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_base64", photo);
        x.http().post(params, xCreateUserTokenCallBack);
    }
    /**
     * 向Face库中添加人脸，iamgeurl
     * @param imgurl
     * @param xCreateUserTokenCallBack
     */
    public static void addImgUrl(String imgurl, Callback.CommonCallback<String> xCreateUserTokenCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/detect");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_url", imgurl);
        x.http().post(params, xCreateUserTokenCallBack);
    }
    /**
     * 向Face库中添加人脸File
     * @param file
     * @param xCreateUserTokenCallBack
     */
    public static void addImgFile(File file, Callback.CommonCallback<String> xCreateUserTokenCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/detect");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_file", file);
        x.http().post(params, xCreateUserTokenCallBack);
    }
    /**
     * 在人脸中添加userid
     * @param faceToken
     * @param xAddFaceCallBack
     */
    public static void setUseridFace(String userid,String faceToken, Callback.CommonCallback<String> xAddFaceCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/face/setuserid");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("user_id", userid);
        params.addBodyParameter("face_token", faceToken);
        x.http().post(params, xAddFaceCallBack);
    }

    /**获取人脸集合信息
     * @param xAddFaceCallBack
     */
    public static void getFaceSetFaceToken(Callback.CommonCallback<String> xAddFaceCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/faceset/getdetail");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("faceset_token", Util.FACESET_TOKEN);
        x.http().post(params, xAddFaceCallBack);
    }

    /**
     * 人脸比较
     * @param xCompareCallBack
     */
    public static void compareFace(String imgStr1,String imgStr2, Callback.CommonCallback<String> xCompareCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/facepp/v3/compare");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_base64_1", imgStr1);
        params.addBodyParameter("image_base64_2",imgStr2);
        x.http().post(params,xCompareCallBack);
    }
    /**
     * 把图片中的文字弄出来
     * @param file
     * @param xCreateUserTokenCallBack
     */
    public static void recognizeText(File file, Callback.CommonCallback<String> xCreateUserTokenCallBack){
        RequestParams params = new RequestParams("https://api-cn.faceplusplus.com/imagepp/v1/recognizetext");
        params.addBodyParameter("api_key", Util.API_KEY);
        params.addBodyParameter("api_secret", Util.API_SECRET);
        params.addBodyParameter("image_file", file);
        x.http().post(params, xCreateUserTokenCallBack);
    }

    /**
     * 把图片中的文字显示出来（baidu）
     * @param access_token
     * @param base64Img
     * @param xCreateUserTokenCallBack
     */
    public static void recognizeBaiduText(String access_token,String base64Img, Callback.CommonCallback<String> xCreateUserTokenCallBack){


        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/general?access_token=";//备注
        path = path+access_token;//备注
        RequestParams params = new RequestParams(path);//备注
        params.addHeader("Content-Type","application/x-www-form-urlencoded");//备注
        params.addBodyParameter("image", base64Img);//备注
        x.http().post(params, xCreateUserTokenCallBack);
        //重新提交一遍
    }

}
