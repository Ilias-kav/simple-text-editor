/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;
import  java.util.Scanner;
/**
 *
 * @author I-TECH
 */
public class MyEditor {
    /** 
     *	Change the following parameters to fit your implementation
     */
    private static final int MAX_CHARS = 80;    // max characters per line
    private static final int MIN_WORD_SIZE = 5; // minimum size of word
    private static final int MAX_WORD_SIZE = 20;	// max size of word
    private static final int PAGE_SIZE = 128;		// page size
    /**
     * @param args the command line arguments
     */
    public MyEditor(){
    /*emprty constructor*/
    }
    public static void main(String[] args) {
        // TODO code application logic here
        TextHandler handler = new TextHandler(); //  create a handle that manipulates the .txt
        
        if(args[0] == null){
            System.out.println("Error! File name not specified.");
            return;
        }
    
        handler.openFile(args[0]);   //  open the file or create it
		
        int run = 1;	// used for main loop, changes to 0 when user chooses to exit
    
        char input;
		
        Scanner in = new Scanner(System.in);
        
        while(run == 1){
            System.out.print("CMD>  ");
            input = in.next().charAt(0); // read user option
            run = handler.exec(input);	 // text editor application logic
        }
    }
    
    public static int getMaxchars(){return MAX_CHARS;}
    public static int getMinwordSize(){return MIN_WORD_SIZE;}
    public static int getMaxwordSize(){return MAX_WORD_SIZE;}
    public static int getPageSize(){return PAGE_SIZE;}
}
