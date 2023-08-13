package textgen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.hamcrest.core.StringEndsWith;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		// TODO: Implement this method
		List<String> listOfWords = new ArrayList<String>();
		listOfWords = this.getTokens("[a-zA-Z|.?!]+", sourceText);
		if( listOfWords.size() != 0) {
			this.starter = listOfWords.get(0);
		}
		String prevWord = starter;
		
		for (int i = 1; i < listOfWords.size(); i++) {
			ListNode isExistWordNode = this.isExistWord(wordList, prevWord);
			String nextWord = listOfWords.get(i);
			
			if (isExistWordNode != null) {
				isExistWordNode.addNextWord(nextWord);
			}
			else {
				ListNode newNode = new ListNode(prevWord);
				newNode.addNextWord(nextWord);
				wordList.add(newNode);
			}
			prevWord = nextWord;
			
		}
		
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
	    // TODO: Implement this method
		if (numWords == 0) return "";
		
		String currentWord = starter;
		String output = "";
		output = currentWord;
		int counter = 0;
		int size = wordList.size();
		while (counter < numWords - 1 && size != 0) {
			ListNode isExistNode = this.isExistWord(wordList, currentWord);
			try {
				Random genRandom = new Random();
				String newWord = isExistNode.getRandomNextWord(genRandom);
				output += " "+ newWord;
				currentWord = newWord;
				counter++;
			} catch (NullPointerException npe) {
				// TODO: handle exception
				Random genRandom = new Random();
				int random = genRandom.nextInt(wordList.size());
				currentWord = wordList.get(random).getWord();
			}
		}
		return output;
	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		// TODO: Implement this method.
		wordList = new LinkedList<ListNode>();
		starter = "";
		
		List<String> listOfWords = new ArrayList<String>();
		listOfWords = this.getTokens("[a-zA-Z]+", sourceText);
		if (listOfWords.size() != 0) {
			this.starter = listOfWords.get(0);
		}
		String prevWord = starter;
		
		for (int i = 1; i < listOfWords.size(); i++) {
			ListNode isExistWordNode = this.isExistWord(wordList, prevWord);
			String nextWord = listOfWords.get(i);
			
			if (isExistWordNode != null) {
				isExistWordNode.addNextWord(nextWord);
			}
			else {
				ListNode newNode = new ListNode(prevWord);
				newNode.addNextWord(nextWord);
				wordList.add(newNode);
			}
			prevWord = nextWord;
			
		}
		
	}
	
	// TODO: Add any private helper methods you need here.
	private ListNode isExistWord(List<ListNode> listNodes, String word) {
		for (ListNode node : listNodes) {
			if (node.getWord().equals(word)) {
				return node;
			}
		}
		return null;
	}
	
	private List<String> getTokens(String pattern, String text)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);
		
		while (m.find()) {
			tokens.add(m.group());
		}
		
		return tokens;
	}
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		System.out.println("========================================");
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
		// TODO: Implement this method
	    // The random number generator should be passed from 
	    // the MarkovTextGeneratorLoL class
		try {
			int random = generator.nextInt(nextWords.size());
			String randomWord = nextWords.get(random); 
			return randomWord;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			System.out.println("This is out of bounds...");
			return null;
		}
	    
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


