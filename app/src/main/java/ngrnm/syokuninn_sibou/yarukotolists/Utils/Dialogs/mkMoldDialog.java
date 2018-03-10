package ngrnm.syokuninn_sibou.yarukotolists.Utils.Dialogs;

import android.os.Bundle;

/**
 * Created by ryo on 2017/11/05.
 */

public class mkMoldDialog {
    
    public static MoldAlertDialogFragment mkCheckDialogFragm(String title, String message) {
        MoldAlertDialogFragment dlogfragment = new MoldAlertDialogFragment();
        // OK ボタン無効化
        dlogfragment.setOnClickedPositiveButtonListener(false, () -> {});
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        dlogfragment.setArguments(bundle);
        /*
        FragmentTransaction ft = frag.getFragmentManager().beginTransaction();
        dlogfragment.show(ft, "dialog");*/
        
        return dlogfragment;
    }
}
