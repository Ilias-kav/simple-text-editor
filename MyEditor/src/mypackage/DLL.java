/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

// Class for Doubly Linked List 
public class DLL { 
    private Node head;	// head of list
    private Node index;	// a node that acts like a cursor to move accross lines
	
        /* Doubly Linked list Node*/
    class Node { 
        private char[] line = new char[MyEditor.getMaxchars()];
        private int lineno;
        private Node prev; 
        private Node next; 
  
// Constructor to create a new node 
// next and prev is by default initialized as null 
        Node(char[] line,int lineno) {
            this.line = line;
            this.lineno = lineno;
        }
// setters and getters
        public char[] getline(){return this.line;}
	public int getnumber(){return this.lineno;}
	public void setnumber(int lineno){this.lineno = lineno;}
	public Node get_prev(){return this.prev;}
	public Node get_next(){return this.next;}
    } 
  
// Adding a node at the front of the list 
    public void push(char[] new_line, int new_lineno) 
    { 
        /* 1. allocate node  
        * 2. put in the data */
        Node new_Node = new Node(new_line, new_lineno); 
  
        /* 3. Make next of new node as head and previous as NULL */
        new_Node.next = head; 
        new_Node.prev = null; 
  
        /* 4. change prev of head node to new node */
        if (head != null) 
            head.prev = new_Node; 
  
        /* 5. move the head to point to the new node */
        head = new_Node;
        index = head;
    } 
  
    /* Given a node as prev_node, insert a new node after the given node */
    public void InsertAfter(char[] new_line) 
    { 
        Node prev_Node = index;
        /* 1. check if the given prev_node is NULL */
        if (prev_Node == null) { 
            System.out.println("There are currently no lines.");
            System.out.println("Your line will be placed at the top of the file.");
            append(new_line, TextHandler.getnumber());
            TextHandler.setnumber(); 
            return; 
        } 
  
        /* 2. allocate node  
         * 3. put in the data */
        Node new_node = new Node(new_line, prev_Node.lineno); 
  
        /* 4. Make next of new node as next of prev_node */
        new_node.next = prev_Node.next; 
  
        /* 5. Make the next of prev_node as new_node */
        prev_Node.next = new_node; 
  
        /* 6. Make prev_node as previous of new_node */
        new_node.prev = prev_Node; 
  
        /* 7. Change previous of new_node's next node */
        if (new_node.next != null) 
            new_node.next.prev = new_node;
        renumber();	//adjust the new numbers
	index = new_node;
    } 
  
// Added by the author based on InsertAfter implementation
    /* Given a node as next_node, insert a new node before the given node */
    public void InsertBefore(char[] new_line) 
    { 
        Node next_Node = index;
        /* 1. check if the given next_node is NULL */
        if (next_Node == null) { 
            System.out.println("There are currently no lines.");
            System.out.println("Your line will be placed at the top of the file.");
            append(new_line, TextHandler.getnumber());
            TextHandler.setnumber();
            return; 
        } 
  
        /* 2. allocate node  
         * 3. put in the data */
        Node new_node = new Node(new_line, next_Node.lineno); 
  
        /* 4. Make next of new node as next_node */
        new_node.next = next_Node; 
  
        /* 5. Make the prev of new_node as previous of next_node */
        new_node.prev = next_Node.prev; 
  
        /* 6. Make previous of next_node as new_node */
        next_Node.prev = new_node; 
  
        /* 7. Change next of new_node's previous node */
        if (new_node.prev != null)
            new_node.prev.next = new_node;
        index = new_node;
        if(new_node.getnumber() == 1) // if the node was inserted before the head, it should be our new head
            head = index;
        renumber();	//adjust the new numbers
    }
  
// Add a node at the end of the list 
    void append(char[] new_line, int new_lineno) 
    { 
        /* 1. allocate node  
         * 2. put in the data */
        Node new_node = new Node(new_line, new_lineno); 
  
        Node last = head; /* used in step 5*/
  
        /* 3. This new node is going to be the last node, so 
        * make next of it as NULL*/
        new_node.next = null; 
  
        /* 4. If the Linked List is empty, then make the new 
        * node as head */
        if (head == null) { 
            new_node.prev = null; 
            head = new_node;
            index = new_node;
            return; 
        } 
  
        /* 5. Else traverse till the last node */
        while (last.next != null) 
            last = last.next; 
  
        /* 6. Change the next of last node */
        last.next = new_node; 
  
        /* 7. Make last node as previous of new node */
        new_node.prev = last; 
	index = new_node;
    } 
  
// This function prints contents of linked list starting from the head 
    public void printlist() 
    { 
        Node node = head;

        if(head == null){
            System.out.println("Nothing to print.");
            return;
        }
        //System.out.println("Traversal in forward Direction"); 
        while (node != null) { 
            if(TextHandler.getToggle() == 1)
                System.out.print(node.getnumber() + ") ");
            
            System.out.println(node.getline());
            node = node.next;
        } 
    } 
    
// Added by the author
// This function moves moves the index to the first line of the .txt
    public void movetohead(){
        index = head;
    }
    
// Added by the author
// This function moves moves the index to the last line of the .txt
    public void movetotail(){
        while(index.next != null)
            index = index.next;
    }
    
// Added by the author
// This function moves moves the index one line down the .txt
    public void movedown(){
        if(index.next != null)
            index = index.next;
    }
	
// Added by the author
// This function moves moves the index one line up the .txt
    public void moveup(){
        if(index.prev != null)
            index = index.prev;
    }
	
// Added by the author
// This function reverses the order of the nodes
    private void reverse(){	//currently not in use by application
        Node temp = null; 
        Node current = head; 
  
        /* swap next and prev for all nodes of  
         doubly linked list */
        while (current != null) { 
            temp = current.prev; 
            current.prev = current.next; 
            current.next = temp; 
            current = current.prev; 
        } 
  
        /* Before changing head, check for the cases like empty  
         list and list with only one node */
        if (temp != null) { 
            head = temp.prev; 
        }
    }
	
// Added by the author
// This function prints the current node
    public void printnode(){
        Node node = index;
        
        if(node != null)
            System.out.println(node.getline());
        else
            System.out.println("Nothing to print.");
    }
	
// Added by the author
// This function adjusts the numbers of the lines in case of insert/delete
    public void renumber(){
        Node temp = index;
        int index_no = temp.getnumber();

	while(temp.next != null){
            temp = temp.next;
            temp.setnumber(index_no + 1);
            index_no++;
        }
        TextHandler.setnumber();
    }
	
// Added by the author
// This function deletes the current node
    public void deletenode(){
        Node del = index;
// Base case
        if (head == null || del == null) { 
            System.out.println("Nothing to delete.");
            return; 
        } 
  
// If node to be deleted is head node 
        if (head == del)
            head = del.next; 
  
// Change next only if node to be deleted 
// is NOT the last node 
        if (del.next != null)
            del.next.prev = del.prev;

// Change prev only if node to be deleted 
// is NOT the first node 
        if (del.prev != null)
            del.prev.next = del.next;
        
        if (del.prev != null)
            index = del.prev;
        else
            index = del.next;
        
        renumber();
        System.out.println("OK.");
    }
	
// setters and getters
    public Node get_list_head(){
        return head;
    }
	
    public Node get_list_index(){
        return index;
    }
	
    public void set_list_index(Node index){
        this.index = index;
    }
}
  
// This code is contributed by Sumit Ghosh
