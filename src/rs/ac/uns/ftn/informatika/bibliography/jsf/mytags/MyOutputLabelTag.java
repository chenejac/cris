package rs.ac.uns.ftn.informatika.bibliography.jsf.mytags;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

public final class MyOutputLabelTag extends UIComponentELTag
{
  
  // Setter Methods
  // PROPERTY: converter
  private javax.el.ValueExpression converter;
  public void setConverter(javax.el.ValueExpression converter) {
      this.converter = converter;
  }

  // PROPERTY: value
  private javax.el.ValueExpression value;
  public void setValue(javax.el.ValueExpression value) {
      this.value = value;
  }

  // PROPERTY: accesskey
  private javax.el.ValueExpression accesskey;
  public void setAccesskey(javax.el.ValueExpression accesskey) {
      this.accesskey = accesskey;
  }

  // PROPERTY: dir
  private javax.el.ValueExpression dir;
  public void setDir(javax.el.ValueExpression dir) {
      this.dir = dir;
  }

  // PROPERTY: escape
  private javax.el.ValueExpression escape;
  public void setEscape(javax.el.ValueExpression escape) {
      this.escape = escape;
  }

  // PROPERTY: for
  private javax.el.ValueExpression _for;
  public void setFor(javax.el.ValueExpression _for) {
      this._for = _for;
  }

  // PROPERTY: lang
  private javax.el.ValueExpression lang;
  public void setLang(javax.el.ValueExpression lang) {
      this.lang = lang;
  }

  // PROPERTY: onblur
  private javax.el.ValueExpression onblur;
  public void setOnblur(javax.el.ValueExpression onblur) {
      this.onblur = onblur;
  }

  // PROPERTY: onclick
  private javax.el.ValueExpression onclick;
  public void setOnclick(javax.el.ValueExpression onclick) {
      this.onclick = onclick;
  }

  // PROPERTY: ondblclick
  private javax.el.ValueExpression ondblclick;
  public void setOndblclick(javax.el.ValueExpression ondblclick) {
      this.ondblclick = ondblclick;
  }

  // PROPERTY: onfocus
  private javax.el.ValueExpression onfocus;
  public void setOnfocus(javax.el.ValueExpression onfocus) {
      this.onfocus = onfocus;
  }

  // PROPERTY: onkeydown
  private javax.el.ValueExpression onkeydown;
  public void setOnkeydown(javax.el.ValueExpression onkeydown) {
      this.onkeydown = onkeydown;
  }

  // PROPERTY: onkeypress
  private javax.el.ValueExpression onkeypress;
  public void setOnkeypress(javax.el.ValueExpression onkeypress) {
      this.onkeypress = onkeypress;
  }

  // PROPERTY: onkeyup
  private javax.el.ValueExpression onkeyup;
  public void setOnkeyup(javax.el.ValueExpression onkeyup) {
      this.onkeyup = onkeyup;
  }

  // PROPERTY: onmousedown
  private javax.el.ValueExpression onmousedown;
  public void setOnmousedown(javax.el.ValueExpression onmousedown) {
      this.onmousedown = onmousedown;
  }

  // PROPERTY: onmousemove
  private javax.el.ValueExpression onmousemove;
  public void setOnmousemove(javax.el.ValueExpression onmousemove) {
      this.onmousemove = onmousemove;
  }

  // PROPERTY: onmouseout
  private javax.el.ValueExpression onmouseout;
  public void setOnmouseout(javax.el.ValueExpression onmouseout) {
      this.onmouseout = onmouseout;
  }

  // PROPERTY: onmouseover
  private javax.el.ValueExpression onmouseover;
  public void setOnmouseover(javax.el.ValueExpression onmouseover) {
      this.onmouseover = onmouseover;
  }

  // PROPERTY: onmouseup
  private javax.el.ValueExpression onmouseup;
  public void setOnmouseup(javax.el.ValueExpression onmouseup) {
      this.onmouseup = onmouseup;
  }

  // PROPERTY: style
  private javax.el.ValueExpression style;
  public void setStyle(javax.el.ValueExpression style) {
      this.style = style;
  }

  // PROPERTY: styleClass
  private javax.el.ValueExpression styleClass;
  public void setStyleClass(javax.el.ValueExpression styleClass) {
      this.styleClass = styleClass;
  }

  // PROPERTY: tabindex
  private javax.el.ValueExpression tabindex;
  public void setTabindex(javax.el.ValueExpression tabindex) {
      this.tabindex = tabindex;
  }

  // PROPERTY: title
  private javax.el.ValueExpression title;
  public void setTitle(javax.el.ValueExpression title) {
      this.title = title;
  }

