/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        // Your code goes here
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node head = new Node(new CharData(chr));
        head.next = this.first;
        this.first = head;
        this.size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if(this.size == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append("( ");
        Node current = this.first;

        while(current != null){
            builder.append(current.toString() + " ");
            current = current.next;
        }

        builder.append(")");

        return builder.toString();


    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int index = 0;
        Node current = this.first;
        if(current == null) return -1;

        do{
            if(current.cp.chr != chr) {
                index++;
                current = current.next;
                continue;
            }
            return index;

        }while(current != null);

        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        Node current = this.first;

        if(current == null) {
            this.addFirst(chr);
            return;
        };
        
        if(current.cp.chr == chr){
            current.cp.count++;
            return;
        }
        
        while(current.next != null){
            if(current.next.cp.chr != chr){
                current = current.next;
                continue;
            }
            current.next.cp.count++;
            return;
        }
        
        current.next = new Node(new CharData(chr));
        this.size++;
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node current = this.first;
        if(current == null) return false;

        // Removing first element:
        if(current.cp.chr == chr){
            this.first = current.next;
            this.size--;
            return true;
        }
        // Removing elements after:
        while(current.next != null){
            if(current.next.cp.chr != chr){
                current = current.next;
            }
            current.next = current.next.next;
            this.size--;
            return true;
        }

        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        // 0 1  2
        if(index > this.size || index < 0 || this.size == 0){
            throw new IndexOutOfBoundsException();
        }
        int i = 0;
        Node current = this.first;
        do{
            if(i == index){
                return current.cp;
            }
            current = current.next;
            i++;
        } while(current != null);

        return null;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}