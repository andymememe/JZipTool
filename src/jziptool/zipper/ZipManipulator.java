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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import jziptool.treer.TreeManipulator;

/**
 *
 * @author andymememe
 */
public class ZipManipulator extends ComManipulator {
    private ZipFile _zipFile;

    public ZipManipulator(TreeManipulator treeMan) {
        super(treeMan);
    }
    
    /* Open a zip file */
    @Override
    public boolean openCom(File file){
        boolean result = true;
        ZipEntry entry;
        
        try {
            _zipFile = new ZipFile(file);
        }
        catch (IOException ex) {
            result = false;
            Logger.getLogger(ZipManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        _treeManipulator.generateRoot(file.getName());
        Enumeration<? extends ZipEntry> entries = _zipFile.entries();
        while(entries.hasMoreElements()){
            entry = entries.nextElement();
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
        return result;
    }
    
    /* Extract a zip file */
    @Override
    public boolean extractCom(String dir){
        boolean result = true;
        BufferedOutputStream bufferOutStream;
        BufferedInputStream bufferInStream;
        byte[] buffer = new byte[2048];
        ZipEntry entry;
        int count;
        Enumeration<? extends ZipEntry> entries = _zipFile.entries();
        
        while(entries.hasMoreElements()){
            entry = entries.nextElement();
            
            /* Entry is a dirctory */
            if(entry.isDirectory()){
                result = _doMkDir(dir, entry.getName());
            }
            /* Entry is a file */
            else{
                try {
                    bufferInStream = new BufferedInputStream(_zipFile.getInputStream(entry)); // Buffer that input the entry file data
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
                    
                    bufferOutStream = new BufferedOutputStream(new FileOutputStream(newFile), 2048); // Buffer that output the buffer data to file stream to new file
                    
                    /* count : the length now buffered stream read into buffer */
                    while ((count = bufferInStream.read(buffer, 0, 2048)) != -1) {
                        bufferOutStream.write(buffer, 0, count);
                    }
                    bufferOutStream.flush();
                    bufferOutStream.close();
                    bufferInStream.close();
                }
                catch (IOException | HeadlessException ex) {
                    result = false;
                    Logger.getLogger(ZipManipulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
}
