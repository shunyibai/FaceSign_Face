package humanface.pwc.com.facesign_face.util;

import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Shunyi Bai on 26/10/2017.
 */

public abstract class XutilsCallback implements org.xutils.common.Callback.CommonCallback<String>{
    private String tag = "xUils";
    @Override
    public abstract void onSuccess(String result);

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Log.e(tag,"onError result-->" + ex.toString()+"\n"+ex.getLocalizedMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Log.e(tag,"onCancelled");
    }

    @Override
    public void onFinished() {
        Log.e(tag,"onFinished");
    }
}
