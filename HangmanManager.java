/*
Anderson Lu
Cse 143 AN with May Wang
Homework 4, HangmanManager
Creates a program to manage a game of evil hangman.
Based off the game of hangman, this program will cheat
and delay picking a word until necessary, this program
will handle and keep track of the game.
*/
import java.util.*;

public class HangmanManager {
   //Creates the count for amount of guesses.
   private int gCount;
   //Creates a String for the current pattern.
   private String pattern;
   //Creates a set to contain the words in dictionary.
   private Set<String> list;
   //Creates a set to contain all the letters guessed.
   private Set<Character> letters;
   
   /*
   Takes in Collection<String> "dictionary", Int "length" and
   Int "max" as parameters.
   
   Pre: "max" >= 0 or "length" >= 1.
   Throws an IllegalArgumentException if preconditions are not met.
   
   Post: Initializes the "max" to the number of guesses, and
   adds the content with size equal to the "length" requested in
   "dictionary" to a set.
   */
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (max < 0 || length < 1) {
         throw new IllegalArgumentException();
      }
      //Sets the count for guesses and the set for the words in dictionary.
      //Also sets the count for letters guessed.
      gCount = max;
      list = new TreeSet<String>();
      letters = new TreeSet<Character>();
      pattern = "";
      //Adds all of the words with the appropriate length to the list.
      for (String word: dictionary) {
         if (word.length() == length) {
            list.add(word);
         }
      }
      //Creates a pattern for unknown letters.
      for (int i = 0; i < length; i++) {
         pattern = pattern + "- ";
      }
   }
   
   //Returns the set of words that is in the list.
   public Set<String> words() {
      return list;
   }
   
   //Returns the amount of guesses left.
   public int guessesLeft() {
      return gCount;
   }
   
   //Returns the letters already guessed.
   public Set<Character> guesses() {
      return letters;
   }
   
   /*
   Pre: Set of words is not empty.
   Throws an IllegalStateException if precondition is not met
   
   Post: Returns the pattern of the word after
   a letter is guessed.
   */
   public String pattern() {
      if (list.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   /*
   Takes in a Character guess as a parameter and
   records the guesses made by the user.
   
   Pre: "gCount" >= 1
   Throws an IllegalStateException if precondition is not met.
   
   Pre: IllegalStateException is thrown or !letters.contains(guess)
   Throws an IllegalArgumentException if preconditions are not met.
   
   Post: Records the guesses made by the user. Decides what
   set of words to use based on the guesses that the user
   makes. Updates the count for guess and returns
   the number of the guessed letter in the pattern of the word, 
   if the guessed letter is in the word.
   */
   public int record(char guess) {
      if (gCount < 1) {
         throw new IllegalStateException();
      } else if (letters.contains(guess)) {
         throw new IllegalArgumentException();
      }
      //Adds guessed letters to guessed set.
      letters.add(guess);
      //Creates map to hold the patterns and the words.
      Map<String, Set<String>> nList = new TreeMap<>();
      helperRecord(guess, nList);
      //Creates a new list of words based on the guessed letter.
      nDict(nList);
      return occur(guess);
   }
   
   /*
   Takes in a Character "guess" as a parameter and
   finds the occurrences of the guessed character if
   it is in the word. Decreases the guess count if 
   guess is not in word.
   */
   private int occur(char guess) {
      int occurred = 0;
      for (int i = 0; i < pattern.length(); i++) {
         //Increases occur count if character is in word.
         if (guess == pattern.charAt(i)) {
            occurred++;
         }
      }
      //If guess is incorrect, decreases count of guesses.
      if (occurred == 0) {
         gCount--;
      }
      return occurred;
   }
   
   /*
   Takes in a Map<String, Set<String>> nList as a parameter.
   Changes the list of words in the general set to the map
   with the largest amount of words.
   */
   private void nDict(Map<String, Set<String>> nList) {
      //Create variable to keep track of the amount of words in map.
      int sum = 0;
      for (String words : nList.keySet()) {
         //fix the dictionary, if the count of words is larger.
         if (sum < nList.get(words).size()) {
            //Changes the pattern to the word.
            pattern = words;
            //Changes the old list to the new list.
            list = nList.get(words);
            //Sets the tracker to the largest size of words in a pattern.
            sum = nList.get(words).size();
         }
      }
   }
   
   /*
   Takes in a Character guess and a Map<String, Set<String>> nList as
   parameters. Checks every word in the list and creates a pattern based
   on the guessed letter. Adds the word to a list that has the same pattern
   or creates a new list to hold the new pattern and adds the word.
   */
   private void helperRecord(char guess, Map<String, Set<String>> nList) {
      //Checks through each word in list.
      for (String word : list) {
         //Creates a pattern from the guessed and saves the pattern.
         String key = helperPattern(guess, word);
         //Checks if the map nList has the saved pattern.
         if (!nList.containsKey(key)) {
            //Adds the pattern to the map with a new list of words.
            nList.put(key, new TreeSet<>());
         }
         //Adds the word to the list with the same pattern.
         nList.get(key).add(word);
      }
   }
   
   /*
   Takes in Character "guess" and String "word" as parameters.
   Checks if the guessed letter is in the word creates
   pattern of dashes from the letter guessed. Returns the pattern
   from that word.
   */
   private String helperPattern(char guess, String word) {
      //Creates a string to hold the pattern.
      String result = "";
      for (int i = 0; i < word.length(); i++) {
         //Checks if the guessed letter is not in the word.
         //Adds a dash if guessed letter is not in word.
         if (word.charAt(i) != guess) {
            result += pattern.charAt(i * 2) + " ";
         } else {
            //Adds the character guessed to the dashed pattern.
            result += guess + " ";
         }
      }
      //returns the pattern.
      return result;
   }
}