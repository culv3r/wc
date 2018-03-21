package com.dannyrlarsen.wordcount;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestWC {

	protected static Map<String,Integer> unsorted, sorted, testUnsorted, testSorted;
	protected static String input;
	protected static Scanner testIn;
	protected static FileWriter fileWriter;
	protected static File testFile, file;
	
	@SuppressWarnings("resource")
	@BeforeAll
	static void initAll() throws IOException {
		System.out.println("Setting things up...");
		unsorted = new HashMap<String,Integer>();
		sorted = new LinkedHashMap<String,Integer>();
		unsorted.put("the", 2);
		unsorted.put("broadcast", 2);
		unsorted.put("a", 3);
		unsorted.put("system", 2);
		unsorted.put("test", 3);
		unsorted.put("of",2);
		unsorted.put("repeat",1);
		unsorted.put("this", 3);
		unsorted.put("only", 1);
		unsorted.put("emergency", 2);
		unsorted.put("i", 1);
		unsorted.put("is", 3);
		sorted.put("a", 3);
		sorted.put("test", 3);
		sorted.put("this", 3);
		sorted.put("is", 3);
		sorted.put("the", 2);
		sorted.put("broadcast", 2);
		sorted.put("system", 2);
		sorted.put("of", 2);
		sorted.put("emergency", 2);
		sorted.put("repeat", 1);
		sorted.put("only", 1);
		sorted.put("i", 1);
		input = "This is a test of the emergency broadcast system, I repeat, "
				+ "this is a test of the emergency broadcast system. This is "
				+ "only a test.";
		testIn = new Scanner(input).useDelimiter("\\Z");
		fileWriter = new FileWriter("output.txt");
		testFile = new File("testOutput.txt");
		file = new File("output.txt");
	}
	
	@Test
	void testMapCount() {
		
		testUnsorted = new HashMap<String,Integer>();
		testUnsorted = WC.mapCount(testIn);
		assertEquals(unsorted,testUnsorted);
	}
	
	@Test
	void testMapSort() throws IOException {
		testSorted = new LinkedHashMap<String,Integer>();
		testSorted = WC.mapSort(unsorted);
		assertEquals(sorted,testSorted);
	}
	
	@Test
	void testWriteResult() throws IOException {
		WC.setMaxStrLen(9);
		WC.writeResult(sorted, fileWriter);
		assertTrue(FileUtils.contentEquals(file,testFile));
	}
	
	@Test
	void testWordCount() throws IOException {
		WC.wordCount(testIn, fileWriter);
		assertTrue(FileUtils.contentEquals(file, testFile));
	}
	
	@AfterAll
	static void tearDownAll() throws IOException {
		testIn.close();
		fileWriter.close();
		file.delete();
		System.out.println("Everything torn down!");
	}
	

}
