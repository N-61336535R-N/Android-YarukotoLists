package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/30.
 */

public class SettingConsts {
    
    private SettingConsts(){}
    
    
    /* 設定画面のデザインもろもろ */
    // 設定 のカテゴリ一覧
    static final String[] scenes = {
            // Scenes of Isle of Wight
            "表示",
            "構造",
            "バックアップ"
    };
    // アイコン一覧
    static final int[] icons = {
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
    };
    // preference.xml の一覧
    static final int[] setPref_xmls = {
            R.xml.setting_p1_hyouzi,
            R.xml.setting_p2_kouzou,
            R.xml.setting_p8_backup,
    };
    
}
