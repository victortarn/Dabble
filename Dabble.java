package dabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import dict.Dictionary;

/**
 * A class that models the state of the puzzle game Dabble.
 * 
 * <p>
 * The game Dabble is made up of five English words of lengths 2, 3, 4, 5, and 6
 * (these five words make up a solution for the puzzle). The letters of the all
 * of the words are randomly scrambled together to produce five scrambled puzzle
 * words of lengths 2, 3, 4, 5, and 6. For example, the five solution words:
 * 
 * <p>
 * {@code it, you, here, batch, burner}
 * 
 * <p>
 * might produce the five scrambled words:
 * 
 * <p>
 * {@code un, uei, ytrc, bahrt, obrehe}
 * 
 * <p>
 * A player attempts to unscramble the words by repeatedly exchanging a letter
 * from one scrambled word with the letter in a second scrambled word.
 * 
 * <p>
 * The puzzle is solved when the player has formed five English words of lengths
 * 2, 3, 4, 5, and 6 (not necessarily the same words as the original solution
 * because there is often multiple solutions).
 *
 */
public class Dabble {

	private Map<Integer, String> solution;
	private Map<Integer, String> scrambled;

	/**
	 * The dictionary used by the class.
	 */
	public static final Dictionary DICT = new Dictionary();

	/**
	 * The shortest word length in the game.
	 */
	public static final int MIN_WORD_LENGTH = 2;

	/**
	 * The longest word length in the game.
	 */
	public static final int MAX_WORD_LENGTH = 6;

	/**
	 * The number of words in the game.
	 */
	public static final int NUMBER_OF_WORDS = 5;

	/**
	 * Initializes this dabble to a specific set of scrambled and solution words for
	 * debugging and testing purposes.
	 * 
	 * @param notUsed not used, included so that this constructor would have a
	 *                different signature than the other constructors
	 */
	public Dabble(int notUsed) {
		this.solution = new TreeMap<>();
		this.scrambled = new TreeMap<>();

		this.solution.put(2, "ad");
		this.solution.put(3, "bet");
		this.solution.put(4, "cook");
		this.solution.put(5, "dumps");
		this.solution.put(6, "eclair");

		this.scrambled.put(2, "ri");
		this.scrambled.put(3, "alc");
		this.scrambled.put(4, "espm");
		this.scrambled.put(5, "udkoo");
		this.scrambled.put(6, "ctebad");
	}

	/**
	 * Initialize the words of the game by choosing random words from a dictionary.
	 */
	public Dabble() {
		this.solution = new TreeMap<>();
		this.scrambled = new TreeMap<>();
		
		for (int i = 0; i < NUMBER_OF_WORDS; i++ ) {
			Random rng = new Random();
			List<String> t = Dabble.DICT.getWordsByLength(i + MIN_WORD_LENGTH);
			int index = rng.nextInt(t.size());
			this.solution.put(i + MIN_WORD_LENGTH, t.get(index));
		}
		scrambled = scrambleWords(solution.values().toArray(new String[0]));
	}
	
	private Map<Integer, String> scrambleWords(String... words) {
		ArrayList<String> letters = new ArrayList<String>(); 
		
	 	for (int i = 0; i < NUMBER_OF_WORDS; i++) {
	 		for (int j = 0; j < i + MIN_WORD_LENGTH; j++) {
	 			letters.add(words[i].substring(j, j + 1));
	 		}
	 	}
	 	
	    Collections.shuffle(letters);
	    
	    Map<Integer, String> scrambledWords;
	    scrambledWords = new TreeMap<>();
	    
	    int index = 0;
	    
	    for (int i = 0; i < NUMBER_OF_WORDS; i++) {
	    	List<String> lettersInWord = letters.subList(index, index + i + MIN_WORD_LENGTH);
	    	String word = String.join("", lettersInWord);
	    	scrambledWords.put(i + MIN_WORD_LENGTH, word);
	    	
	    	index += i + MIN_WORD_LENGTH;
	    } 
		return scrambledWords;
	}
	/**
	 * Initialize the words of the game by using the specified words.
	 * 
	 * <p>
	 * There must be exactly {@code NUMBER_OF_WORDS} strings in the {@code words}
	 * otherwise an exception is thrown. Furthermore, the strings must be in
	 * ascending order of length from {@code MIN_WORD_LENGTH, MIN_WORD_LENGTH + 1,
	 * MIN_WORD_LENGTH + 2, ... , MAX_WORD_LENGTH}. Finally, the strings must all be
	 * contained in the dictionary used by the class.
	 * 
	 * @param words an array of NUMBER_OF_WORDS strings in ascending order of length
	 * @throws IllegalArgumentException if
	 *                                  {@code words.length != Dabble.NUMBER_OF_WORDS}
	 *                                  or if the strings in word are not in
	 *                                  ascending order by length
	 */
	public Dabble(String... words) {
		 if (words.length != Dabble.NUMBER_OF_WORDS) {
		        throw new IllegalArgumentException("expected " + Integer.toString(Dabble.NUMBER_OF_WORDS) + " words, got " + Arrays.toString(words));
		    }
		 
		 for (int i = MIN_WORD_LENGTH; i <= MAX_WORD_LENGTH; i++) {
			  if (words[i - MIN_WORD_LENGTH].length() != i) {
				  throw new IllegalArgumentException("The strings in word are not in ascending order by length");
			  }
			}
		 
		 this.solution = new TreeMap<>();
		 this.scrambled = new TreeMap<>();
		 
		 for (int i = 0; i < NUMBER_OF_WORDS; i++ ) {
			 this.solution.put(i+ MIN_WORD_LENGTH, words[i]);
		 }
		 
		 scrambled = scrambleWords(solution.values().toArray(new String[0]));
	}

