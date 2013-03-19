import java.text.DecimalFormat;

/**
 * DNA Tree class for Project 2.
 * 
 * @author Chris Schweinhart (schwein)
 * @author Nate Kibler (nkibler7)
 */
public class DNATree {

	private DNATreeNode root;
	private FlyweightNode fw;

	public DNATree() {
		fw = new FlyweightNode();
		root = fw;
	}


	public int insert(String sequence) 
	{
		// Check if the root is null (empty tree)
		if (root instanceof FlyweightNode) {
			root = new LeafNode(sequence, 0);
			return 0;
		}
		// Check if there is only one node in the tree
		if (root instanceof LeafNode) {
			return handleLeafSituation(sequence);
		}
		return insert(sequence, (InternalNode)root);
	}

	/**
	 * Builds a tree structure from a starting LeafNode root and a String sequence
	 * value that will be added to this tree.
	 * @param root - the LeafNode to build from
	 * @param sequence - the String value sequence to add
	 * @return the level at which the sequence was added
	 */
	private int handleLeafSituation(String sequence) {
		String sequence2 = ((LeafNode) root).getSequence();
		root = new InternalNode(fw, 0);
		InternalNode focusNode = (InternalNode) root;

		int i;
		for (i = 0; (i < sequence.length() || i < sequence2.length()); i++) {
			if (i == sequence.length()) {
				focusNode.addNode(new LeafNode(sequence2, i + 1), sequence2.charAt(i));
				focusNode.addNode(new LeafNode(sequence, i + 1), 'e');
				break;
			}
			if (i == sequence2.length()) {
				focusNode.addNode(new LeafNode(sequence, i + 1), sequence.charAt(i));
				focusNode.addNode(new LeafNode(sequence2, i + 1), 'e');
				break;
			}
			if (sequence.charAt(i) != sequence2.charAt(i)) {
				focusNode.addNode(new LeafNode(sequence, i + 1), sequence.charAt(i));
				focusNode.addNode(new LeafNode(sequence2, i + 1), sequence2.charAt(i));
				break;
			}
			InternalNode newFocus = new InternalNode(fw, i);
			focusNode.addNode(newFocus, sequence.charAt(i));
			focusNode = newFocus;
		}
		return i + 1;
	}

	private int insert(String sequence, InternalNode node)
	{
		DNATreeNode child = node.getNode(sequence.charAt(node.getLevel()));

		if (child instanceof FlyweightNode) {
			node.addNode(new LeafNode(sequence, node.getLevel() + 1), sequence.charAt(node.getLevel()));
			return node.getLevel() + 1;
		}

		if (child instanceof LeafNode) {
			return replaceNodeWithTree(node, sequence);
		}

		return insert(sequence, (InternalNode)child);
	}

	/**
	 * Private helper method for the insert operation.  Will build a tree at
	 * the specified node to replace the leaf node in question with a tree.
	 * Depending on how many characters the sequence and the leaf node share,
	 * the height of the tree may differ.
	 * 
	 * @param node - the internal node above the leaf node
	 * @param sequence - the string sequence for DNA
	 * @return the level of the new inserted node
	 */
	private int replaceNodeWithTree(InternalNode node, String sequence)
	{
		LeafNode save = (LeafNode)node.getNode(sequence.charAt(node.getLevel()));
		String pattern = save.getSequence();
		int count = save.getLevel();

		while (count < sequence.length() && count < pattern.length()) {
			if (pattern.charAt(count) == sequence.charAt(count)) {
				InternalNode temp = new InternalNode(fw, count);
				node.addNode(temp, sequence.charAt(count));
				node = temp;
				count++;
			} else {
				node.addNode(save, pattern.charAt(count));
				node.addNode(new LeafNode(sequence, count), sequence.charAt(count));
				return count;
			}
		}
		node.addNode(save, pattern.charAt(count));
		node.addNode(new LeafNode(sequence, count), sequence.charAt(count));
		return count;
	}

	public boolean remove(String sequence) {
		if (root instanceof FlyweightNode) {
			return false;
		}
		if (root instanceof LeafNode) {
			if (((LeafNode) root).getSequence().equals(sequence)) {
				root = fw;
				return true;
			}
			return false;
		}
		return findAndRemove(sequence, (InternalNode) root);
	}

