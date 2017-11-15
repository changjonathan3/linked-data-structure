/**
 *CS 0445 Fall 17
 *@author Jonathan Chang
 *Assignment 2
 * Khattab
 */

/** CS 0445 Fall 2017 (Adapted  from Dr. John Ramirez's assignment code)
 This is a partial implementation of the ReallyLongInt class.  You need to
 complete the implementations of the remaining methods.  Also, for this class
 to work, you must complete the implementation of the LinkedDS class.
 See additional comments below.
*/

public class ReallyLongInt 	extends LinkedDS<Integer> 
							implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables
	
	// Default constructor
	private ReallyLongInt()
	{
		super();
	}

	// Note that we are adding the digits here in the FRONT. This is more efficient
	// (no traversal is necessary) and results in the LEAST significant digit first
	// in the list.  It is assumed that String s is a valid representation of an
	// unsigned integer with no leading zeros.
	public ReallyLongInt(String s)
	{
		super();
		char c;
		int digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the addItem() method (from LinkedDS) does not need to traverse the list since
		// it is adding in position 0.  
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				this.addItem(new Integer(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
		/**
		 * ADDED to automatically reverse when created
		 */
		this.reverse();
	}

	// Simple call to super to copy the nodes from the argument ReallyLongInt
	// into a new one.
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}
	
	// Method to put digits of number into a String.  Since the numbers are
	// stored "backward" (least significant digit first) we first reverse the
	// number, then traverse it to add the digits to a StringBuilder, then
	// reverse it again.  This seems like a lot of work, but given the
	// limitations of the super classes it is what we must do.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (numOfEntries > 0)
		{
			this.reverse();
			for (Node curr = firstNode; curr != null; curr = curr.next)
			{
				sb.append(curr.data);
			}
			this.reverse();
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		Node one = this.firstNode;
		Node two = rightOp.firstNode;
		ReallyLongInt total = new ReallyLongInt();
		boolean rem = false;
		boolean oneDone = false;
		boolean twoDone = false;
		int carry=0;
		int units=0;
		int sum=0;
		/**
		 * Run through entirety of the long int, whichever is longer
		 *
		 * add each pair to get a sum
		 * special cases if there is a remainder:
		 * (rem true)
		 *
		 * oneDone and twoDone if one int is shorter, don't continue comparing
		 */
		for(int i=0;i<this.size()||i<rightOp.size();i++){
			if(oneDone)
				sum=two.data;
			else if(twoDone)
				sum=one.data;
			else
				sum=one.data+two.data;
			if(rem){
				sum=sum+carry;
				rem=false;
			}
			if(sum>9){
				carry=sum/10;		//gets the # to add to the next sum
				units=sum%10;		//gets units digit of the sum
				rem = true;
			}
			total.addItem(units);
			if(one.next==null&&two.next==null){	//if nothing more to add
				total.addItem(carry);
				break;
			}
			if(one.next==null)
				oneDone=true;
			else
				one=one.next;
			if(two.next==null)
				twoDone=true;
			else
				two=two.next;
		}
		return total;
	}


	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{
		/**
		 * don't allow for a negative difference
		 */
		if (this.compareTo(rightOp) < 0) {
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		Node one=this.firstNode;
		Node two=rightOp.firstNode;
		ReallyLongInt total=new ReallyLongInt();
		boolean twoDone = false;
		int dif=0;
		int oneMinus=0;						//used as a "borrowed from" value, so original minus 1
		boolean borr=false;
		/**
		 * run through length of the long int
		 *
		 * each time, find the difference
		 * special cases if there was a borrowed value from the next node:
		 * (borr true)
		 *
		 * twoDone is if the second int is shorter, so don't need to continue comparing
		 */
		for(int i=0;i<this.size();i++){
			if(borr){
				borr=false;
				if(one.data==0){
					borr=true;
					oneMinus=9;			//can't do minus 1 from 0, have to set it to 9
				}
				else
					oneMinus=one.data-1;
				if(twoDone)
					dif=oneMinus;
				else if(oneMinus<two.data){	//need to borrow
					borr=true;
					dif=10+oneMinus-two.data;	//since we borrowed, add 10 more
				}
				else
					dif=oneMinus-two.data;
			}
			else{
				if(twoDone)
					dif=one.data;
				else if(one.data<two.data){ //need to borrow
					borr=true;
					dif=10+one.data-two.data;	//since borrowed, add 10
				}
				else
					dif=one.data-two.data;
			}
			total.addItem(dif);
			if(one.next==null){		//if nothing more to subtract
				total.reverse();
				while(total.firstNode.data==0)	//remove leading 0's
					total.removeItem();
				total.reverse();
				break;
			}
			else
				one=one.next;
			if(two.next==null)
				twoDone=true;
			else
				two=two.next;
		}
		return total;
	}


	/**Defined the way we expect compareTo to be defined for numbers.
	// If one number has more digits than the other then clearly it is bigger
	// (since there are no leading 0s). Otherwise, the numbers must be compared digit by digit.
	// Since this requires the most significant digit to be processed first
	// (which is at the end of the list), we cannot just iterate through the digits as given.
	// We will use what is given in the LinkedDS class – first reverse() the
	// Nodes in both numbers, then do the comparison in a sequential way,
	// then reverse() again to restore the original numbers.
	 */

	public int compareTo(ReallyLongInt rOp)
	{
		/**
		 * compare numOfEntries first, if one int is shorter it is automatically less than
		 *
		 * if equal num, then need to compare node to node
		 */
		if(this.size()<rOp.size())
			return -1;
		else if(this.size()>rOp.size())
			return 1;
		else{
			Node one = this.firstNode;
			Node two=rOp.firstNode;
			for(int i=0;i<this.size();i++){		//compare each node, return as soon as value differs
				if(one.data>two.data)
					return 1;
				else if(one.data<two.data)
					return -1;
				else{							//otherwise keep checking. if no diff, return 0
					one=one.next;
					two=two.next;
				}

			}
		}
		return 0;
	}

	/**Defined the way we expect equals to be defined for objects – comparing the data and not the reference.
	// Don't forget to cast rightOp to ReallyLongInt so that its nodes can be accessed
	// (note: the argument here is Object rather than ReallyLongInt because we are overriding equals() from
	//the version defined in class Object).
	// Note: This method can easily be implemented once compareTo() has been completed.
	 */

	public boolean equals(Object rightOp)
	{
		/**
		 * just use compareTo and whether it returns 0
		 */
		if(compareTo((ReallyLongInt)rightOp)==0)
			return true;
		else
			return false;
	}

	//Multiply the current ReallyLongInt by 10num .
	// Note that this can be done very simply through adding of nodes containing 0's.
	public void multTenToThe(int num)
	{
		/**
		 * add on however many 0's to the end of the int
		 */
		this.reverse();
		for(int i=0;i<num;i++)
			this.addItem(0);
		this.reverse();
	}

	//Divide the current ReallyLongInt by 10num . Note that this can be done very simply through shifting.
	public void divTenToThe(int num)
	{
		/**
		 * use rightShift to remove however many values
		 */
		this.reverse();
		rightShift(num);
		this.reverse();
	}
}
