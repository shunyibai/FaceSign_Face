package humanface.pwc.com.facesign_face;

import android.app.Application;
import android.os.Environment;

import org.xutils.x;

import java.io.File;

import humanface.pwc.com.facesign_face.util.L;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class MyApplication extends Application {
    /**
     * {
     "faceset_token": "a9df8b711e16f6458ef462956f09af6a",
     "time_used": 112,
     "face_count": 0,
     "face_added": 0,
     "request_id": "1508919938,fa96e6f0-c6a1-4ee8-a9ea-a70895fe15e4",
     "outer_id": "",
     "failure_detail": []
     }
     */
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        x.Ext.init(this);
    }
    public static final MyApplication getInstance(){
        return application;
    }
    /**
     * 获取图片路径
     *
     * @return
     */
    public File getImgPath() {
        String pathExternal = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File file = new File(pathExternal, "MyCameraApp");
        L.i(file.toString());
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
    }
    /**
     * 获取图片路径
     *
     * @return
     */
    public File getImgPathSearch() {
        String pathExternal = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File file = new File(pathExternal, "Search");
        L.i(file.toString());
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
    }
}
