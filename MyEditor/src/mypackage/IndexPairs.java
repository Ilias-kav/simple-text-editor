/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

/**
 *
 * @author I-TECH
 */
public class IndexPairs implements Comparable<IndexPairs> {	// class used for custom data type <string,int>
    private String s;	// the word
    private int i;	// the line number where the word is found at
	
    public IndexPairs(String s, int i){	// simple constructor
        this.s = s;
        this.i = i;
    }
	
	public String get_s(){	// getter for the word
            int length = this.s.length();	// we do some extra work before we return the word
            String str = this.s;	// we pad the word with spaces until it's max word characters long
            while(length < MyEditor.getMaxwordSize()){
                str = str+" ";
                length++;
            }
            return str;	// we return the new padded word
	}
	
	public int get_i(){	// getter for the line number
            return this.i;
	}
	
    @Override
    public int compareTo(IndexPairs comparendx) {	// our custom implementation of compareTo, used to order the list
        return this.s.compareTo(comparendx.s);
    }
	
    @Override
    public String toString() {	// our custom implementation of toString, used to print the pairs
        return this.s + " " + this.i;
    }

}