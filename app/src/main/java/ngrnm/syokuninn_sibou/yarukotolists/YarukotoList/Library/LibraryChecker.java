package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Checker;

/**
 * Created by M.R on 2017/06/04.
 */

public class LibraryChecker extends Checker {
    private List<String> Names, ImgNames;
    private String kind;  // "Category" or "Lists" or "Item"
    private int LIMIT_kind;
    private int kind_num;
    
    private String kindDirPath;
    
    
    public LibraryChecker(String kind) {
        super(kind);
    }
    
    @Override
    protected void determPath(String kind) {
        // rootDir には、context.getFilesDir().getPath() + "/カテゴリ名" + "/リスト名"
        // (今いる位置（階層）のタイトルまでを、rootDirの末尾に追加して渡す。)
        this.kind = kind;
        switch (kind) {
            case "Category":
                LIMIT_kind = Consts.LIMIT_Category;
                break;
            case "Lists":
                LIMIT_kind = Consts.LIMIT_Lists;
                break;
        }
        kindDirPath = Consts.rootPath + Consts.combinePath(Consts.libraryName);
    }
    
    @Override
    public void check() {
        File kindDir = new File(kindDirPath);
        File checkF;
        
        /*
         * [kind].list が
         * 存在しない
         *      → 作る
         * 存在する
         *      → 読み込みを開始
         */  
        checkF =  new File(kindDirPath + kind + ".list");
    
        List<String> dirList = getDirList(kindDir);
        int dirNum = dirList.size();
        StringBuilder sb = new StringBuilder();
    
        if( ! checkF.exists() ) {
            kindDir.mkdirs();
            try {
                checkF.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Category.list が存在しない ＝  なんらかの事情でデータが消失した or 初回起動
             * Category.list がないとどうしようもないので、
             * ok●まず、Category.list を作る。
             * ●初回起動の説明ページを表示。
             */
            /**
             *  ok●すでにディレクトリが存在しているのなら、
             *      ディレクトリの名前をリストに組み込む
             *      ※ 「データベースが破損しました。修復を試みますか？」
             *          というメッセージのあと、同意を得てからする。
             */
            if ( dirNum == 0 ) {
                // No_Image の場合は、無条件にあの画像になる。
                sb.append(kind+"名"+",No_Image\n");
                DirFile.writeAll(checkF, sb.toString());
            }
    
            // カテゴリーの階層でtrueになった場合は、●初回起動の説明ページを表示。
        }
        
        
        readLibraryFile(checkF);
        sb = new StringBuilder();
        sb.append(DirFile.readAll(checkF));
        /*
         * ○kind+".list "に書いてある [kindの要素の名前のついたディレクトリ] が全て存在しているかを確認。
         * ○なければ作る
         */
        for (String dir : dirList) {
            if ( !Names.contains(dir) ) {
                sb.append(dir);
                Names.add(dir);
                
                File ImgFile = new File(kindDirPath + dir + "/" + Consts.LIB_IMG_NAME);
                if (ImgFile.exists()) {
                    sb.append(","+Consts.LIB_IMG_NAME);
                    ImgNames.add(Consts.LIB_IMG_NAME);
                } else {
                    sb.append(",No_Image");
                    ImgNames.add(",No_Image");
                }
                sb.append("\n");
            }
        }
        DirFile.writeAll(checkF, sb.toString());
        kind_num = Names.size();
        
        // ディレクトリがなければ作る
        for(int i=0; i<kind_num; i++) {
            if ( !( checkF = new File(kindDirPath+ Names.get(i)) ).exists() ) {
                checkF.mkdir();
            }
        }
    }
    protected void readLibraryFile(File checkF) {
        // kind+".list " を読み込み
        String[] List_checkDir = DirFile.readAll(checkF).split("\n", -1);
        Names = new ArrayList<>();
        ImgNames = new ArrayList<>();
        for(String categ : List_checkDir) {
            if (categ.equals("")) break;
            Names.add( categ.split(",", -1)[0] );
            ImgNames.add( categ.split(",", -1)[1] );
        }
    }
    
    @Override
    protected void ReflectUpdate() {
        
    }
    
    private static List<String> getDirList(File Dir) {
        File[] dirs = Dir.listFiles();
        List<String> dirList = new ArrayList<>();
        
        if (dirs!=null) {
            for (File dir : dirs) {
                if (dir.isDirectory()) {
                    dirList.add(dir.getName());
                }
            }
        }
        return dirList;
    }
    
    

    // .list に変更を追記
    private void update_listF() {
        File listF =  new File(kindDirPath + kind + ".list");
        StringBuilder sb = new StringBuilder();
    
        for (int i=0; i<kind_num; i++) {
            sb.append(Names.get(i)+","+ImgNames.get(i));
            if (i != kind_num-1) sb.append("\n");
        }
        DirFile.writeAll(listF, sb.toString());
    }
    
    // 新しいライブラリ（カテゴリ、リスト）を作成
    @Override
    public void addNew(String ItemName, String option) {
        makeNewLibrary(ItemName, option);
    }
    private void makeNewLibrary(String ItemName, String ItemImageName) {
        /**
         * 同じ名前があった場合は例外を投げる
         */
        Names.add(ItemName);
        ImgNames.add(ItemImageName);
        kind_num++;
        // Items.list を更新
        update_listF();
        // ディレクトリ作成
        new File(kindDirPath + Names.get(kind_num-1)).mkdir();
    }
    
    // 選択したライブラリ（カテゴリ、リスト）を削除
    @Override
    public void remove(int posi) {
        removeLibrary(posi);
    }
    private void removeLibrary(int posi) {
        // ディレクトリ削除
        delete( new File(kindDirPath + Names.get(posi)) );
        Names.remove(posi);
        ImgNames.remove(posi);
        kind_num--;
        // Items.list を更新
        update_listF();
    }
    
    private static void delete(File f) {
         // ファイルまたはディレクトリが存在しない場合は何もしない
        if(f.exists() == false) {  return;  }
        
        if(f.isFile()) {
             // ファイルの場合は削除する
            f.delete();
        } else if(f.isDirectory()){
             // ディレクトリの場合は、すべてのファイルを削除する
 
             // 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
            File[] files = f.listFiles();
 
             // ファイルおよびディレクトリをすべて削除
            for(int i=0; i<files.length; i++) {
                 // 自身をコールし、再帰的に削除する
                delete( files[i] );
            }
             // 自ディレクトリを削除する
            f.delete();
        }
    }
    
    
    
    @Override public List<String>getNames() {
        return Names;
    }
    public List<String> getImgNames() {
        return ImgNames;
    }
    public String getKindDirPath() {
        return kindDirPath;
    }
    @Override public int getSize() {
        if (kind_num != Names.size()) {
            throw new IllegalStateException("[Check]サイズが正しく記録できていません。");
        }
        return kind_num;
    }
    @Override public int getLIMIT_kind() {
        return LIMIT_kind;
    }
    
    
}
