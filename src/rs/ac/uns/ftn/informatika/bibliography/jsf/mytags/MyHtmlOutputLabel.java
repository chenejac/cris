package rs.ac.uns.ftn.informatika.bibliography.jsf.mytags;

/*
 * Created on Jul 19, 2004
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class MyHtmlOutputLabel extends UIOutput 
{
	
	public static final String COMPONENT_TYPE = "javax.faces.MyHtmlOutputLabel";
    
	private static final String OPTIMIZED_PACKAGE = "javax.faces.component.";

    public MyHtmlOutputLabel() {
        super();
        setRendererType("javax.faces.Label");
    }

    protected enum PropertyKeys {
        accesskey,
        dir,
        escape,
        forVal("for"),
        lang,
        onblur,
        onclick,
        ondblclick,
        onfocus,
        onkeydown,
        onkeypress,
        onkeyup,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup,
        style,
        realStyle,        
        styleClass,
        realStyleClass,
    	errorStyle,
        errorStyleClass,
        tabindex,
        title,
;
        String toString;
        PropertyKeys(String toString) { this.toString = toString; }
        PropertyKeys() { }
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
}

    /**
     * <p>Return the value of the <code>accesskey</code> property.</p>
     * <p>Contents: Access key that, when pressed, transfers focus
     * to this element.
     */
    public java.lang.String getAccesskey() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.accesskey);

    }

    /**
     * <p>Set the value of the <code>accesskey</code> property.</p>
     */
    public void setAccesskey(java.lang.String accesskey) {
        getStateHelper().put(PropertyKeys.accesskey, accesskey);
        handleAttribute("accesskey", accesskey);
    }


    /**
     * <p>Return the value of the <code>dir</code> property.</p>
     * <p>Contents: Direction indication for text that does not inherit directionality.
     * Valid values are "LTR" (left-to-right) and "RTL" (right-to-left).
     */
    public java.lang.String getDir() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dir);

    }

    /**
     * <p>Set the value of the <code>dir</code> property.</p>
     */
    public void setDir(java.lang.String dir) {
        getStateHelper().put(PropertyKeys.dir, dir);
        handleAttribute("dir", dir);
    }


    /**
     * <p>Return the value of the <code>escape</code> property.</p>
     * <p>Contents: Flag indicating that characters that are sensitive
     * in HTML and XML markup must be escaped.  If omitted, this
     * flag is assumed to be "true".
     */
    public boolean isEscape() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.escape, true);

    }

    /**
     * <p>Set the value of the <code>escape</code> property.</p>
     */
    public void setEscape(boolean escape) {
        getStateHelper().put(PropertyKeys.escape, escape);
    }


    /**
     * <p>Return the value of the <code>for</code> property.</p>
     * <p>Contents: Client identifier of the component for which this element
     * is a label.
     */
    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forVal);

    }

    /**
     * <p>Set the value of the <code>for</code> property.</p>
     */
    public void setFor(java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forVal, _for);
    }


    /**
     * <p>Return the value of the <code>lang</code> property.</p>
     * <p>Contents: Code describing the language used in the generated markup
     * for this component.
     */
    public java.lang.String getLang() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.lang);

    }

    /**
     * <p>Set the value of the <code>lang</code> property.</p>
     */
    public void setLang(java.lang.String lang) {
        getStateHelper().put(PropertyKeys.lang, lang);
        handleAttribute("lang", lang);
    }


    /**
     * <p>Return the value of the <code>onblur</code> property.</p>
     * <p>Contents: Javascript code executed when this element loses focus.
     */
    public java.lang.String getOnblur() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onblur);

    }

    /**
     * <p>Set the value of the <code>onblur</code> property.</p>
     */
    public void setOnblur(java.lang.String onblur) {
        getStateHelper().put(PropertyKeys.onblur, onblur);
        handleAttribute("onblur", onblur);
    }


    /**
     * <p>Return the value of the <code>onclick</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * clicked over this element.
     */
    public java.lang.String getOnclick() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onclick);

    }

    /**
     * <p>Set the value of the <code>onclick</code> property.</p>
     */
    public void setOnclick(java.lang.String onclick) {
        getStateHelper().put(PropertyKeys.onclick, onclick);
        handleAttribute("onclick", onclick);
    }


    /**
     * <p>Return the value of the <code>ondblclick</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * double clicked over this element.
     */
    public java.lang.String getOndblclick() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.ondblclick);

    }

    /**
     * <p>Set the value of the <code>ondblclick</code> property.</p>
     */
    public void setOndblclick(java.lang.String ondblclick) {
        getStateHelper().put(PropertyKeys.ondblclick, ondblclick);
        handleAttribute("ondblclick", ondblclick);
    }


    /**
     * <p>Return the value of the <code>onfocus</code> property.</p>
     * <p>Contents: Javascript code executed when this element receives focus.
     */
    public java.lang.String getOnfocus() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onfocus);

    }

    /**
     * <p>Set the value of the <code>onfocus</code> property.</p>
     */
    public void setOnfocus(java.lang.String onfocus) {
        getStateHelper().put(PropertyKeys.onfocus, onfocus);
        handleAttribute("onfocus", onfocus);
    }


    /**
     * <p>Return the value of the <code>onkeydown</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * pressed down over this element.
     */
    public java.lang.String getOnkeydown() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onkeydown);

    }

    /**
     * <p>Set the value of the <code>onkeydown</code> property.</p>
     */
    public void setOnkeydown(java.lang.String onkeydown) {
        getStateHelper().put(PropertyKeys.onkeydown, onkeydown);
        handleAttribute("onkeydown", onkeydown);
    }


    /**
     * <p>Return the value of the <code>onkeypress</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * pressed and released over this element.
     */
    public java.lang.String getOnkeypress() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onkeypress);

    }

    /**
     * <p>Set the value of the <code>onkeypress</code> property.</p>
     */
    public void setOnkeypress(java.lang.String onkeypress) {
        getStateHelper().put(PropertyKeys.onkeypress, onkeypress);
        handleAttribute("onkeypress", onkeypress);
    }


    /**
     * <p>Return the value of the <code>onkeyup</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * released over this element.
     */
    public java.lang.String getOnkeyup() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onkeyup);

    }

    /**
     * <p>Set the value of the <code>onkeyup</code> property.</p>
     */
    public void setOnkeyup(java.lang.String onkeyup) {
        getStateHelper().put(PropertyKeys.onkeyup, onkeyup);
        handleAttribute("onkeyup", onkeyup);
    }


    /**
     * <p>Return the value of the <code>onmousedown</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * pressed down over this element.
     */
    public java.lang.String getOnmousedown() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onmousedown);

    }

    /**
     * <p>Set the value of the <code>onmousedown</code> property.</p>
     */
    public void setOnmousedown(java.lang.String onmousedown) {
        getStateHelper().put(PropertyKeys.onmousedown, onmousedown);
        handleAttribute("onmousedown", onmousedown);
    }


    /**
     * <p>Return the value of the <code>onmousemove</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved within this element.
     */
    public java.lang.String getOnmousemove() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onmousemove);

    }

    /**
     * <p>Set the value of the <code>onmousemove</code> property.</p>
     */
    public void setOnmousemove(java.lang.String onmousemove) {
        getStateHelper().put(PropertyKeys.onmousemove, onmousemove);
        handleAttribute("onmousemove", onmousemove);
    }


    /**
     * <p>Return the value of the <code>onmouseout</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved away from this element.
     */
    public java.lang.String getOnmouseout() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onmouseout);

    }

    /**
     * <p>Set the value of the <code>onmouseout</code> property.</p>
     */
    public void setOnmouseout(java.lang.String onmouseout) {
        getStateHelper().put(PropertyKeys.onmouseout, onmouseout);
        handleAttribute("onmouseout", onmouseout);
    }


    /**
     * <p>Return the value of the <code>onmouseover</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved onto this element.
     */
    public java.lang.String getOnmouseover() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onmouseover);

    }

    /**
     * <p>Set the value of the <code>onmouseover</code> property.</p>
     */
    public void setOnmouseover(java.lang.String onmouseover) {
        getStateHelper().put(PropertyKeys.onmouseover, onmouseover);
        handleAttribute("onmouseover", onmouseover);
    }


    /**
     * <p>Return the value of the <code>onmouseup</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * released over this element.
     */
    public java.lang.String getOnmouseup() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onmouseup);

    }

    /**
     * <p>Set the value of the <code>onmouseup</code> property.</p>
     */
    public void setOnmouseup(java.lang.String onmouseup) {
        getStateHelper().put(PropertyKeys.onmouseup, onmouseup);
        handleAttribute("onmouseup", onmouseup);
    }


    /**
     * <p>Return the value of the <code>style</code> property.</p>
     * <p>Contents: CSS style(s) to be applied when this component is rendered.
     */
    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style);

    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(java.lang.String style) {
        getStateHelper().put(PropertyKeys.style, style);
        handleAttribute("style", style);
    }


    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) to be applied when
     * this element is rendered.  This value must be passed through
     * as the "class" attribute on generated markup.
     */
    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass);

    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(java.lang.String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }
    
    public String getErrorStyle()
    {
    	 return (java.lang.String) getStateHelper().eval(PropertyKeys.errorStyle);
    }

    public void setErrorStyle(String errorStyle)
    {
    	getStateHelper().put(PropertyKeys.errorStyle, errorStyle);
    }

    public String getErrorStyleClass()
    {
    	return (java.lang.String) getStateHelper().eval(PropertyKeys.errorStyleClass);
    }

    public void setErrorStyleClass(String errorStyleClass)
    {
    	getStateHelper().put(PropertyKeys.errorStyleClass, errorStyleClass);
    }
    
    public String getRealStyle()
    {
    	 return (java.lang.String) getStateHelper().eval(PropertyKeys.realStyle);
    }

    public void setRealStyle(String realStyle)
    {
    	getStateHelper().put(PropertyKeys.realStyle, realStyle);
    }

    public String getRealStyleClass()
    {
    	return (java.lang.String) getStateHelper().eval(PropertyKeys.realStyleClass);
    }

    public void setRealStyleClass(String realStyleClass)
    {
    	getStateHelper().put(PropertyKeys.realStyleClass, realStyleClass);
    }


    /**
     * <p>Return the value of the <code>tabindex</code> property.</p>
     * <p>Contents: Position of this element in the tabbing order
     * for the current document.  This value must be
     * an integer between 0 and 32767.
     */
    public java.lang.String getTabindex() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tabindex);

    }

    /**
     * <p>Set the value of the <code>tabindex</code> property.</p>
     */
    public void setTabindex(java.lang.String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
        handleAttribute("tabindex", tabindex);
    }


    /**
     * <p>Return the value of the <code>title</code> property.</p>
     * <p>Contents: Advisory title information about markup elements generated
     * for this component.
     */
    public java.lang.String getTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.title);

    }

    /**
     * <p>Set the value of the <code>title</code> property.</p>
     */
    public void setTitle(java.lang.String title) {
        getStateHelper().put(PropertyKeys.title, title);
        handleAttribute("title", title);
    }


    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("blur","click","dblclick","focus","keydown","keypress","keyup","mousedown","mousemove","mouseout","mouseover","mouseup"));

    public Collection<String> getEventNames() {
        return EVENT_NAMES;    }


    public String getDefaultEventName() {
        return null;    }


    @SuppressWarnings("unchecked")
	private void handleAttribute(String name, Object value) {
        List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                }
            } else if (!setAttributes.contains(name)) {
                setAttributes.add(name);
            }
        }
    } 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	

    
    	
	

