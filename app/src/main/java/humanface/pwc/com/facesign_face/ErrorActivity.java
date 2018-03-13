package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

import humanface.pwc.com.facesign_face.db.ImgHelper;
import humanface.pwc.com.facesign_face.util.L;

/**
 * Created by Shunyi Bai on 27/12/2017.
 */

public class ErrorActivity extends Activity{
    //打印上传失败信息
    private EditText etError;
    private ImgHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        etError = (EditText) findViewById(R.id.et_errorAty);
        helper = new ImgHelper();
        List<String> strList = helper.selectAllError();
        StringBuilder strB = new StringBuilder();
        for(String str1:strList){
            strB.append(str1);
            strB.append("\n");
        }
        etError.setText(strB.toString());
        L.i("error"+strB.toString());
    }
}
