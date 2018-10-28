/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jziptool;

/**
 *
 * @author andymememe
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /* View */
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) {
            System.out.println("[執行] : JZipTool.jar [壓縮檔的位置（可省略）]");
        } else {
            MainFrame main = new MainFrame(args);
            main.setVisible(true);
        }
    }

}
