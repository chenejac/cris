/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

/**
 * Managed bean for getting integer constants of modes
 * 
 * @author chenejac@uns.ac.rs
 */
public class ModesManagedBean {
	
	static int MODE_NONE = 0;
	static int MODE_BROWSE = 1;
	static int MODE_PICK = 2;
	static int MODE_ADD = 3;
	static int MODE_UPDATE = 4;
	static int MODE_VIEW_DETAILS = 5;
	static int MODE_REGISTER = 6;
	static int MODE_UPDATE_USER_BASIC_DATA = 7;
	static int MODE_UPDATE_USER_ADVANCED_DATA = 8;
	static int MODE_CHANGE_PASSWORD = 9;
	static int MODE_ADD_FORMAT_NAME = 10;
	static int MODE_IMPORT = 11;
	static int MODE_UPDATE_REGISTER_ENTRY = 12;
	static int MODE_ADD_REGISTER_ENTRY = 13;
	static int MODE_VIEW_REGISTER_ENTRY = 14;
	static int MODE_ADD_MONOGRAPH_EVALUATION_DATA = 15;

	/**
	 * @return the integer constant which presents mode NONE
	 */
	public int getNone(){
		return ModesManagedBean.MODE_NONE;
	}
	
	/**
	 * @return the integer constant which presents mode browse
	 */
	public int getBrowse(){
		return ModesManagedBean.MODE_BROWSE;
	}
	
	/**
	 * @return the integer constant which presents mode pick
	 */
	public int getPick(){
		return ModesManagedBean.MODE_PICK;
	}
	
	/**
	 * @return the integer constant which presents mode add
	 */
	public int getAdd(){
		return ModesManagedBean.MODE_ADD;
	}
	
	/**
	 * @return the integer constant which presents mode update
	 */
	public int getUpdate(){
		return ModesManagedBean.MODE_UPDATE;
	}
	
	/**
	 * @return the integer constant which presents mode view details
	 */
	public int getViewDetails(){
		return ModesManagedBean.MODE_VIEW_DETAILS;
	}
	
	/**
	 * @return the integer constant which presents mode register
	 */
	public int getRegister(){
		return ModesManagedBean.MODE_REGISTER;
	}
	
	/**
	 * @return the integer constant which presents mode change password
	 */
	public int getChangePassword(){
		return ModesManagedBean.MODE_CHANGE_PASSWORD;
	}
	
	/**
	 * @return the integer constant which presents mode change logged user basic data
	 */
	public int getUpdateUserBasicData(){
		return ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA;
	}
	
	/**
	 * @return the integer constant which presents mode change logged user advanced data
	 */
	public int getUpdateUserAdvancedData(){
		return ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA;
	}
	
	/**
	 * @return the integer constant which presents mode add format name
	 */
	public int getAddFormatName(){
		return ModesManagedBean.MODE_ADD_FORMAT_NAME;
	}
	
	/**
	 * @return the integer constant which presents mode import
	 */
	public int getImport(){
		return ModesManagedBean.MODE_IMPORT;
	}
	
	/**
	 * @return the integer constant which presents mode register entry update
	 */
	public int getUpdateRegisterEntry(){
		return ModesManagedBean.MODE_UPDATE_REGISTER_ENTRY;
	}
	
	/**
	 * @return the integer constant which presents mode register entry add
	 */
	public int getAddRegisterEntry(){
		return ModesManagedBean.MODE_ADD_REGISTER_ENTRY;
	}
	
	/**
	 * @return the integer constant which presents mode register entry view
	 */
	public int getViewRegisterEntry(){
		return ModesManagedBean.MODE_VIEW_REGISTER_ENTRY;
	}
	
	
	public int getAddMonographEvaluationData(){
		return ModesManagedBean.MODE_ADD_MONOGRAPH_EVALUATION_DATA;
	}
}
