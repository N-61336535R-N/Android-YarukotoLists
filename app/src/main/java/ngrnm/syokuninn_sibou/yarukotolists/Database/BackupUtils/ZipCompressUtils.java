package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipCompressUtils {
    
    /**
     * ファイルとフォルダを圧縮する
     * @param entrys 圧縮するFile, Directory のリスト。File.Filelist()
     * @param zipFilePath 生成されるzipファイルのパス
     * */
    public void compressFiles(List<File> entrys, String zipFilePath) {
        try{
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(zipFilePath);
            
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
        }catch (ZipException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public InputStream getZipFileInputStream(String zipFPath, String targetFName) throws FileNotFoundException {
        InputStream in = null;
        
        try{
            ZipFile zipFile = new ZipFile( zipFPath );
            Enumeration<? extends ZipEntry> enume  =  zipFile.entries();
            while( enume.hasMoreElements() ){
                ZipEntry entry = enume.nextElement();
                if ( entry.getName().equals(targetFName) ) {
                    in = zipFile.getInputStream(entry);
                    break;
                }
            }
            zipFile.close();
        }catch( Exception ex ) {
            //err = -1;
            //Log.d( "Err", "// ZipIO : getList err... //" );
            ex.printStackTrace();
        }
        
        if ( in == null )  throw new FileNotFoundException("zip ファイル内を探しましたが、対象のファイルは見つかりませんでした。");
    
        return in;
    }
}