package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ryo on 2017/11/05.
 */

public class makeADialog {
    public makeADialog(String title, String message, Fragment frag) {
        MoldAlertDialogFragment fragment = new MoldAlertDialogFragment();
        fragment.setOnClickedPositiveButtonListener(false, new MoldAlertDialogFragment.OnClickedPositiveButtonListener() {
            @Override
            public void OnClickedPositiveButtonListener() {
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        FragmentTransaction ft = frag.getFragmentManager().beginTransaction();
        fragment.show(ft, "dialog");
    }
}
