import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


// Trevon Jones
// 1/31/19
// The purpose of this program is to implement the Gale-Shapley Algorithm and create a stable matching from
// preferences of men and woman.

public class stableMatching {

	// Define Linked List and its functions.
	static class LinkedList {
		Node head;

		public LinkedList() {
			head = null;
		}

		public void addFirst(String w) {
			head = new Node(w, head);
		}

		public String getFirst() {
			return head.womenInteger;
		}

		public void removeFirst() {
			// String temp = getFirst();
			head = head.next;
		}

		public void addMenPref(String m) {
			if (head == null) {
				addFirst(m);
			} else {
				Node newPref = head;

				while (newPref.next != null) {
					newPref = newPref.next;
				}

				newPref.next = new Node(m, null);
			}
		}

		static class Node {
			String womenInteger;
			Node next;

			public Node(String x, Node n) {
				womenInteger = x;
				next = n;
			}
		}

	}

	public static void main(String[] args) throws FileNotFoundException 
	{
		// Notation
		System.out.println("\nNOTE: Each man and woman in the algorithm is represented by their actual number - 1.\nFor Example: Man 1 is Man 0, Woman 3 is Woman 2 etc.\n");
		// Access and scan men preference file.
		File boys = new File(args[0]);
		Scanner getMPref = new Scanner(boys);

		// Get number of Men from the first line.
		String size = getMPref.nextLine();
		System.out.println("Total amount of Men: " + size);

		// Create Array of LinkedList for Each men's preference. Preferences starts at
		// 0.
		LinkedList[] menPreference = new LinkedList[Integer.parseInt(size)];

		for (int i = 0; i < Integer.parseInt(size); i++) {
			String prefLine = getMPref.nextLine();
			menPreference[i] = new LinkedList();
			int lineIterator = 0;

			System.out.println("Preference For man: " + i);
			while (lineIterator < prefLine.length()) {
				String hisGirl = String.valueOf(prefLine.charAt(lineIterator));
				if (!hisGirl.contains(" ")) {
					int theGirl = Integer.parseInt(hisGirl) - 1;
					System.out.print(theGirl + " ");

					menPreference[i].addMenPref(Integer.toString(theGirl));

				}
				lineIterator++;
			}
			System.out.println();
		}

		// Close men preference file
		getMPref.close();

		// Access Women Preference File
		File girls = new File(args[1]);
		Scanner getWPref = new Scanner(girls);

		String wsize = getWPref.nextLine();
		System.out.println("\nTotal amount of woman: " + wsize);

		// Create an Array of String arrays for girls preferences
		String[][] womenPreference = new String[Integer.parseInt(wsize)][Integer.parseInt(size)];

		for (int i = 0; i < Integer.parseInt(wsize); i++) {
			String prefWLine = getWPref.nextLine();
			int lineIterator = 0;
 
			System.out.println("Preference For woman: " + i);
			int count = 0;
			while (lineIterator < prefWLine.length()) {
				String herMan = String.valueOf(prefWLine.charAt(lineIterator));
				if (!herMan.contains(" ")) {
					int theMan = Integer.parseInt(herMan) - 1;
					System.out.print(theMan + " ");

					womenPreference[i][count] = Integer.toString(theMan);
						count++;

				}
				lineIterator++;
			}
			System.out.println();
		}

		// close women preference file
		getWPref.close();

		// Create stack to represent free men
		Stack<String> freeMen = new Stack<String>();
		for (int i = 0; i < Integer.parseInt(size); i++) {
			freeMen.push(Integer.toString(i));
		}

		// Create Stable Matching Array Pairs
		String[] wife = new String[Integer.parseInt(size)];
		String[] husband = new String[Integer.parseInt(wsize)];

		// initialize stable matching to -1
		for (int i = 0; i < Integer.parseInt(size); i++) {
			wife[i] = "-1";
			husband[i] = "-1";
		}

		// Begin Algorithm

		while (!freeMen.isEmpty()) {
			String man = freeMen.pop();
			String woman = menPreference[Integer.parseInt(man)].getFirst();

			// Wife of the man
			System.out.println("current man is: " + man);
			System.out.println("he prefers: " + woman);
			
			
			if (wife[Integer.parseInt(man)] == "-1") {
				// Husband of the woman
				if (husband[(Integer.parseInt(woman))] == "-1") {
					// Both man and woman are free
					System.out.println("They are both free, they are matched");
					husband[Integer.parseInt(woman)] = man;
					wife[Integer.parseInt(man)] = woman;
					menPreference[Integer.parseInt(man)].removeFirst();
				}
				// Woman is already taken, check if she wants to reject
				else {
					System.out.println("His preference is already matched, check if rejects or not");
					// Create Inverse Preference List of Woman.
					String[] wip = new String[Integer.parseInt(size)];
					System.out.print("Woman " + woman + "'s preference: ");
					for (int i = 0; i < Integer.parseInt(size); i++) {
						  System.out.print(womenPreference[Integer.parseInt(woman)][i] + " ");
						wip[Integer.parseInt(womenPreference[Integer.parseInt(woman)][i])] = Integer.toString(i);
					}
					System.out.println();

					// Check her preference of the new man vs the man she is currently with
					int newMan = Integer.parseInt(wip[Integer.parseInt(man)]);
					int currentMan = Integer.parseInt(wip[Integer.parseInt(husband[Integer.parseInt(woman)])]);

					// If the preference of the freeman is better than the preference of the current
					// man
					System.out.println("\n\nIF NM Pref: " + newMan + " <  CM Pref: " + currentMan);
					if (newMan < currentMan) {
						System.out.println("She dumps her current man, and accepts the new proposal.");
						// The wife breaks up with current man, current man is now a free man.
						String x = husband[Integer.parseInt(woman)];
						wife[Integer.parseInt(x)] = "-1";
						
						freeMen.push(x);

						// New man and current women now are married
						husband[Integer.parseInt(woman)] = man;
						
						wife[Integer.parseInt(man)] = woman;
					}
					// Current Man is preferred, new Man is rejected. He is still free
					else {
						System.out.println("She reject the new proposal, and keeps her current man");
						menPreference[Integer.parseInt(man)].removeFirst();
						freeMen.push(man);
					}

				}
			}
			
			////////
			System.out.println("\nThe current stable matching pairs: man|woman and woman|man");
			for(int i = 0; i < Integer.parseInt(size); i++)
			{
				String wifey = wife[i];
				String hubby = husband[i];
				System.out.print(i + " | " + wifey + "\t" + i + "|" + hubby + "\n");
			}

		}
		
		for(int i = 0; i < Integer.parseInt(size); i++)
		{
			String wifey = wife[i];
			System.out.println(i + " " + wifey);
		}
		
		
		
		try {
			usePrintWriter(args[2], Integer.parseInt(size), wife);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nFile Written Successfully.");
		
	}
	

	// Function to print results to final document.
	public static void usePrintWriter(String fileName, int size, String[] stable) throws IOException
	{
	     
	    FileWriter fileWriter = new FileWriter(fileName);
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    
	    
		for(int i = 0; i < size; i++)
		{
			int man = i + 1;
			int woman = Integer.parseInt(stable[i]) + 1;
			printWriter.println(man + " " + woman);
		}
	    printWriter.close();
	}
	
	

}
