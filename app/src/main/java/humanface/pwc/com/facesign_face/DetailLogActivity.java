package humanface.pwc.com.facesign_face;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import humanface.pwc.com.facesign_face.util.StrUtils;

/**
 * Created by Shunyi Bai on 06/12/2017.
 */

public class DetailLogActivity extends Activity{
    private EditText tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_json);
        tv = (EditText) findViewById(R.id.tv_log_logDetailAty);
        String json = getIntent().getStringExtra("JSON");
        if(!TextUtils.isEmpty(json)){
            String str = StrUtils.formatString(json);
            tv.setText(str);
        }
    }

}
