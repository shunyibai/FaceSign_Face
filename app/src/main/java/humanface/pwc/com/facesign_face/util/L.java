package humanface.pwc.com.facesign_face.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Shunyi Bai on 05/07/2017.
 */

public class L {
    public static void i(String content){
        L.i("",content);
    }

    public static void i(String tag, String content){
        if(TextUtils.isEmpty(tag)){
            tag = "Face";
        }
        if(TextUtils.isEmpty(content)){
            return ;
        }
        //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (content.length() > max_str_length) {
            Log.i(tag, content.substring(0, max_str_length));
            content = content.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, content);
    }
    public static void e(String content){
        L.e("",content);
    }
    public static void e(String tag, String content){
        if(TextUtils.isEmpty(tag)){
            tag = "Face";
        }
        if(TextUtils.isEmpty(content)){
            return ;
        }
        Log.e(tag,content);
    }
    public static String getUUid(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
        return str;
    }
}
