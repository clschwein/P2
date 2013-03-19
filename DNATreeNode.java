/**
 * This class represents a general node in the DNA tree.  It is
 * abstract, and so cannot ever be instantiated.  To see more
 * specific uses of child nodes, see their own files.
 *
 */
public abstract class DNATreeNode {
	/**
	 * Represents the level of the node.  Used for print string formating.
	 */
	private int level;
	
	/**
	 * Returns the level of the node.
	 * 
	 * @return - the level of the node
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Sets the level of the node to a new value.
	 * 
	 * @param l - the new level for the node
	 */
	public void setLevel(int l)
	{
		level = l;
	}
}