package com.dannyrlarsen.wordcount;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Daniel Larsen (culv3r)
 * This program takes a file, "input.txt", parses the file and stores word
 * frequency inside a Map. The Map is then sorted using Streams and formatted
 * as a frequency descending histogram in file: "output.txt".
 */
public class WC {
	
	/**
	 * Global Variables
	 */
	private static int maxStrLen;
	
	/**
	 * @param args Command line arguments, if required
	 * Main method initializes File and IO handling before passing to method
	 * wordcount with Scanner and FileWriter Objects.
	 */
	public static void main(String args[]){
		FileWriter fileWriter;
		Scanner in;
		// Local Variables
		File input = new File("input.txt");
		
		// Try\Catch for File IO Error Handling
		try {
			in = new Scanner(input);
			fileWriter = new FileWriter("output.txt");
			// Delimeter is EOF
			in.useDelimiter("\\Z");
			
			wordCount(in, fileWriter);

			in.close();
			fileWriter.close();
		} catch (Exception e) {
			
			// File not found, kick traceback
			e.printStackTrace();
			
		}
	}
	
	/**
	 * @param in Scanner object initialized with input file
	 * @param fileWriter object initialized with output file
	 * WordCount method handles execution of sub methods and stores references
	 * to Scanner and FileWriter objects.
	 */
	public static void wordCount(Scanner in, FileWriter fileWriter) {
		Map<String,Integer> unsorted = mapCount(in);
		Map<String,Integer> sorted = mapSort(unsorted);
		writeResult(sorted, fileWriter);
	}
	
	
	/**
	 * @param in Scanner object initialized with input file
	 * @return Unsorted map file with word frequency count
	 * MapCount method takes the scanner object, parses the file, and then maps
	 * the words to a hashmap, incrementing if the word has occurred more than
	 * once.
	 */
	public static Map<String,Integer> mapCount(Scanner in){
		Map <String,Integer> count = new HashMap<String, Integer>();
		String line;
		setMaxStrLen(0);
		
		// If file is larger than scanner buffer, loop until done
		while(in.hasNext()) {
			
			// Set line equal to next part of scanner and then remove
			// punctuation.
			line = in.next();
			line = line.replaceAll("\\W", " ").toLowerCase();
			
			// Create array of strings, splitting on white space.
			String splitted[] = line.split("\\s+");
			
			// Iterate through array of strings
			for (String word:splitted) {
				
				// Base Case, map will always be empty on first entry.
				if (count.isEmpty()) {
					count.put(word, 1);
				} else {
					// Encapsulated remaining cases to prevent erroneous data
					// If word exists, increment by one
					if (count.containsKey(word)) {
						count.put(word, count.get(word)+1);
					}
					
					// If word does not exist, add to map. 
					if (!(count.containsKey(word))) {
						count.put(word, 1);
					}
					
					// Check each word's length, set max length for formatting.
					if (word.length() > getMaxStrLen()) {
						setMaxStrLen(word.length());
					}
				}
			}
		}
		// System.out.println("Count: " + count);
		return count;
	}
	
	
	/**
	 * @param unsorted Map containing unsorted word/frequency pairings
	 * @return Map sorted by value, then by key
	 * MapSort method takes the unsorted map and sorts it using the streams
	 * package, comparators package, and entry packages. Returns a sorted
	 * LinkedHashMap to wordcount method.
	 */
	public static Map<String,Integer> mapSort(Map<String, Integer> unsorted){		
		// System.out.println("Initial Map: " + count.toString());
		
		Map<String, Integer> byVal = unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, 
                		Map.Entry::getValue, (oldVal, newVal) -> oldVal, 
                		LinkedHashMap::new));
		// System.out.println("byVal: " + byVal);
		return byVal;
	}
	
	
	/**
	 * @param sorted Map of sorted key value pairs, sorted by value
	 * @param fileWriter object initialized to output file
	 * writeResult method takes the sorted LinkedHashMap and fileWriter and
	 * converts the data in the map to the desired format before writing the
	 * formatted data to "Output.txt"
	 */
	public static void writeResult(Map<String, Integer> sorted, 
			FileWriter fileWriter) {
		// Local Variables
		PrintWriter printWriter = new PrintWriter(fileWriter);
		StringBuilder sb;
		String tmp, result;
		int mapSize = 0;
		int mapCount = 0;
		
		// Use Mapsize to determine when to change fileWriter printing style
		mapSize = sorted.size();
		
		// System.out.println("Map Size = " + Integer.toString(mapSize));
		// System.out.println("Sorted Map: " + byVal);
		
		String format = "%" + Integer.toString(getMaxStrLen()) + "s | ";
		
		// Iterate through all entries, format, and write to file.
		for(Map.Entry<String, Integer> entry:sorted.entrySet()) {
			// Initilization and Declaration of counting variable
			sb = new StringBuilder();
			int iCountdown = entry.getValue();
			
			// Store formatted string in temp holder and append to 
			// stringbuilder.
			tmp = String.format(format, entry.getKey());
			sb.append(tmp);
			
			// Append desired amount of histogram bars until last, then append
			// value.
			for(int i = 0; i < iCountdown; i++) {
			
				if (iCountdown - 1 == i) {
					tmp = "= (" + entry.getValue() + ")";
					sb.append(tmp);
				} else {
					sb.append("=");
				}
				
			}
			
			mapCount++;
			result = sb.toString();
			
			// Formatting Conditional, if at last value, just print to avoid
			// extra line.
			if (mapCount < mapSize) {
				printWriter.println(result);
			} else {
				printWriter.print(result);
			}
		}
		
		printWriter.close();
	}
	
	/**
	 * @param x new value of maxStrLen
	 * Sets the value of maxStrLen
	 */
	public static void setMaxStrLen(int x) {
		maxStrLen = x;
	}
	
	/**
	 * @return value of maxStrLen
	 * Returns the value of maxStrLen
	 */
	public static int getMaxStrLen() {
		return maxStrLen;
	}
	
}
