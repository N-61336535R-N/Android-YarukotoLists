package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Items;

import java.io.File;
import java.io.IOException;

import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.DirFile;


/**
 * ItemNumber = 3 に登録されているItemなら、
 * 3[summary].Item
 * 3[detail].Item
 * に内容が保存される。
 */

public class ItemIO {
    private static File[] ItemFs = new File[3];    // [0] なし, [1] 概要(summary), [2] 詳細(detail)  を保存したファイル
    private static String[] ItemContents = new String[3];
    
    
    /**
     * 適当に追加！！
     */
    private static ItemsChecker ItC = new ItemsChecker("aaa");
    
    
    public static boolean check() {
        String ItemPath = Consts.rootPath + Consts.libraryName +"/" + Consts.listName+"/" + Consts.ItemNumber;
        ItemFs[0] = new File(ItemPath +".Item");  // ８コードに移行後に使う予定
        ItemFs[1] = new File(ItemPath +"[summary].Item");
        ItemFs[2] = new File(ItemPath +"[detail].Item");
        
        
        boolean problem = false;
        // なければ作る
        new File(Consts.rootPath + Consts.libraryName +"/" + Consts.listName+"/").mkdirs();
        for (File ItemF : ItemFs) {
            if (!ItemF.exists()) {
                try {
                    ItemF.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            problem = true;
        }
        return problem;
    }
    
    public static String[] readAllItem() {
        ItemContents[0] = ItC.getNames().get(Consts.ItemNumber);
        for (int i=1; i<3; i++) {
            ItemContents[i] = DirFile.readAll(ItemFs[i]);
        }
        return ItemContents;
    }
    
    public static void updateItem(String[] new_data) {
        ItC.getNames().set(Consts.ItemNumber, new_data[0]);
        ItC.update_listF();
        for (int i=1; i<3; i++) {
             DirFile.writeAll(ItemFs[i], new_data[i]);
        }
    }
    
}