	private boolean findAndRemove(String sequence, InternalNode focus) {
		char character = sequence.isEmpty() ? 'e' : sequence.charAt(0);
		DNATreeNode nextNode = focus.getNode(character);
		if (nextNode instanceof FlyweightNode) {
			return false;
		}
		if (nextNode instanceof LeafNode) {
			if (((LeafNode) nextNode).getSequence().equals(sequence)) {
				focus.addNode(fw, character);
				return true;
			}
			return false;
		}
		if (findAndRemove(sequence.substring(1), (InternalNode) nextNode)) {
			if (((InternalNode)nextNode).getNumFlyNodes() == 4) {
				char[] chars = {'a', 'c', 'g', 't', 'e'};
				for (char currentChar: chars) {
					DNATreeNode node = ((InternalNode)nextNode).getNode(currentChar);
					if (node instanceof LeafNode) {
						focus.addNode(node, character);
						return true;
					}
				}
			}
			return true;
		}
		return false;
	}


	/**
	 * Method to print out various things about a tree.  Will always
	 * give the basic structure via indentation and I/E/sequences.
	 * Flags are for extra options, such as sequence lengths or
	 * letter statistics.
	 * @param lengths - whether or not to print sequence lengths
	 * @param stats - whether or not to print sequence statistics
	 * @return the print for the entire tree
	 */
	public String print(boolean lengths, boolean stats) {

		if (root instanceof FlyweightNode) {
			return "Print called on empty tree.";
		}
		
		return print(root, null, lengths, stats);
	}

	/**
	 * Helper method for the print method.  Will recursively perform
	 * a preorder traversal, hitting all the nodes to print them.
	 * @param node - the current node to print
	 * @param lengths - whether or not to print sequence lengths
	 * @param stats - whether or not to print sequence statistics
	 * @return the print for this node and any children
	 */
	private String print(DNATreeNode node, DNATreeNode parent, boolean lengths, boolean stats) {

		String output = "\n";

		// Determine node level
		int level;
		if (node instanceof FlyweightNode) {
			level = parent.getLevel() + 1;
		} else {
			level = node.getLevel();
		}
		
		// Add indentation for node level
		for (int i = 0; i < level; i++) {
			output += "  ";
		}

		// Case breakdown
		if (node instanceof FlyweightNode) {
			output += "E";
		} else if(node instanceof LeafNode) {
			String sequence = ((LeafNode)node).getSequence();
			output += sequence;

			// Handle print lengths command
			if (lengths) {
				output += ": length " + sequence.length();
			}

			// Handle print stats command
			if (stats) {
				int[] letters = new int[4];
				double[] frequencies = new double[4];
				
				for (int i = 0; i < 4; i++) {
					letters[i] = 0;
				}

				for (char c : sequence.toLowerCase().toCharArray()) {
					if (c == 'a') {
						letters[0]++;
					} else if (c == 'c') {
						letters[1]++;
					} else if (c == 'g') {
						letters[2]++;
					} else if (c == 't') {
						letters[3]++;
					}
				}

				for (int i = 0; i < 4; i++) {
					frequencies[i] = 100.0 * letters[i] / sequence.length();
				}


				DecimalFormat decim = new DecimalFormat("0.00");

				output += ": ";
				output += "A(" + decim.format(frequencies[0]) + "), ";
				output += "C(" + decim.format(frequencies[1]) + "), ";
				output += "G(" + decim.format(frequencies[2]) + "), ";
				output += "T(" + decim.format(frequencies[3]) + ")";
			}
		} else {
			output += "I";

			// Recursive calls to children
			output += print(((InternalNode)node).getNode('a'), node, lengths, stats);
			output += print(((InternalNode)node).getNode('c'), node, lengths, stats);
			output += print(((InternalNode)node).getNode('g'), node, lengths, stats);
			output += print(((InternalNode)node).getNode('t'), node, lengths, stats);
			output += print(((InternalNode)node).getNode('e'), node, lengths, stats);
		}

		return output;
	}

