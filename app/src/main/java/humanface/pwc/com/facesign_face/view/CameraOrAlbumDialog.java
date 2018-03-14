package humanface.pwc.com.facesign_face.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Shunyi Bai on 14/03/2018.
 */

public class CameraOrAlbumDialog extends DialogFragment{
    private OnCameraOnClick click;
    public interface OnCameraOnClick{
        void onClick(DialogInterface dialog, int which);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(new String[]{"拍照","图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                click.onClick(dialog,which);
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if(activity instanceof OnCameraOnClick){
            click = (OnCameraOnClick) activity;
        }
    }
}
