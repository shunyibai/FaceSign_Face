package humanface.pwc.com.facesign_face.db;
/**
 * 用来获取用户名  用户图片id的工具类
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import humanface.pwc.com.facesign_face.MyApplication;
import humanface.pwc.com.facesign_face.bean.User;
import humanface.pwc.com.facesign_face.util.Util;

public class ImgHelper {
	public iConnectDBHelper dbHelper;
	public ImgHelper(){
		this.dbHelper= iConnectDBHelper.getInstance(MyApplication.getInstance());
	}
	/**
	 * 插入一条数据
	 * @param FACE_TOKEN
	 * @param USER_ID
     * @return
     */
	public long replace(String FACE_TOKEN, String USER_ID){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("FACE_TOKEN", FACE_TOKEN);
		values.put("USER_ID", USER_ID);
		values.put("FACESET_TOKEN", Util.FACESET_TOKEN);
		long res =  db.replace("img_table","STAFF_ID",values);
		return res;
	}

	/**
	 * 插入一条上传错误数据
	 * @param Error 上传错误原因
	 * @param USER_ID 图片名
	 * @param UPLOAD_NAME 本次上传名
     * @return
     */
	public long replaceERROR(String Error, String USER_ID,String UPLOAD_NAME){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ERROR", Error);
		values.put("USER_ID", USER_ID);
		values.put("FACESET_TOKEN", Util.FACESET_TOKEN);
		values.put("UPLOAD_NAME",UPLOAD_NAME);
		long res =  db.replace("error_table","ERROR",values);
		return res;
	}
	/**
	 * 查询所有图片
	 * @return
	 */
	public List<String>  selectAllError(){
		List<String> list=new ArrayList<>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from error_table where FACESET_TOKEN = '"+Util.FACESET_TOKEN+"'";
		String[] selectionArgs = new String[] {};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		while (cursor.moveToNext()){
			String USER_ID = cursor.getString(cursor.getColumnIndex("USER_ID"));
			String UPLOAD_NAME = cursor.getString(cursor.getColumnIndex("UPLOAD_NAME"));
			String ERROR = cursor.getString(cursor.getColumnIndex("ERROR"));
			StringBuilder str = new StringBuilder(USER_ID);
			str.append(" ");
			str.append(UPLOAD_NAME);
			str.append(" ");
			str.append(ERROR);
			list.add(str.toString());
		}
		Log.i("img_table","数据库查询结果" + list.size());
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 查询所有图片
	 * @return
     */
	public List<User> selectAllAuthid(){
		List<User> list=new ArrayList<>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from img_table where FACESET_TOKEN = '"+Util.FACESET_TOKEN+"'";
		String[] selectionArgs = new String[] {};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		while (cursor.moveToNext()){
			String FACE_TOKEN = cursor.getString(cursor.getColumnIndex("FACE_TOKEN"));
			String USER_ID = cursor.getString(cursor.getColumnIndex("USER_ID"));
			User user = new User();
			user.face_token = FACE_TOKEN;
			user.user_id = USER_ID;
			list.add(user);
		}
		Log.i("img_table","数据库查询结果" + list.size());
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 查询图片密码
	 * @return
	 */
	public String selectUserToken(String staffid){
		String password=null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select FACE_TOKEN from img_table where USER_ID=? and FACESET_TOKEN = ?";
		String[] selectionArgs = new String[] {staffid,Util.FACESET_TOKEN};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if (cursor.moveToNext()){
			password = cursor.getString(cursor.getColumnIndex("FACE_TOKEN"));
		}
		Log.i("img_table","FACE_TOKEN" + password);
		cursor.close();
		db.close();
		return password;
	}
	/**
	 * 根据验证码，查询图片id
	 * @return
	 */
	public String passSelectAuthid(String pass){
		String password=null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select Auth_Id from img_table where Password=?";
		String[] selectionArgs = new String[] {pass};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if (cursor.moveToNext()){
			password = cursor.getString(cursor.getColumnIndex("Auth_Id"));
		}
		Log.i("img_table","password" + password);
		cursor.close();
		db.close();
		return password;
	}

	/**
	 * 根据号查询图片id
	 * @param phone_number
	 * @return
     */
	public String selectPhoto(String phone_number){
		if(TextUtils.isEmpty(phone_number)){
			return null;
		}
		String Auth_Id=null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select Auth_Id from img_table where phone_number=?";
		String[] selectionArgs = new String[] {phone_number};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if (cursor.moveToNext()){
			Auth_Id = cursor.getString(cursor.getColumnIndex("Auth_Id"));
		}
		Log.i("img_table","Auth_Id" + Auth_Id);
		cursor.close();
		db.close();
		return Auth_Id;
	}

	/**
	 * 删除某个图片
	 * @param authid
	 * @return
     */
	public long deleteImgStaffid(String staffid){
		if(TextUtils.isEmpty(staffid)){
			return - 1;
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		long sum= db.delete("img_table","USER_ID = ? and FACESET_TOKEN = ?",new String[]{staffid,Util.FACESET_TOKEN});
		db.close();
		return sum;
	}
	public long deleteImgTable(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		long sum= db.delete("img_table",null,null);
		db.close();
		return sum;
	}
}