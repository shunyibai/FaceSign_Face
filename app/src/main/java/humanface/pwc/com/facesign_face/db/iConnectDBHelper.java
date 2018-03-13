package humanface.pwc.com.facesign_face.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class iConnectDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	private static iConnectDBHelper instance;
	private  iConnectDBHelper(Context context) {
		super(context, "Img", null, DATABASE_VERSION);

	}
	public static iConnectDBHelper getInstance(Context context) {
		synchronized (iConnectDBHelper.class) {
			if (instance == null) {
				instance = new iConnectDBHelper(context);
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE img_table (FACE_TOKEN VARCHAR PRIMARY KEY,"
				+ "USER_ID VARCHAR, FACESET_TOKEN VARCHAR)");//上传成功记录
		db.execSQL("CREATE TABLE error_table (USER_ID VARCHAR PRIMARY KEY,"
				+ "ERROR VARCHAR, UPLOAD_NAME VARCHAR,FACESET_TOKEN VARCHAR)");//批量上传，错误日志
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldDbNum, int newDbNum) {
		if(oldDbNum==1){
			db.execSQL("CREATE TABLE error_table (USER_ID VARCHAR PRIMARY KEY,"
					+ "ERROR VARCHAR, UPLOAD_NAME VARCHAR)");
		}
	}

}
