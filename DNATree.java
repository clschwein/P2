/**
 * DNA Tree class for Project 2.
 * 
 * @author Chris Schweinhart (schwein)
 * @author Nate Kibler (nkibler7)
 */
public class DNATree {

	private DNATreeNode root;

	public DNATree() {
		root = null;
	}

	public int insert(String sequence) {
		if (root == null) {
			root = new LeafNode(sequence);
			return 0;
		}
		if (root instanceof LeafNode) {
			return handleLeafSituation((LeafNode)root, sequence);
		}
		if (root instanceof InternalNode) {
			InternalNode focusNode = null;
			DNATreeNode newFocus = root;
			int pos = 0;
			do {
				focusNode = (InternalNode) newFocus;
				if (pos == sequence.length()) {
					focusNode.addNode(new LeafNode(sequence), 'e');
					return pos + 1;
				}
				newFocus = focusNode.getNode(sequence.charAt(pos));
				pos++;
			}
			while (newFocus != null && newFocus instanceof InternalNode);
			if (newFocus instanceof LeafNode) {
				if (((LeafNode)newFocus).getSequence() == null) {
					focusNode.addNode(new LeafNode(sequence), sequence.charAt(pos));
					return pos + 1;
				}
				//TODO: Implement this case. (Ideally, use handleLeafSituation())
			}
		}
		return -1;
	}

	public boolean remove(String sequence) {

		// TODO Implement remove method

		return false;
	}

	public String print(boolean lengths, boolean stats) {

		// TODO Implement print method

		return "NYI";
	}

	public String search(String pattern) {

		// TODO Implement search method

		return "NYI";
	}
	
	/**
	 * Builds a tree structure from a starting LeafNode root and a String sequence
	 * value that will be added to this tree.
	 * @param root - the LeafNode to build from
	 * @param sequence - the String value sequence to add
	 * @return the level at which the sequence was added
	 */
	private int handleLeafSituation(LeafNode root, String sequence) {
		//TODO: Change return type to both return the resulting root and int level.
		String sequence2 = root.getSequence();
		InternalNode newRoot = new InternalNode();
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

	// TODO Add private, recursive helper methods
}