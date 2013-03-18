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

	public int insert(String sequence) {
		
		// Check if the root is null (empty tree)
		if (root instanceof FlyweightNode) {
			root = new LeafNode(sequence, 0);
			return 0;
		}
		
		// Check if there is only one node in the tree
		if (root instanceof LeafNode) {
			return handleLeafSituation((LeafNode)root, sequence);
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
	private int handleLeafSituation(LeafNode root, String sequence) {
		String sequence2 = root.getSequence();
		InternalNode newRoot = new InternalNode(fw, 0);
		InternalNode focusNode = newRoot;

		int i;
		for (i = 0; (i < sequence.length() || i < sequence2.length()); i++) {
			if (i == sequence.length()) {
				focusNode.addNode(new LeafNode(sequence2), sequence2.charAt(i));
				focusNode.addNode(new LeafNode(sequence), 'e');
				break;
			}
			if (i == sequence2.length()) {
				focusNode.addNode(new LeafNode(sequence), sequence.charAt(i));
				focusNode.addNode(new LeafNode(sequence2), 'e');
				break;
			}
			if (sequence.charAt(i) != sequence2.charAt(i)) {
				focusNode.addNode(new LeafNode(sequence), sequence.charAt(i));
				focusNode.addNode(new LeafNode(sequence2), sequence2.charAt(i));
				break;
			}
			InternalNode newFocus = new InternalNode();
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
				
			}
		}
	}

	public boolean remove(String sequence) {

		InternalNode node = find(sequence);
		
		if (node == null) {
			return false;
		}
		
		

		return true;
	}

	public String print(boolean lengths, boolean stats) {

		// TODO Implement print method

		return "NYI";
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
			return "Cannot search on empty tree.";
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