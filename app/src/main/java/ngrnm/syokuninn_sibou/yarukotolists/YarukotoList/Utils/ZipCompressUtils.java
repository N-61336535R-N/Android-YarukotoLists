package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.List;

/**
 * ZipCompressUtils は、ZIP 圧縮をおこなう上で利便性の高い機能を提供します。
 *
 * @author saka-en.
 * @version $Revision: 1.0 $ $Date: 2013.10.24 $ $Description: 新規作成 $
 */
public class ZipCompressUtils {
    
    /**
     * ファイルとフォルダを圧縮する
     * @param entrys 圧縮するFile, Directory のリスト。File.Filelist()
     * @param zipFilePath 生成されるzipファイルのパス
     * */
    public void compressFiles(List<File> entrys, String zipFilePath) {
        try{
            ZipFile zipFile = new ZipFile(zipFilePath);
            
            /** パラメータ */
            ZipParameters params = new ZipParameters();
            params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            
            /** ファイルの圧縮 */
            for(File entry : entrys){
                if(entry.isFile())
                    zipFile.addFile(entry, params);
                else
                    zipFile.addFolder(entry, params);
            }
        }catch(ZipException e) {
            e.printStackTrace();
        }
    }
}