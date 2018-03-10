package ngrnm.syokuninn_sibou.yarukotolists.YListItem.Items;

/**
 * Created by M.R on 2017/06/25.
 */

public class History {
    /** 選択した項目の内容の一時保管場所
     *  [0] タイトル
     *  [1] 概要
     *  [2] 詳細メモ    */
    private int tag;    // ↑ [tag] 〜
    private CharSequence s;
    
    public History(int tag) {
        this.tag = tag;
    }
    
    public void setS(CharSequence s) {
        this.s = s;
    }
    
    public int getTag() {
        return tag;
    }
    
    public CharSequence getS() {
        return s;
    }
}
