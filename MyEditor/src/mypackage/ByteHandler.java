/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import  java.util.Scanner;

/**
 *
 * @author I-TECH
 */
public class ByteHandler {
    private static final int EMPTY = 0;
    private static final int FULL = MyEditor.getPageSize()/(MyEditor.getMaxwordSize() +4);
    public ByteHandler(){
        /*emprty constructor*/
    }
	
    public int createndx(String filename){
        ArrayList<IndexPairs> arr = new ArrayList<IndexPairs>();	// we use an arraylist to store the <string,int> pair values
        String line;
        String delims = "\\P{Alpha}+";
        int pages = 0;
        int cnt = 1;
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));	// create a reader to the file
            while ((line = br.readLine()) != null) {	// while there are lines to read
                // process the line.
                String[] result = line.split(delims);	// keep only words in line
                for (int x=0; x < result.length; x++) {	// for every word in the line
                    if(result[x].length() >= MyEditor.getMinwordSize()){	// if it's at least minimum size (default 5) 
                        if(result[x].length() > MyEditor.getMaxwordSize())	// if it's more that maximum size (default 20) trim
                            result[x] = result[x].substring(0,MyEditor.getMaxwordSize());	// trim extra chars
                        IndexPairs ndx = new IndexPairs(result[x], cnt);	// add word and line number to custom data class
                        arr.add(ndx);	// add to arraylist
                    }
                }
                cnt++;	// increment the line number
            }
            br.close();	// close the reader
            Collections.sort(arr);	// sort the array
            /*for(indice ndx : arr){
		System.out.println(ndx);
            }*/
			
            pages = writetondx(filename,arr);	//	write the array to the .ndx file
            
            if(pages == 0){
                System.out.println("Failed to create file: " + filename+".ndx");
                System.out.println("Nothing to be written, yet.");
                return 0;
            }
            System.out.println("OK. Data pages of size " + MyEditor.getPageSize() + " bytes: " + pages);	// print
        }catch(Exception ex){  
            ex.printStackTrace();
        }
        return pages;
    }
	