	/**
	 * Returns a string representation of the puzzle.
	 * 
	 * <p>
	 * The returned string consists of each scrambled word separated by a comma and
	 * space, followed by space-colon-space, followed by each solution word
	 * separated by a comma and space.
	 * 
	 * @return a string representation of the puzzle
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(this.scrambled.get(Dabble.MIN_WORD_LENGTH));
		for (int len = Dabble.MIN_WORD_LENGTH + 1; len <= Dabble.MAX_WORD_LENGTH; len++) {
			b.append(", ");
			b.append(this.scrambled.get(len));
		}
		b.append(" : ");
		b.append(this.solution.get(Dabble.MIN_WORD_LENGTH));
		for (int len = Dabble.MIN_WORD_LENGTH + 1; len <= Dabble.MAX_WORD_LENGTH; len++) {
			b.append(", ");
			b.append(this.solution.get(len));
		}
		return b.toString();
	}
	
	/**
	 * Returns {@code true} if each scrambled word is contained in the dictionary
	 * used by the class, {@code false} otherwise.
	 * 
	 * <p>
	 * It is not the case that the scrambled words must be equal to the solution
	 * words because it is possible that many different solutions exist for any
	 * given puzzle.
	 * 
	 * @return {@code true} if each scrambled word is contained in the dictionary
	 *         used by the class, {@code false} otherwise.
	 */
	public boolean isSolved() {
		for (int i = MIN_WORD_LENGTH; i <= MAX_WORD_LENGTH; i++) {
			String word = scrambled.get(i);
			if (DICT.contains(word) != true) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Exchange a letter in one scrambled word with a letter from a second scrambled
	 * word. The two scrambled words might be the same word, in which case two
	 * letters are exchanged in the same word.
	 * 
	 * <p>
	 * The letter having {@code index1} in the scrambled word having length
	 * {@code len1} is exchanged with the letter having {@code index2} in the
	 * scrambled word having length {@code len2}.
	 * 
	 * <p>
	 * Consider the dabble {@code d} whose string representation is equal to:
	 * 
	 * <p>
	 * {@code "eb, ueu, eyoh, rnhti, rrtacb : it, you, here, batch, burner"}
	 * 
	 * <p>
	 * Then {@code d.exchange(2, 0, 5, 4)} would exchange the first letter of
	 * {@code "eb"} with the last letter of {@code "rnhti"}, and
	 * {@code d.toString()} would return the string equal to:
	 * 
	 * <p>
	 * {@code "ib, ueu, eyoh, rnhtb, rrtacb : it, you, here, batch, burner"}
	 * 
	 * @param len1   the length of the first word
	 * @param index1 the index of the letter to exchange of the first word
	 * @param len2   the length of the second word
	 * @param index2 the index of the letter to exchange of the second word
	 * @throws IllegalArgumentException if {@code len1} or {@code len2} are not
	 *                                  valid Dabble word lengths, or if
	 *                                  {@code index1} or {@code index2} are not
	 *                                  valid indexes for their respective strings
	 */
	public void exchange(int len1, int index1, int len2, int index2) {
		if (len1 > Dabble.MAX_WORD_LENGTH 
			|| len2 > Dabble.MAX_WORD_LENGTH 
			|| index1 > len1 
			|| index2 > len2) {
			throw new IllegalArgumentException("Len1 and/or Len2 are not valid word lengths and/or index1 and/or index 2 are not valid indexes for their respective strings");
		}
		if (len1 == len2) {
			String word = scrambled.get(len1);
			StringBuilder a = new StringBuilder(word);
			
			char b = a.charAt(index1);
			char c = a.charAt(index2);
			
			a.setCharAt(index1, c);
			a.setCharAt(index2, b);
			
			scrambled.put(len1, a.toString());
		} 
		else {
			String word1 = scrambled.get(len1);
			String word2 = scrambled.get(len2);
			
			StringBuilder a = new StringBuilder(word1);
			StringBuilder b = new StringBuilder(word2);
			
			char c = a.charAt(index1);
			char d = b.charAt(index2);
			
			a.setCharAt(index1, d);
			b.setCharAt(index2, c);
			
			scrambled.put(len1, a.toString());
			scrambled.put(len2, b.toString());
		}		
	}

	/**
	 * Returns the map of scrambled words.
	 * 
	 * <p>
	 * The returned map maps the word length to a scrambled word.
	 * 
	 * @return the map of scrambled words
	 */
	public Map<Integer, String> getScrambledWords() {
		// ALREADY DONE FOR YOU
		return this.scrambled;
	}

	/**
	 * Returns a map of solution words. More than one solution may exist; this
	 * method always returns the solution that was used to generate the puzzle.
	 * 
	 * <p>
	 * The returned map maps the word length to a solution word.
	 * 
	 * @return the map of solution words
	 */
	public Map<Integer, String> getSolutionWords() {
		// ALREADY DONE FOR YOU
		return this.solution;
	}



	public static void main(String[] args) {
		//Dabble dab = new Dabble(0);
		//Dabble dab = new Dabble();
		Dabble dab = new Dabble("in","run","hunt","cheat","prints");
		System.out.println(dab);
		
	}
}
