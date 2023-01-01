package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;



/**
 * Abstract class that holds manipulation logic for rich tree object
 * 
 * 
 */
@SuppressWarnings("serial")
public class TreeNodeDTO <TreeNodeObjectType> implements Serializable, TreeNode{

	protected boolean checkbox_state = false;
	public String [] tree_stateType = { "empty", "full", "partial"};
	protected String tree_state = "empty";
	
	protected TreeNodeObjectType element;
	protected String type ="";
	protected boolean expanded = false;
	
	private TreeNodeDTO <TreeNodeObjectType> parent = null;
	private List<TreeNodeDTO <TreeNodeObjectType>> children = new ArrayList<TreeNodeDTO <TreeNodeObjectType>>();
	
	
	public TreeNodeDTO(TreeNodeObjectType element)
	{
		this.element=element;
		type = element.getClass().getSimpleName();
	}
	
	
	/**
	 * @return the element
	 */
	public TreeNodeObjectType getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(TreeNodeObjectType element) {
		this.element = element;
	}
	/**
	 * @return the element class type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	public void setTree_state(String treeState) {
		tree_state = treeState;
	}

	public String getTree_state() {
		return tree_state;
	}

	public String[] getTree_stateType() {
		return tree_stateType;
	}

	public void setTree_stateType(String[] treeStateType) {
		tree_stateType = treeStateType;
	}

	public boolean isCheckbox_state() {
		return checkbox_state;
	}

	public void setCheckbox_state(boolean checkbox_state) {
		this.checkbox_state = checkbox_state;
	}
	
	public String toString()
	{
		return this.element.toString();
	}

	public String getLocalizedString(String language)
	{
		if (type.equals("OrganizationUnitDTO")) {
			return ((OrganizationUnitDTO) this.element).getLocalizedString(language);
		}else if (type.equals("InstitutionDTO")) {
			return ((InstitutionDTO) this.element).getLocalizedString(language);
		}else if (type.equals("ClassDTO")) {
			return ((ClassDTO) this.element).getLocalizedString(language);
		} else
			return this.element.toString();
	}
	
	public boolean equals(Object o)
	{
		return this.element.equals(o);
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	
	public boolean addChild(TreeNodeDTO <TreeNodeObjectType> child){
		return children.add(child);
	}
	
	public List<TreeNodeDTO <TreeNodeObjectType>> getChildren()
	{
		return children;
	}
	
	@Override
	public Enumeration<TreeNodeDTO <TreeNodeObjectType>> children() {
		return Iterators.asEnumeration(children.iterator());
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}


	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		 return children.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	public void setParent(TreeNodeDTO<TreeNodeObjectType> parent) 
	{
		this.parent = parent;
	}
	
	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}


	
	
}