//    public Object saveState(FacesContext _context)
//    {
//        Object _values[] = new Object[27];
//        _values[0] = super.saveState(_context);
//        _values[1] = accesskey;
//        _values[2] = dir;
//        _values[3] = escape ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE));
//        _values[4] = escape_set ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE));
//        _values[5] = _for;
//        _values[6] = lang;
//        _values[7] = onblur;
//        _values[8] = onclick;
//        _values[9] = ondblclick;
//        _values[10] = onfocus;
//        _values[11] = onkeydown;
//        _values[12] = onkeypress;
//        _values[13] = onkeyup;
//        _values[14] = onmousedown;
//        _values[15] = onmousemove;
//        _values[16] = onmouseout;
//        _values[17] = onmouseover;
//        _values[18] = onmouseup;
//        _values[19] = style;
//        _values[20] = styleClass;
//        _values[21] = realStyle;
//        _values[22] = realStyleClass;
//		_values[23] = errorStyle;
//        _values[24] = errorStyleClass;
//        _values[25] = tabindex;
//        _values[26] = title;
//        return ((Object) (_values));
//    }
//
//    public void restoreState(FacesContext _context, Object _state)
//    {
//        Object _values[] = (Object[])(Object[])_state;
//        super.restoreState(_context, _values[0]);
//        accesskey = (String)_values[1];
//        dir = (String)_values[2];
//        escape = ((Boolean)_values[3]).booleanValue();
//        escape_set = ((Boolean)_values[4]).booleanValue();
//        _for = (String)_values[5];
//        lang = (String)_values[6];
//        onblur = (String)_values[7];
//        onclick = (String)_values[8];
//        ondblclick = (String)_values[9];
//        onfocus = (String)_values[10];
//        onkeydown = (String)_values[11];
//        onkeypress = (String)_values[12];
//        onkeyup = (String)_values[13];
//        onmousedown = (String)_values[14];
//        onmousemove = (String)_values[15];
//        onmouseout = (String)_values[16];
//        onmouseover = (String)_values[17];
//        onmouseup = (String)_values[18];
//        style = (String)_values[19];
//        styleClass = (String)_values[20];
//        realStyle = (String)_values[21];
//        realStyleClass = (String)_values[22];
//		errorStyle = (String)_values[23];
//        errorStyleClass = (String)_values[24];
//        tabindex = (String)_values[25];
//        title = (String)_values[26];
//    }

    
	
	/**
     * @see javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
    	String style = getStyle();
    	String styleClass = getStyleClass();
    	String errorStyle = getErrorStyle();
    	String errorStyleClass = getErrorStyleClass();
    	String realStyle = getRealStyle();
    	String realStyleClass = getRealStyleClass();
    	if(isError(context)){
    		if ((errorStyle != null) && (errorStyle.trim().length()>0)){ 
	    		if((style!=null) && (!(style.equals(realStyle)))){
	    			setRealStyle(style);
	    		}
	    		setStyle(errorStyle);
    		}
    		if ((errorStyleClass != null) && (errorStyleClass.trim().length()>0)){ 
	    		if((styleClass!=null) && (!(styleClass.equals(realStyleClass)))){
	    			setRealStyleClass(styleClass);
	    		}
	    		setStyleClass(errorStyleClass);
    		}
    	} else {
    		if ((style != null) && (style.trim().length()>0)){ 
	    		setStyle(realStyle);
	    	}
    		if ((styleClass != null) && (styleClass.trim().length()>0)){ 
    			setStyleClass(realStyleClass);
    		}
    	}
    	super.encodeBegin(context);
    }
	
	private boolean isError(FacesContext context) {
        /* find form */
        UIComponent parent = this.getParent();
        while (!(parent instanceof UIForm || parent instanceof UIViewRoot)) {
            parent = parent.getParent();
            if (parent == null) {
                break;
            }
        }

        String compId=null;
        String clientId = this.getClientId(context);
        if (clientId.contains(":")) {
            String[] comps = clientId.split(":");
            compId = comps[0] + ":" + this.getFor();
        } else {
            compId = parent.getId() + ":" + this.getFor();
        }
        Iterator<FacesMessage> messages = context.getMessages(compId);
        if (messages == null) {
            return false;
        }
        boolean hasError = messages.hasNext();
        return hasError;
    }
}