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
import humanface.pwc.com.facesign_face.bean.User;

/**
 * Created by Shunyi Bai on 25/10/2017.
 */

public class ArrayUserNameAdapter extends BaseAdapter{
    private List<User> mList;
    private Context mContext;

    public ArrayUserNameAdapter(List<User> mList, Context mContext) {
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
        User user = mList.get(i);
        viewHolder.tv.setText(user.face_token+"\rstaffid-->"+user.user_id);
        return view;
    }
    static class ViewHolder{
        TextView tv;
    }
}
