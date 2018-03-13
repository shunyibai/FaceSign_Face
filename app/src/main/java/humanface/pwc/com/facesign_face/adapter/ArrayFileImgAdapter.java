package humanface.pwc.com.facesign_face.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import humanface.pwc.com.facesign_face.R;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class ArrayFileImgAdapter extends BaseAdapter{
    private List<File> mList;
    private Context mContext;

    public ArrayFileImgAdapter(List<File> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_imgfile_search,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) view.findViewById(R.id.tv_name_imgItem);
            viewHolder.ivImg = (ImageView) view.findViewById(R.id.iv_icon_imgItem);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        File file = mList.get(i);
        String filename = file.getName();
        Bitmap b = decodeFile(file);
        if(b!=null){
            viewHolder.ivImg.setImageBitmap(b);
        }else{
            viewHolder.ivImg.setImageResource(R.drawable.ic_launcher);
        }
        int j = filename.lastIndexOf(".");
        String name = filename.substring(0,j);//得到文件后缀

        viewHolder.tv.setText(name);
        return view;
    }
    static class ViewHolder{
        TextView tv;
        ImageView ivImg;
    }
    int  IMAGE_MAX_SIZE = 400;
    private Bitmap decodeFile(File f){
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
