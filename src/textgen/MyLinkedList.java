package textgen;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	private int size;
	private List<LLNode<E>> myLinkedList;
	

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		myLinkedList = new LinkedList<LLNode<E>>();
		head = new LLNode(null);
		tail = new LLNode(null);
		
		head.next = tail;
		tail.prev = head;
		
		myLinkedList.add(head);
		myLinkedList.add(tail);
		size = 0;
		// TODO: Implement this method
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element) 
	{
		if (element == null) {
			throw new NullPointerException();
		}
		try {
			LLNode<E> newNode = new LLNode(element);
			newNode.next = tail;
			newNode.prev = tail.prev;
			
			tail.prev.next = newNode; // LastElement = tail.prev;    LastElement = elementNode;
			tail.prev = newNode;
			size++;
			return true;
		} catch (NullPointerException nullPointerException) {
			throw nullPointerException;
		}
		
		// TODO: Implement this method
		
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		// TODO: Implement this method.
		if (index > this.size || this.size == 0) {
			throw new IndexOutOfBoundsException();
		}
		
		LLNode<E> currentNode1 = head.next;
		for (int i = 0; i < this.size; i++) {
			if (i != index) {
				currentNode1 = currentNode1.next;
			}
			else {
				return currentNode1.data;
			}
		}	
		throw new IndexOutOfBoundsException();
	}
	
	public LLNode<E> getObj(int index) 
	{
		// TODO: Implement this method.
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		LLNode<E> currentNode1 = head.next;
		for (int i = 0; i < this.size; i++) {
			if (i != index) {
				currentNode1 = currentNode1.next;
			}
			else {
				return currentNode1;
			}
		}	
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		// TODO: Implement this method
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (element == null) {
			throw new NullPointerException();
		}
		
		try {
			LLNode<E> currentNode = (this.size != 0) ? getObj(index) : tail;
			if (this.size != 0) {
				currentNode = getObj(index);
			}
			
			LLNode<E> newNode = new LLNode(element);
			
			newNode.next = currentNode;
			newNode.prev = currentNode.prev;
			
			currentNode.prev.next = newNode;
			currentNode.prev = newNode;
			size++;
			
		} catch (NullPointerException npe) {
			// TODO: handle exception
			throw npe;
		}
		
	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		LLNode<E> node = head.next;
		int counter = 0;
		while (node != tail) {
			counter++;
			node = node.next;
		}
		return counter;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		LLNode<E> removableNode = getObj(index);
		LLNode<E> backNode = removableNode.prev;
		LLNode<E> frontNode = removableNode.next;
		
		backNode.next = frontNode;
		frontNode.prev = backNode;
		size--;
		// TODO: Implement this method
		return removableNode.data;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		// TODO: Implement this method
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (element == null) {
			throw new NullPointerException();
		}
		
		LLNode<E> currentNode = getObj(index);
		E oldVal = currentNode.data;
		currentNode.data = element;
		
		return oldVal;
	} 
	
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}
	
	public String toString() {
		return (String) data;
	}

}
