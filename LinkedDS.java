/**
 *CS 0445 Fall 17
 *@author Jonathan Chang
 *Assignment 2
 * Khattab
 */

import java.util.*;
public class LinkedDS<T> implements PrimQ<T>,Reorder
{
	/**
	 * allowed instance variables
	 */
	protected Node firstNode;
	protected int numOfEntries;

	/**
	 * gives a blank LinkedDS
	 */
	public LinkedDS(){
		firstNode = null;
		numOfEntries = 0;
	}

	/**
	 * gives an exact copy of a list
	 * @param oldList
	 */
	public LinkedDS(LinkedDS<T> oldList){
		Node temp = oldList.firstNode;
		while(temp != null)			//iterate through the list, adding each value
		{
			addItem( temp.data );
			temp = temp.next;
		}
	}

	/**
	 * give a string to return by appending StringBuilder per Node data
	 * @return
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		Node currentNode=firstNode;
		while(currentNode!=null){
			sb.append(currentNode.data+" ");
			currentNode=currentNode.next;
		}
		return sb.toString();
	}
	// Add a new Object to the PrimQ<T> in the next available location.  If
	// all goes well, return true.

	/**
	 * either add as a firstNode (empty list) or add to the end
	 * @param newEntry
	 * @return
	 */
	public boolean addItem(T newEntry){
		Node newNode = new Node(newEntry);
		if(firstNode==null)
			firstNode=newNode;            //add if no entries yet
		else{
			Node temp = firstNode;
			while(temp.next!=null)     //search for end of list, add to end
				temp=temp.next;
			temp.next=newNode;
		}
		numOfEntries++;
		return true;
	}
	// Remove and return the "oldest" item in the PrimQ.  If the PrimQ
	// is empty, return null.

	/**
	 * delete the firstNode, and make the original second node the firstNode
	 * @return
	 */
	public T removeItem(){
		if(firstNode==null)
			return null;
		T result=firstNode.data;  //save value to return
		Node temp=firstNode;     //track from first
		firstNode=temp.next;     //set first now as the "Second" value
		while(temp.next!=null)
			temp=temp.next;
		temp=null;
		numOfEntries--;
		return result;
	}
	// Return true if the PrimQ is empty, and false otherwise

	public boolean empty(){
		return numOfEntries == 0;
	}
	// Return the number of items currently in the PrimQ

	public int size(){
		return numOfEntries;
	}
	// Reset the PrimQ to empty status by reinitializing the variables
	// appropriately

	public void clear(){
		while (!empty())
			removeItem();
	}
	// Logically reverse the data in the Reorder object so that the item
	// that was logically first will now be logically last and vice
	// versa.  The physical implementation of this can be done in
	// many different ways, depending upon how you actually implemented
	// your physical LinkedDS<T> class

	/**
	 * reverses all data entries
	 *
	 * using 3 nodes, swap until reverse of original
	 */
	public void reverse(){
		Node before = null;
		Node cur = firstNode;
		while (cur != null) {
			Node next = cur.next; //next list value stored
			cur.next = before;		//SWAP node places
			before = cur;			//before node moved down list by 1
			cur = next;			//current node moved down list by 1
		}
		firstNode = before;		//before is latest item when cur is null(end of list)
	}
	// Remove the logical last item of the DS and put it at the
	// front.  As with reverse(), this can be done physically in
	// different ways depending on the underlying implementation.

	public void shiftRight(){
		Node prev = null;
		Node cur = firstNode;
		while (cur.next != null)  //prev is second to last node, cur is last node
		{
			prev = cur;		//save second to last value
			cur = cur.next;	//save last value
		}
		prev.next = null;		//after end of list is null
		cur.next = firstNode;  //next of last node is the firstNode
		firstNode = cur;       //firstNode is the last node
	}
	// Remove the logical first item of the DS and put it at the
	// end.  As above, this can be done in different ways.
	public void shiftLeft(){
		Node temp=firstNode;	//save value
		while(temp.next!=null) //get to end of list
			temp=temp.next;
		temp=firstNode;			//put at end
	}
	// Reorganize the items in the object in a pseudo-random way.  The exact
	// way is up to you but it should utilize a Random object (see Random in
	// the Java API).  Thus, after this operation the "oldest" item in the
	// DS could be arbitrary.

