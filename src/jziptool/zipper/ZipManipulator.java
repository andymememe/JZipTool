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
import javax.swing.tree.DefaultTreeModel;
import jziptool.treer.TreeManipulator;

/**
 *
 * @author andymememe
 */
public class ZipManipulator {
    private final TreeManipulator _treeManipulator;
    private ZipFile _zipFile;
    
    public ZipManipulator(TreeManipulator treeMan){
        _treeManipulator = treeMan; 
    }
    
    /* Open a zip file */
    public boolean openZip(File zipFile){
        boolean result = true;
        ZipEntry entry;
        
        try {
            _zipFile = new ZipFile(zipFile);
        }
        catch (IOException ex) {
            result = false;
            Logger.getLogger(ZipManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        _treeManipulator.generateRoot(zipFile.getName());
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
    public boolean extractZip(String dir){
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
    
    /* Make new folder */
    private boolean _doMkDir(String rootName, String docName){
        File newDir = new File(rootName + File.separator + docName);
        /* If dirctory is exist or not */
        if (!newDir.exists()) {
            return newDir.mkdirs();
        }
        return true;
    }
    
    /* When find exist file, what should this application do */
    private File _doReplaceJob(File newFile, int replaceResult) {
        File result = newFile;
        switch (replaceResult) {
            case JOptionPane.YES_OPTION: // Replace
                result.delete(); // Delete old file
                {
                    try {
                        result.createNewFile(); // Create new file
                    }
                    catch (IOException ex) {
                        result = null;
                        Logger.getLogger(ZipManipulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case JOptionPane.NO_OPTION: // New name
                int extPosition = result.getAbsolutePath().lastIndexOf("."); // Extension position
                int countName = 1;
                String originalPathWithoutExt, ext;
                if(extPosition != -1){
                    originalPathWithoutExt = result.getAbsolutePath().substring(0, extPosition); // Path without extension
                    ext = result.getAbsolutePath().substring(extPosition); // Extension
                }
                else{
                    originalPathWithoutExt = result.getAbsolutePath();
                    ext = "";
                }
                
                /* Make a new name */
                do {
                    result = new File(originalPathWithoutExt + "(" + countName + ")" + ext);
                    countName++;
                } while (result.exists());
                {
                    try {
                        result.createNewFile();
                    }
                    catch (IOException ex) {
                        result = null;
                        Logger.getLogger(ZipManipulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case JOptionPane.CANCEL_OPTION: // Skip
                break;
        }
        return result;
    }
    
    /* Get tree model */
    public DefaultTreeModel getZipTreeModel(){
        return _treeManipulator.getModel();
    }
}
