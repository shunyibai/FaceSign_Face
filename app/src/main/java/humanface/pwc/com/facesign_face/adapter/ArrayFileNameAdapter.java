package humanface.pwc.com.facesign_face.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import humanface.pwc.com.facesign_face.R;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class ArrayFileNameAdapter extends BaseAdapter{
    private List<File> mList;
    private Context mContext;

    public ArrayFileNameAdapter(List<File> mList, Context mContext) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_filename_tv,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) view.findViewById(R.id.tv_name_filename);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        String filename = mList.get(i).getName();
        int j = filename.lastIndexOf(".");
        String name = filename.substring(0,j);//得到文件后缀
        viewHolder.tv.setText(name);
        return view;
    }
    static class ViewHolder{
        TextView tv;
    }
}