	/**
	 * take a shuffle by random number
	 */
	public void shuffle(){
		int mix = (int) ((Math.random()*numOfEntries)); //random number to shuffle amount
		Node temp = firstNode;
		for(int i = 0;i<mix;i++ ) {
			temp = temp.next;
		}
		Node tempTwo = firstNode;	//store random firstNode value
		if(temp.next != null)
			firstNode = temp.next; //random firstNode
		temp.next=firstNode.next;	//next temp value
		firstNode.next=tempTwo;		//next value
	}
	// Shift the contents of the DS num places to the left (assume the beginning
	// is the leftmost node), removing the leftmost num nodes.  For example, if
	// a list has 8 nodes in it (numbered from 1 to 8), a leftShift of 3 would
	// shift out nodes 1, 2 and 3 and the old node 4 would now be node 1.
	// If num <= 0 leftShift should do nothing and if num >= the length of the
	// list, the result should be an empty list.

	/**
	 * simply remove first item, and then shiftLeft using written methods
	 * @param num
	 */
	public void leftShift(int num){
		for(int i=0;i<num;i++)
		{
			removeItem();
			shiftLeft();
		}
	}
	// Same idea as leftShift above, but in the opposite direction.  For example,
	// if a list has 8 nodes in it (numbered from 1 to 8) a rightShift of 3 would
	// shift out nodes 8, 7 and 6 and the old node 5 would now be the last node
	// in the list.  If num <= 0 rightShift should do nothing and if num >= the
	// length of the list, the result should be an empty list.

	/**
	 * simply shiftRight, and then remove item using methods written
	 * @param num
	 */
	public void rightShift(int num){
		for(int i=0;i<num;i++){
			shiftRight();
			removeItem();
		}
	}
	// In this method you will still shift the contents of the list num places to
	// the left, but rather than removing nodes from the list you will simply change
	// their ordering in a cyclic way.  For example, if a list has 8 nodes in it
	// numbered from 1 to 8), a leftRotate of 3 would shift nodes 1, 2 and 3 to the
	// end of the list, so that the old node 4 would now be node 1, and the old nodes
	// 1, 2 and 3 would now be nodes 6, 7 and 8 (in that order).  The rotation should
	// work modulo the length of the list, so, for example, if the list is length 8 then
	// a leftRotate of 10 should be equivalent to a leftRotate of 2.  If num < 0, the
	// rotation should still be done but it will in fact be a right rotation rather than
	// a left rotation.
	public void leftRotate(int num){
		Node temp  = firstNode;
		while(num>numOfEntries)
			num=num%numOfEntries; //take modulus if larger than entries
		if(num<0){					//rotate opposite way if negative num
			num=num*-1;
			rightRotate(num);
		}
		else{						//shiftRight the reversed list, then reverse to original
			this.reverse();
			for(int i=0;i<num;i++)
				shiftRight();
			this.reverse();
		}
	}

	// Same idea as leftRotate above, but in the opposite direction.  For example, if a list
	// has 8 nodes in it (numbered from 1 to 8), a rightRotate of 3 would shift nodes 8, 7 and
	// 6 to the beginning of the list, so that the old node 8 would now be node 3, the old node
	// 7 would now be node 2 and the old node 6 would now be node 1.  The behavior for num > the
	// length of the list and for num < 0 should be analogous to that described above for leftRotate.
	public void rightRotate(int num){
		while(num>numOfEntries)
			num=num%numOfEntries;	//take modulus if longer than list
		if(num<0){					//go opposite way if negative using leftRotate
			num=num*-1;
			leftRotate(num);
		}
		else{
			for(int i=0;i<num;i++)	//shift right for desired times
				shiftRight();
		}
	}

	protected class Node
	{
		protected T    data; // Entry in bag
		protected Node next; // Link to next node
		private Node(T dataPortion)
		{
			this(dataPortion, null);
		}
		private Node(T dataPortion, Node nextNode)
		{
			data = dataPortion;
			next = nextNode;
		}
	}
}