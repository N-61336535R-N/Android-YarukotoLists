package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Items;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayDeque;
import java.util.Deque;

import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/**
 * Created by M.R on 2017/06/25.
 * 
 * 入力があれば
 */

public class urdo_TW implements TextWatcher {
    public  static boolean allow_push = false;
    private int Ctag;
    public History history;
    public static History history_now;
    // undo・redo 関係
    private static Deque<History> ItemCs_before = new ArrayDeque<>(Consts.LIMIT_histry);  //「戻る」の格納場所
    
    public urdo_TW(int tag) {
        this.Ctag = tag;
    }
    
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (allow_push) {
            history = new History(Ctag);
            history.setS(s);
            int c = 0;
            push(history, c);
        }
    }
    
    public void afterTextChanged(Editable editable) {
        // historyは、常に最新の状態になる。
        history_now = new History(Ctag);
        history_now.setS(editable.toString());
    }
    
    
    public static void push(History history, int c) {
        if (ItemCs_before.offerFirst(history) == false) {
            ItemCs_before.removeLast();
            c++;
            if (c<3) {
                push(history, c);
            } else {
                System.out.println("push() で問題が発生しました");
            }
        }
    }
    
    public static boolean peek() {
        if (ItemCs_before.peekFirst() == null) {
            return false;
        } else { return true; }
    }
    public static History pop() {
        return ItemCs_before.pollFirst();
    }
}
