/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jziptool.zipper;

import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jziptool.treer.TreeManipulator;
import org.xeustechnologies.jtar.*;

/**
 *
 * @author andymememe
 */
public class TarManipulator extends ComManipulator {
    private File _file;
    
    public TarManipulator(TreeManipulator treeMan) {
        super(treeMan);
    }

    @Override
    public boolean openCom(File file) {
        boolean result = true;
        TarInputStream tarInputStream;
        _file = file;
        TarEntry entry;
        _treeManipulator.generateRoot(_file.getName());
        try {
            tarInputStream = new TarInputStream(new BufferedInputStream(new FileInputStream(_file)));
            while((entry = tarInputStream.getNextEntry()) != null){
                String[] structure = entry.getName().split(File.separator);
                String parent;
                
                if(structure.length == 1){
                    parent = null;
                }
                else{
                    parent = structure[structure.length - 2];
                }

                _treeManipulator.addEntryNode(structure[structure.length - 1], parent, structure.length, entry.isDirectory());
            }
            tarInputStream.close();
        }
        catch (Exception ex) {
            result = false;
            Logger.getLogger(TarManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean extractCom(String dir) {
        boolean result = true;
        int count;
        byte[] data = new byte[2048];
        TarInputStream tarInputStream;
        BufferedOutputStream bufferOutputStream;
        try {
            tarInputStream = new TarInputStream(new BufferedInputStream(new FileInputStream(_file)));
            TarEntry entry;
            while((entry = tarInputStream.getNextEntry()) != null) {
                /* Entry is a dirctory */
                if(entry.isDirectory()){
                    result = _doMkDir(dir, entry.getName());
                }
                /* Entry is a file */
                else{
                    File newFile = new File(dir + File.separator + entry.getName());
                    
                    /* See if file exist or not */
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                    else {
                        Object[] option = {"覆蓋", "更名", "跳過"};
                        String msg = "「" + newFile.getName() + "」重複了，請選擇下一步:";
                        String title = "發現重複檔案";
                        int replaceResult = JOptionPane.showOptionDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, option, option[0]);
                        
                        newFile = _doReplaceJob(newFile, replaceResult);
                        if (newFile == null) {
                            return false;
                        }
                    }
                    
                    bufferOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
                    
                    while((count = tarInputStream.read(data)) != -1){
                        bufferOutputStream.write(data, 0, count);
                    }
                    
                    bufferOutputStream.flush();
                    bufferOutputStream.close();
                }
            }
            tarInputStream.close();
        }
        catch (IOException | HeadlessException ex) {
            result = false;
            Logger.getLogger(TarManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
