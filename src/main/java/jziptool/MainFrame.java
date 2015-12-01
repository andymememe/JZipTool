/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jziptool;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import jziptool.treer.TreeManipulator;
import jziptool.zipper.*;

/**
 *
 * @author andymememe
 */
public class MainFrame extends javax.swing.JFrame {
    private ComManipulator _comManipulator;
    
    /**
     * Creates new form MainFrame
     * @param args
     */
    public MainFrame(String[] args) {
        initComponents();
        
        fileTree.setModel(null);
        
        /* Set fileChooser's filter */
        fileChooser.setFileFilter(new FileFilter() {
            private final FileNameExtensionFilter filter =new FileNameExtensionFilter("壓縮檔","zip", "tar");
            
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return filter.accept(f);
                }
            }

            @Override
            public String getDescription() {
                return filter.getDescription();
            }
        });
        
        if(args.length > 0){
            _comFileOpen(new File(args[0]));
        }
    }

    /* Open zip file */
    private void _comFileOpen(File file){
        /* Check is folder exist or not */
        if(!file.exists()){
            JOptionPane.showMessageDialog(null, "沒有「" + file.getName() + "」這個檔案喔!", "找不到檔案", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] fileName;
        fileName = fileChooser.getSelectedFile().getName().split("\\.");
        switch(fileName[fileName.length - 1]){
            case "tar":
                _comManipulator = new TarManipulator(new TreeManipulator(new DefaultTreeModel(null)));
                break;

            case "zip":
                _comManipulator = new ZipManipulator(new TreeManipulator(new DefaultTreeModel(null)));
                break;
        }
        _comManipulator.openCom(file);
        
        /* Set fileTree's model */
        fileTree.setModel(_comManipulator.getTreeModel());
        extractBtn.setEnabled(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        dirChooser = new javax.swing.JFileChooser();
        openBtn = new javax.swing.JButton();
        extractBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileTree = new javax.swing.JTree();

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new java.io.File("/home/andymememe/~"));
        fileChooser.setDialogTitle("開啟壓縮檔");

        dirChooser.setAcceptAllFileFilterUsed(false);
        dirChooser.setCurrentDirectory(new java.io.File("/home/andymememe/~"));
        dirChooser.setDialogTitle("解壓縮到...");
        dirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JZipTool");
        setResizable(false);

        openBtn.setText("開啟...");
        openBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openBtnMouseClicked(evt);
            }
        });

        extractBtn.setText("解壓縮");
        extractBtn.setEnabled(false);
        extractBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                extractBtnMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(fileTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(openBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extractBtn)
                        .addGap(0, 555, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openBtn)
                    .addComponent(extractBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openBtnMouseClicked
        // TODO add your handling code here:
        fileChooser.showOpenDialog(null);
        if(fileChooser.getSelectedFile() != null){
            _comFileOpen(fileChooser.getSelectedFile());
        }
    }//GEN-LAST:event_openBtnMouseClicked

    private void extractBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_extractBtnMouseClicked
        // TODO add your handling code here:
        dirChooser.showOpenDialog(null);
        if(dirChooser.getSelectedFile() != null){
            if(_comManipulator.extractCom(dirChooser.getSelectedFile().getAbsolutePath())){
                JOptionPane.showMessageDialog(null, "解壓縮成功囉！", "解壓縮成功", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
               JOptionPane.showMessageDialog(null, "嗚~解壓縮失敗了！", "解壓縮失敗", JOptionPane.ERROR_MESSAGE); 
            }
        }
    }//GEN-LAST:event_extractBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame(args).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser dirChooser;
    private javax.swing.JButton extractBtn;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JTree fileTree;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton openBtn;
    // End of variables declaration//GEN-END:variables
}