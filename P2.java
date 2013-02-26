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
 * @author Chris Schweinhart (schwein)
 * @author Nate Kibler (nkibler7)
 */
public class P2 {

	// TODO Add member fields
	
	public static void main(String[] args) {
				
		// Check for proper usage
		if (args.length != 1) {
			System.out.println("Usage:");
			System.out.println("P2 COMMAND_FILE");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		// Main command line reading
		try {
			
			// Attempt to open the input file into a buffered reader
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line;
			
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			
			System.out.println("EOF");
			
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
		
		// TODO Add file handling and reading
		
		// TODO Add RegEx checks for commands
	}
	
	// TODO Add private helper methods
}