// creates the new .ndx file
    public int writetondx(String filename, ArrayList<IndexPairs> arr){
        String fname = filename+".ndx";	// the new file name is xxx.txt.ndx
        int capacity = FULL;	// the amount of pairs that our buffer can fit
        int pages = 0;	// used to count pages
        int words = 0;	// counts words
        int callno = 0;	// counts pairs
        
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(MyEditor.getPageSize());	// allocate page size (default 128)
		
        for(IndexPairs ndx : arr){	// while there are pairs in arraylist
            if(words < capacity){	// if buffer can fit pairs
                bb.put(ndx.get_s().getBytes(java.nio.charset.StandardCharsets.US_ASCII));	// write the string part
                bb.putInt(ndx.get_i());	// write the int part
                words++;	// increment word
                if(callno == arr.size()-1){	// write last page, same process as below
                    /* the idea is that if the buffer is not full but
                       there are some pairs written to it by the time we get to the last pair
                       we need to write the buffer to the file */
                    bb.putInt(0);	// this is the last byte written to the file
					// when we get this byte at the beginning, we know the rest of the page is empty
                    byte byteArray[] = bb.array();
                    try{
                        RandomAccessFile raf = new RandomAccessFile(fname, "rw");
                        raf.seek(MyEditor.getPageSize()*pages);
                        raf.write(byteArray);
                        raf.close();
                        pages++;	// turn page
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else{	// buffer can't fit any more pairs
                byte byteArray[] = bb.array();	// create a byte array
                try{
                    RandomAccessFile raf = new RandomAccessFile(fname, "rw");	// open the file
                    raf.seek(MyEditor.getPageSize()*pages);	// move to appropriate page (page size times page, the first time position is 0)
                    raf.write(byteArray);	// write the array
                    raf.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                bb = java.nio.ByteBuffer.allocate(MyEditor.getPageSize());	// allocate new buffer
                bb.put(ndx.get_s().getBytes(java.nio.charset.StandardCharsets.US_ASCII));	// write the current word that didn't fit
                bb.putInt(ndx.get_i());	// write it's line number
                words = 1;	// now we start at 1 because there's already a pair inside the buffer
                pages++;	// turn page
            }
            callno++;	// keep track of pairs
        }
        return pages;	// return number of pages of file
    }
	
// read .ndx file and print it's contents
// our implementation assumes knowledge of how many pages the file consists of
// therefore it is essential that c command is executed first
    public void readndx(String filename, int pages){
        String fname = filename + ".ndx";
         if(pages == 0){
            try{
                RandomAccessFile raf = new RandomAccessFile(fname, "r");
                pages = (int)raf.length()/MyEditor.getPageSize();
                raf.close();
                System.out.println(pages);
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
		
        int capacity = FULL;	// the amount of pairs that our buffer can fit
        int accesses = 0;

        try{
            RandomAccessFile raf = new RandomAccessFile(fname, "r");	// open the file
            while(pages > EMPTY){
                /*1. read page */
                byte[] buffer = new byte[MyEditor.getPageSize()];
                raf.seek(MyEditor.getPageSize()*accesses);
                accesses++; // turn page on next loop
                raf.read(buffer);
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);
		/*2. read the pairs written in the page */
                while(capacity > EMPTY){
                    byte byteArray[] = new byte[MyEditor.getMaxwordSize()];
                    bb.get(byteArray, 0, MyEditor.getMaxwordSize()); // fills byteArray with maxwordsz(default 20) bytes from ByteBuffer bb, starting from offset 0
                    String someString = new String(byteArray,java.nio.charset.StandardCharsets.US_ASCII);
                    if(someString.charAt(0) == 0)   // if there are no more pairs in page
                        break;
                    else{
                        someString = someString.trim();	//remove the spaces
                        int someInt = bb.getInt();
                        System.out.println(someString+" "+someInt);
                        capacity--;
                    }
                }
                capacity = FULL;
                pages--;
            }
            raf.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
	
    public void serials(String filename,int pages){
        String fname = filename + ".ndx";
        if(pages == 0){
            try{
                RandomAccessFile raf = new RandomAccessFile(fname, "r");
                pages = (int)raf.length()/MyEditor.getPageSize();
                raf.close();
                System.out.println(pages);
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        Scanner in = new Scanner(System.in);
        System.out.println("Type word for search:");
        String word = in.nextLine();
		
        int capacity = FULL;	// the amount of pairs that our buffer can fit
        int found = -1;
        int finished = -1;
        int accesses = 0;
        int cnt = 0;
		
        int[] linesfound = new int[100000];	// needs very large index
		
        try{
            RandomAccessFile raf = new RandomAccessFile(fname, "r");	// open the file
            while(pages > EMPTY && finished != 1){
                /*1. read page */
                byte[] buffer = new byte[MyEditor.getPageSize()];
                raf.seek(MyEditor.getPageSize()*accesses);
                raf.read(buffer);
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);
                /*2. read the pairs written in the page */
                while(capacity > 0){
                    byte byteArray[] = new byte[MyEditor.getMaxwordSize()];
                    bb.get(byteArray, 0, MyEditor.getMaxwordSize()); // fills byteArray with maxwordsz(default 20) bytes from ByteBuffer bb, starting from offset 0
                    String someString = new String(byteArray,java.nio.charset.StandardCharsets.US_ASCII);
                    if(someString.charAt(0) == 0){	// if there are no more pairs in page
                        finished = 1;
                        break;
                    }
                    else{
                        someString = someString.trim();	//remove the spaces
                        int someInt = bb.getInt();
                        if(someString.equals(word)){
                            linesfound[cnt] = someInt;
                            found = 1;
                            cnt++;
                        }
                        if(someString.compareTo(word)>0){
                            finished = 1;
                            break;
                        }
                        capacity--;
                    }
                }
                capacity = FULL;
                pages--;
                accesses++; // turn page on next loop
            }
            raf.close();
            if(found == -1)
                System.out.print("''"+word+"'' was not found.");
            else
                System.out.print("''"+word+"'' is on lines ");
            
            int i = 0;
            while(linesfound[i] > EMPTY){
                System.out.print(linesfound[i]);
                if(linesfound[i+1] != EMPTY)
                    System.out.print(",");
                i++;
            }
            System.out.println();
            System.out.println("disk accesses: "+ accesses);
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
	
    public void binarys(String filename,int pages){
        String fname = filename + ".ndx";
         if(pages == 0){
            try{
                RandomAccessFile raf = new RandomAccessFile(fname, "r");
                pages = (int)raf.length()/MyEditor.getPageSize();
                raf.close();
                System.out.println(pages);
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Type word for search:");
        String word = in.nextLine();
		
        int capacity = FULL;	// the amount of pairs that our buffer can fit
        int found = -1;
        int accesses = 0;
        int plusAccesses = 0;
        int cnt = 0;
		
        int[] linesfound = new int[100000];	// needs very large index
		
        try{
            RandomAccessFile raf = new RandomAccessFile(fname, "r");	// open the file
            int bottom = 0;
            int top = pages;
            int middle;
            while(bottom <= top){
                byte[] buffer = new byte[MyEditor.getPageSize()];
                
                middle = (bottom+top)/2;
                //System.out.println("page"+middle);
                raf.seek(middle*MyEditor.getPageSize()); // jump to this page in the file
                raf.read(buffer);
                
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);
                while(capacity > EMPTY){
                    //System.out.println("Capacity: "+capacity);
                    byte byteArray[] = new byte[MyEditor.getMaxwordSize()];
                    bb.get(byteArray, 0, MyEditor.getMaxwordSize()); // fills byteArray with maxwordsz(default 20) bytes from ByteBuffer bb, starting from offset 0
                    String someString = new String(byteArray,java.nio.charset.StandardCharsets.US_ASCII);
                    
                    if(someString.charAt(0) == 0){	// if there are no more pairs in file
                        top = -1;
                        break;
                    }
                    else{
                        someString = someString.trim();	//remove the spaces
                        int someInt = bb.getInt();
                        if(someString.compareTo(word)>0){
                            top = middle - 1;
                            break;
                        }
                        if(someString.compareTo(word) == 0){
                            linesfound[cnt] = someInt;
                            found = 1;
                            cnt++;
                            //System.out.println("Here!");
                            if(capacity == 1){
                                if(middle-1 >= 0)
                                    plusAccesses = searchNear(fname,word,middle-1,cnt,linesfound,-1);
                                accesses = accesses + plusAccesses;
                                int j = 0;
                                while(linesfound[j] > EMPTY){
                                    j++;
                                }
                                cnt = j;
                                if(middle+1 <= pages)
                                    plusAccesses = searchNear(fname,word,middle+1,cnt,linesfound,1);
                                accesses = accesses + plusAccesses;
                                top = -1;
                                break;
                            }
                            //top = -1;
                            //break;
                        }
                        if(someString.compareTo(word) < 0){
                            if(capacity == 1){
                                bottom = middle + 1;
                                break;
                            }
                        }
                        capacity--;
                    }
                }
                capacity = FULL;
                accesses++; // turn page on next loop
            }
            raf.close();
            if(found == -1)
                System.out.print("''"+word+"'' was not found.");
            else
                System.out.print("''"+word+"'' is on lines ");
            int i = 0;
            while(linesfound[i] > EMPTY){
                System.out.print(linesfound[i]);
                if(linesfound[i+1] != EMPTY)
                    System.out.print(",");
                i++;
            }
            System.out.println();
            System.out.println("disk accesses: "+ accesses);
			
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public int searchNear(String filename, String word, int start, int pos, int[] linesfound, int dir){
        int accesses = 0;
        int finished = -1;
        int looknomore = 0;
        int capacity = FULL;
        int someInt;
        String someString;
        
        try{
            RandomAccessFile raf = new RandomAccessFile(filename, "r");	// open the file
            while(finished != 1){
                //System.out.println("inisidepage"+start);
                /*1. read page */
                byte[] buffer = new byte[MyEditor.getPageSize()];
                raf.seek(MyEditor.getPageSize()*start);
                raf.read(buffer);
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);
                byte byteArray[] = new byte[MyEditor.getMaxwordSize()];;
                /*2. read the pairs written in the page */
                while(capacity > EMPTY){
                    //System.out.println("inisidecapacity: "+capacity);
                    bb.get(byteArray, 0, MyEditor.getMaxwordSize()); // fills byteArray with maxwordsz(default 20) bytes from ByteBuffer bb, starting from offset 4
                    someString = new String(byteArray,java.nio.charset.StandardCharsets.US_ASCII);

                    if(someString.charAt(0) == 0){	// if there are no more pairs in page
                        finished = 1;
                        break;
                    }
                    else{
                        someString = someString.trim();	//remove the spaces
                        someInt = bb.getInt();
                        if(someString.equals(word)){
                            //System.out.println("Here inside!");
                            linesfound[pos] = someInt;
                            pos++;
                        }
                        if(capacity == 1 && someString.compareTo(word)<0){
                            looknomore = 1;
                        }
                        if(dir == 1 && someString.compareTo(word)>0){
                            //System.out.println(dir+" "+someString);
                            finished = 1;
                            break;
                        }
                        if(dir == -1 && looknomore == 1){
                            finished = 1;
                            break;
                        }
                        capacity--;
                    }
                }
                capacity = FULL;
                if(dir == 1)
                    start++;
                else
                    start--;
                accesses++; // turn page on next loop
            }
            raf.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return accesses;
    }
}