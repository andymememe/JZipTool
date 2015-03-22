/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jziptool.treer;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author andymememe
 */
public class TreeManipulator {
    private final DefaultTreeModel _treeModel;
    private final DefaultMutableTreeNode EMPTY = new DefaultMutableTreeNode("[empty]");
    private DefaultMutableTreeNode _root;
    
    public TreeManipulator(DefaultTreeModel dtm){
        _treeModel = dtm;
    }
    
    /* Generate root */
    public void generateRoot(String filename){
        _treeModel.setRoot(new DefaultMutableTreeNode(filename));
        _root = (DefaultMutableTreeNode) _treeModel.getRoot();
    }
    
    /* Add node */
    public void addEntryNode(String newNodeName, String parent, int level, boolean isDir){
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeName);
        newNode.add((DefaultMutableTreeNode) EMPTY.clone());
        newNode.setAllowsChildren(isDir);
        if(parent == null && level == 1){
            _root.add(newNode);
            _treeModel.reload();
        }
        else{
            Enumeration findChild = _root.breadthFirstEnumeration();
            while(findChild.hasMoreElements()){
                DefaultMutableTreeNode nextElement = (DefaultMutableTreeNode) findChild.nextElement();
                String eleName = (String) nextElement.getUserObject();
                int eleLevel = nextElement.getLevel();
                if(eleName.equals(parent) && eleLevel == level - 1){
                    String firstChildName = ((String)((DefaultMutableTreeNode)nextElement.getFirstChild()).getUserObject());
                    if(firstChildName.equals("[empty]")){
                        nextElement.remove(0);
                    }
                    nextElement.add(newNode);
                }
            }
        }
    }
    
    /* Get model */
    public DefaultTreeModel getModel(){
        return _treeModel;
    }
}
