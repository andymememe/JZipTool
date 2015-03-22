/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jziptool.zipper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import jziptool.treer.TreeManipulator;

/**
 *
 * @author andymememe
 */
public abstract class ComManipulator {
    protected TreeManipulator _treeManipulator;
    
    public ComManipulator(TreeManipulator treeMan){
        _treeManipulator = treeMan; 
    }
    
    public abstract boolean openCom(File file);
    public abstract boolean extractCom(String dir);
    
    public DefaultTreeModel getTreeModel(){
        return _treeManipulator.getModel();
    }
    
    /* Make new folder */
    protected boolean _doMkDir(String rootName, String docName){
        File newDir = new File(rootName + File.separator + docName);
        /* If dirctory is exist or not */
        if (!newDir.exists()) {
            return newDir.mkdirs();
        }
        return true;
    }
    
    /* When find exist file, what should this application do */
    protected File _doReplaceJob(File newFile, int replaceResult) {
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
}
