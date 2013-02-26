import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * Main P2 class for Project 2.
 * 
 * This class contains the main method for this project, which does
 * several things.  First, it deals with the command line parameter
 * and usage.  Second, it attempts to open and read lines from the
 * input file.  Third, it handles the commands by using the DNATree
 * class.  Fourth, it outputs appropriate errors and prints.
 * 
 * @author Chris Schweinhart (schwein)
 * @author Nate Kibler (nkibler7)
 */
public class P2 {

	/**
	 * Constant string patterns for command matching.  These are
	 * used for regular expression matching with the commands
	 * given by the input file.  They all allow for uppercase or
	 * lowercase commands or sequences, and any number of spacing
	 * between arguments.
	 */
	private static final String INSERT_PATTERN = "^ *(insert|INSERT) *[ACGTacgt]+ *$";
	private static final String REMOVE_PATTERN = "^ *(remove|REMOVE) *[ACGTacgt]+ *$";
	private static final String PRINT_PATTERN = "^ *(print|PRINT) *$";
	private static final String PRINT_LENGTHS_PATTERN = "^ *(print|PRINT) *(lengths|LENGTHS) *$";
	private static final String PRINT_STATS_PATTERN = "^ *(print|PRINT) *(stats|STATS) *$";
	private static final String SEARCH_PATTERN = "^ *(search|SEARCH) *[ACGTacgt]+($)? *$";
	
	/**
	 * Member field for DNATree tree.  This tree represents the
	 * sequences to be stored in memory, with each branch for
	 * one letter of a DNA sequence.  For more information, look
	 * in the DNATree.java file.
	 */
	private static DNATree tree;
	
	/**
	 * Main method to control data flow.  This function takes
	 * the command line parameter as input and uses it to read
	 * from the input file, executing and outputting commands
	 * along the way.
	 * 
	 * @param args
	 * 				the command line arguments
	 */
	public static void main(String[] args) {
				
		// Check for proper usage
		if (args.length != 1) {
			System.out.println("Usage:");
			System.out.println("P2 COMMAND_FILE");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		tree = new DNATree();
		
		// Main command line reading
		try {
			
			// Attempt to open the input file into a buffered reader
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			// Keep reading in commands until we reach the EOF
			String line;
			while ((line = in.readLine()) != null) {
				if (line.matches(INSERT_PATTERN)) {
					
					// Parse out the sequence from the command line
					int index = Math.max(line.indexOf("r"), line.indexOf("R")) + 1;
					String sequence = line.substring(index);
					sequence = sequence.trim();
					
					// Add to tree and output error message if found
					int result = tree.insert(sequence);
					if(result < 0) {
						System.out.println("Sequence " + sequence + " already in tree.");
					} else {
						System.out.println("Sequence " + sequence + " inserted at level " + result + ".");
					}
				} else if (line.matches(REMOVE_PATTERN)) {
					
					// Parse out the sequence from the command line
					int index = Math.max(line.indexOf("e"), line.indexOf("E"));
					String sequence = line.substring(index);
					sequence = sequence.trim();
					
					// Remove from tree and output error message if not found
					if(!tree.remove(sequence)) {
						System.out.println("Sequence " + sequence + " not found in tree.");
					}
				} else if (line.matches(PRINT_PATTERN)) {
					
					// Output the tree
					System.out.println(tree.print(false, false));
				} else if (line.matches(PRINT_LENGTHS_PATTERN)) {
					
					// Output the tree with lengths
					System.out.println(tree.print(true, false));
				} else if (line.matches(PRINT_STATS_PATTERN)) {
					
					// Output the tree with stats
					System.out.println(tree.print(false, true));
				} else if (line.matches(SEARCH_PATTERN)) {
					
					// Parse out the sequence from the command line
					int index = Math.max(line.indexOf("h"), line.indexOf("H"));
					String sequence = line.substring(index);
					sequence = sequence.trim();
					
					// Search the tree and output results
					System.out.println(tree.search(sequence));
				} else {
					continue;
				}
			}
			
			in.close();
		}  catch (FileNotFoundException e) {
			System.out.println("The input file could not be found.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Error reading from file.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Incorrect file formatting.");
			System.exit(0);
		}
	}
}