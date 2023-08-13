package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    
    
    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
		this.size = 0;
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
	    //TODO: Implement this method.
		word = word.toLowerCase();
		boolean isWord = this.isWord(word);
		if(isWord) return false;
		
		char [] allCharacters = word.toCharArray();
		TrieNode curNode = this.root;
		int wordLength = allCharacters.length;
		int counter = 0;
		
		for (int i = 0; i < wordLength; i++) {
			char c = allCharacters[i];
			TrieNode isNodeExis = curNode.getChild(c);
			
			if (isNodeExis == null) {
				TrieNode newNode = curNode.insert(c);
				curNode = newNode;
			}
			else {
				curNode = isNodeExis;
			}
			counter = i;
		}
		this.setIsWord(counter, wordLength, curNode);
		this.size++;
		return true;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //TODO: Implement this method
	    return this.size;
	}
	
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s) 
	{
	    // TODO: Implement this method
		boolean flag = false;
		s = s.toLowerCase();
		char [] allCharacters = s.toCharArray();
		TrieNode currNode = this.root;
		for (Character charString: allCharacters) {
			flag = true;
			TrieNode isExistNode = currNode.getChild(charString);
			if (isExistNode == null) {
				return false;
			}
			currNode = isExistNode;
		}
		if (flag) {
			if(!currNode.endsWord()) {
				currNode.setEndsWord(true);
				this.size++;
			} 
			
			return true;
		}
		else return false;
	}
	

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 
    	 TrieNode stemNode;
    	 List<String> predicList = new ArrayList<String>();
    	 int counter = 0;
    	 
    	 if (!prefix.equals("")) {
    		 stemNode = this.findPrefix(prefix);
        	 if (stemNode == null) {
        		 return predicList;
        	 }
    	 }
    	 else stemNode = this.root;
    	 
    	 Queue<TrieNode> queue = new LinkedList<TrieNode>();
    	 queue.add(stemNode);
    	 while (!queue.isEmpty() && counter < numCompletions) {
    		 stemNode = queue.remove();
    		 
    		 if(stemNode.endsWord()) {
    			 predicList.add(stemNode.getText());
    			 counter++;
    		 }
			 Set<Character> allchar = stemNode.getValidNextCharacters();
    		 for (char c : allchar) {
    			 queue.add(stemNode.getChild(c));
    		 }
    	 }
         return predicList;
    }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		System.out.println(curr.getText()+ "--" + curr.endsWord());
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	
 	private void setIsWord(int index, int size, TrieNode node) {
 		if (index + 1 == size) {
 			node.setEndsWord(true);
 		}
 		else {
 			node.setEndsWord(false);
		}
 	}
 	 	
 	private TrieNode findPrefix(String prefix) {
 		char [] allChar = prefix.toCharArray();
 		TrieNode currNode = this.root;
 		boolean flag = false;
 		for (char c : allChar) {
 			flag = true;
 			TrieNode isExist = currNode.getChild(c);
 			if (isExist == null) return null;
 			currNode = isExist;
 		}
 		if(flag) return currNode;
 		else return null;
 	}


	
}