	/**
	 * Method to search the tree for a particular pattern.  There
	 * are two types of patterns allowed: (1) prefix search, and
	 * (2) exact matching.  Prefix will return all sequences with a
	 * given prefix, and exact matching will print the exact sequence
	 * if found in the tree.
	 * @param pattern - the search pattern to look for
	 * @return the number of nodes visited and search results
	 */
	public String search(String pattern) {

		// Check for special root case
		if (root instanceof FlyweightNode) {
			return "Search called on empty tree.";
		}

		String output = "\n";
		int[] visited = new int[1];
		visited[0] = 1;
		boolean exact;

		// Check for exact searching or prefix searching
		if(pattern.charAt(pattern.length() - 1) == '$') {
			exact = true;
			pattern = pattern.substring(0, pattern.length() - 1);
		} else {
			exact = false;
		}

		// Check for only one node
		if (root instanceof LeafNode) {
			String sequence = ((LeafNode)root).getSequence();
			if (!exact && sequence.substring(0, pattern.length()).equals(pattern)) {
				output += "Sequence: " + pattern;
			} else if (exact && sequence.equals(pattern)) {
				output += "Sequence: " + pattern;
			} else {
				output += "No sequence found";
			}
		} else {
			// Search for the closest parent node for our pattern
			int count = 0;
			InternalNode focus = (InternalNode)root;
			while (count < pattern.length() - 1) {
				if (focus.getNode(pattern.charAt(count)) instanceof InternalNode) {
					focus = (InternalNode)focus.getNode(pattern.charAt(count));
				} else {
					break;
				}
				count ++;
				visited[0]++;
			}

			if (exact) {
				if (count == pattern.length() - 1 && focus.getNode(pattern.charAt(count + 1)) instanceof LeafNode) {
					output += "Sequence: " + ((LeafNode)focus.getNode(pattern.charAt(count + 1))).getSequence();
				} else {
					output += "No sequence found";
				}
			} else {
				if (count == pattern.length() - 1 && focus.getNode(pattern.charAt(count + 1)) instanceof LeafNode) {
					output += "Sequence: " + ((LeafNode)focus.getNode(pattern.charAt(count + 1))).getSequence();
				} else if (count == pattern.length() - 1 && focus.getNode(pattern.charAt(count + 1)) instanceof InternalNode) {
					output += printAllLeafNodes((InternalNode)focus.getNode(pattern.charAt(count + 1)), visited);
				} else {
					output += "No sequence found";
				}
			}
		}

		return "Number of nodes visited: " + visited[0] + output;
	}

	/**
	 * Helper method for printing all of the nodes in a tree.
	 * @param node - the root of the tree
	 * @param visited - an int counter for the nodes visited
	 * @return an output string for all the sequences
	 */
	private String printAllLeafNodes(InternalNode node, int[] visited) {

		visited[0]++;
		String output = "";

		if (node.getNode('a') instanceof LeafNode) {
			output += "Sequence: " + ((LeafNode)node.getNode('a')).getSequence() + "\n";
			visited[0]++;
		} else if (node.getNode('a') instanceof InternalNode) {
			output += printAllLeafNodes((InternalNode)node.getNode('a'), visited);
		} else {
			visited[0]++;
		}

		if (node.getNode('c') instanceof LeafNode) {
			output += "Sequence: " + ((LeafNode)node.getNode('c')).getSequence() + "\n";
			visited[0]++;
		} else if (node.getNode('c') instanceof InternalNode) {
			output += printAllLeafNodes((InternalNode)node.getNode('c'), visited);
		} else {
			visited[0]++;
		}

		if (node.getNode('g') instanceof LeafNode) {
			output += "Sequence: " + ((LeafNode)node.getNode('g')).getSequence() + "\n";
			visited[0]++;
		} else if (node.getNode('g') instanceof InternalNode) {
			output += printAllLeafNodes((InternalNode)node.getNode('g'), visited);
		} else {
			visited[0]++;
		}

		if (node.getNode('t') instanceof LeafNode) {
			output += "Sequence: " + ((LeafNode)node.getNode('t')).getSequence() + "\n";
			visited[0]++;
		} else if (node.getNode('t') instanceof InternalNode) {
			output += printAllLeafNodes((InternalNode)node.getNode('t'), visited);
		} else {
			visited[0]++;
		}

		if (node.getNode('e') instanceof LeafNode) {
			output += "Sequence: " + ((LeafNode)node.getNode('a')).getSequence() + "\n";
		}

		visited[0]++;

		return output;
	}
}