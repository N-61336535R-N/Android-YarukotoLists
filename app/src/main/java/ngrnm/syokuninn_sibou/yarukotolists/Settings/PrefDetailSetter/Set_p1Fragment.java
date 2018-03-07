package ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ngrnm.syokuninn_sibou.yarukotolists.R;


public class Set_p1Fragment extends PreferenceFragmentCompat {
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.setting_p1_hyouzi, rootKey);
    }
    
}