  // PROPERTY: errorStyle
  private ValueExpression errorStyle;
  public void setErrorStyle(ValueExpression errorStyle)
  {
    this.errorStyle = errorStyle;
  }
  
  // PROPERTY: errorStyleClass
  private ValueExpression errorStyleClass;
  public void setErrorStyleClass(ValueExpression errorStyleClass)
  {
    this.errorStyleClass = errorStyleClass;
  }
  

 

 

  

  
  //General Methods
  public String getComponentType() {
	return "javax.faces.MyHtmlOutputLabel";	
  }

  public String getRendererType() {
	return "javax.faces.Label";	
  }
  
  protected void setProperties(UIComponent component) {
      super.setProperties(component);
      javax.faces.component.UIOutput output = null;
      try {
          output = (javax.faces.component.UIOutput) component;
      } catch (ClassCastException cce) {
          throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UIOutput.  Perhaps you're missing a tag?");
      }

      if (converter != null) {
          if (!converter.isLiteralText()) {
              output.setValueExpression("converter", converter);
          } else {
              Converter conv = FacesContext.getCurrentInstance().getApplication().createConverter(converter.getExpressionString());
              output.setConverter(conv);
          }
      }

      if (value != null) {
          output.setValueExpression("value", value);
      }
      if (accesskey != null) {
          output.setValueExpression("accesskey", accesskey);
      }
      if (dir != null) {
          output.setValueExpression("dir", dir);
      }
      if (escape != null) {
          output.setValueExpression("escape", escape);
      }
      if (_for != null) {
          output.setValueExpression("for", _for);
      }
      if (lang != null) {
          output.setValueExpression("lang", lang);
      }
      if (onblur != null) {
          output.setValueExpression("onblur", onblur);
      }
      if (onclick != null) {
          output.setValueExpression("onclick", onclick);
      }
      if (ondblclick != null) {
          output.setValueExpression("ondblclick", ondblclick);
      }
      if (onfocus != null) {
          output.setValueExpression("onfocus", onfocus);
      }
      if (onkeydown != null) {
          output.setValueExpression("onkeydown", onkeydown);
      }
      if (onkeypress != null) {
          output.setValueExpression("onkeypress", onkeypress);
      }
      if (onkeyup != null) {
          output.setValueExpression("onkeyup", onkeyup);
      }
      if (onmousedown != null) {
          output.setValueExpression("onmousedown", onmousedown);
      }
      if (onmousemove != null) {
          output.setValueExpression("onmousemove", onmousemove);
      }
      if (onmouseout != null) {
          output.setValueExpression("onmouseout", onmouseout);
      }
      if (onmouseover != null) {
          output.setValueExpression("onmouseover", onmouseover);
      }
      if (onmouseup != null) {
          output.setValueExpression("onmouseup", onmouseup);
      }
      if (style != null) {
          output.setValueExpression("style", style);
      }
      if (styleClass != null) {
          output.setValueExpression("styleClass", styleClass);
      }
      
      if (this.errorStyle != null) {
	        output.setValueExpression("errorStyle", this.errorStyle);
	  }
	    
      if (this.errorStyleClass != null) {
	        output.setValueExpression("errorStyleClass", this.errorStyleClass);
	  }
      
      if (tabindex != null) {
          output.setValueExpression("tabindex", tabindex);
      }
      if (title != null) {
          output.setValueExpression("title", title);
      }
  }
  // Methods From TagSupport
  public int doStartTag() throws JspException {
      try {
          return super.doStartTag();
      } catch (Exception e) {
          Throwable root = e;
          while (root.getCause() != null) {
              root = root.getCause();
          }
          throw new JspException(root);
      }
  }

  public int doEndTag() throws JspException {
      try {
          return super.doEndTag();
      } catch (Exception e) {
          Throwable root = e;
          while (root.getCause() != null) {
              root = root.getCause();
          }
          throw new JspException(root);
      }
  }
  

  public void release()
  {
    super.release();

    this.converter = null;
    this.value = null;

    this.accesskey = null;
    this.dir = null;
    this.escape = null;
    this._for = null;
    this.lang = null;
    this.onblur = null;
    this.onclick = null;
    this.ondblclick = null;
    this.onfocus = null;
    this.onkeydown = null;
    this.onkeypress = null;
    this.onkeyup = null;
    this.onmousedown = null;
    this.onmousemove = null;
    this.onmouseout = null;
    this.onmouseover = null;
    this.onmouseup = null;
    this.style = null;
    this.styleClass = null;
	this.errorStyle = null;
    this.errorStyleClass = null;
    this.tabindex = null;
    this.title = null;
  }

  public String getDebugString() {
    String result = "id: " + getId() + " class: " + super.getClass().getName();
    return result;
  }
}