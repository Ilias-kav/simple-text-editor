/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

import java.io.*; 
import  java.util.Scanner; 
/**
 *
 * @author I-TECH
 */
public class TextHandler {
    DLL dll = new DLL();	// create a doubly linked list instance
    ByteHandler bhandler = new ByteHandler();	// create am instance of ByteHandler class
						// will be used for .ndx file operations
    Scanner in = new Scanner(System.in);
	
    private static int pages = 0;
    private static int lineno = 1;	// the starting line number
    private static int toggle = 1;	// a switch used to toggle line numbers off/on
    private String fname;		// used for the .ndx file naming
	
    public TextHandler(){
    /*emprty constructor*/
    }
   
    public void openFile(String filename){	// opens file and fills the list with its lines or creates an empty file
        try{
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File: " + filename + " created successfully.");
            } else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filename));	// create a reader to the file
                    fname = filename;
                    String line;
                    while ((line = br.readLine()) != null) {	// while there are lines to read
                        // process the line.
                        dll.append(line.toCharArray(), lineno);	// add the line and the line number to the list
                        lineno++;	// increment the line number
                    }
                    br.close();	// close the reader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){  
            e.printStackTrace();
        } 
    }
    
    public int exec(char opt){
        switch(opt){
            case '^':	//move to the top of the list
                dll.movetohead();
		System.out.println("OK");
                break;
            case '$':	//move to the bottom of the list
                dll.movetotail();
		System.out.println("OK");
                break;
            case '-':	//move up a line
                dll.moveup();
                break;
            case '+':	//move down a line
                dll.movedown();
                break;
            case 'a':	//add a new line after current line
		System.out.println("Type text for new line:");
		dll.InsertAfter(in.nextLine().toCharArray());
		break;
            case 't':	//add a new line before current line
                System.out.println("Type text for new line:");
		dll.InsertBefore(in.nextLine().toCharArray());
		break;
            case 'd':	//delete current node
                dll.deletenode();
				break;
            case 'l':	//print all lines
                dll.printlist();
		break;
            case 'n':	//toggle line number display on/off
                toggle();
		break;
            case 'p':	//print current line
                dll.printnode();
		break;
            case 'q':	//quit without save
                return 0;
            case 'w':	//write to file
                writetofile();
		System.out.println("OK");
		break;
            case 'x':	//quit with save
		writetofile();
                System.out.println("OK");
		return 0;
            case '=':	//print current line number
                printlinenumber();
		break;
            case '#':	//print number of lines and characters
                printstats();
		break;
            case 'c':	//create the .ndx file
                pages = bhandler.createndx(fname);
		break;
            case 'v':	//read and print the .ndx file
		bhandler.readndx(fname,pages);
                break;
            case 's':	//search for word
                bhandler.serials(fname,pages);
		break;
            case 'b':
                bhandler.binarys(fname,pages);
		break;
            default:
                System.out.println("Not a valid command.");
		break;
        }
	return 1;
    }
	
// writes the list to file
    private void writetofile() {
        try (FileWriter writer = new FileWriter(fname);
                BufferedWriter bw = new BufferedWriter(writer)) {	// open a new file writer
            char[] content = new char[MyEditor.getMaxchars()];	// create a char array with size maxchars(default 80)
            dll.movetohead();	//	move to the head of the list
            while (dll.get_list_index().get_next() != null) {	// while list is not empty 
                content = dll.get_list_index().getline();	// copy line to char array
                bw.write(content);	// write array to file
                bw.newLine();	// go to new line
                dll.set_list_index(dll.get_list_index().get_next());	//move to next node
            }
            /* do the same as above for the last node of the list */
            content = dll.get_list_index().getline();
            bw.write(content);
            bw.newLine();
            bw.close();	// close the writer
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
	
// prints the current line number
    private void printlinenumber(){
        dll.movetotail();	// move to last node
	System.out.println(dll.get_list_index().getnumber() +" lines");	// print the line number of the last node
	dll.movetohead();	// back to head
    }
	
// prints number of lines and characters
    private void printstats(){
        String line;
	int count = 0;	// used to count characters
	dll.movetohead();	// move to head of list
	while(dll.get_list_index().get_next() != null){	// while list is not empty
            line = String.valueOf(dll.get_list_index().getline());	// get the line from the list so we know its length
            //Counts each character except space    
            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) != ' ')
                    count++;    // count characters
            }
            dll.set_list_index(dll.get_list_index().get_next());	// move to next node
        }
	/* do the same as above for last node */
        line = String.valueOf(dll.get_list_index().getline());
	//Counts each character except space    
	for(int i = 0; i < line.length(); i++) {    
            if(line.charAt(i) != ' ')
                count++;    
        }
	System.out.println(dll.get_list_index().getnumber() +" lines, " + count +" characters"); // print
    }
	
//toggles line number display on/off
    private void toggle(){
        if(toggle == 1){ // if on then off
            toggle = 0;
            System.out.println("TOGGLE OFF");
        }
	else{	// else on
            toggle = 1;	
            System.out.println("TOGGLE ON");
        }
    }
	
    public static int getnumber(){
        return lineno;
    }
	
    public static void setnumber(){
        lineno++;
    }
    
    public static int getToggle(){
        return toggle;
    }
}
