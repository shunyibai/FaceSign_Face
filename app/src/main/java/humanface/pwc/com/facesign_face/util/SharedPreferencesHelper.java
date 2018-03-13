package humanface.pwc.com.facesign_face.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shunyi Bai on 06/03/2018.
 */

public class SharedPreferencesHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Context context;

    public SharedPreferencesHelper(Context c,String name){
        context = c;
        sp = context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS);
        editor = sp.edit();
    }

    public void putValue(String key, String value){
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key){
        return sp.getString(key, null);
    }
}
