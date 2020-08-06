		
	window.onbeforeunload = unloadPage;
	
	function unloadPage()
	{;
	}
	

	function rowSelect(row){
		var tabela = row.parentNode;
		var tt = tabela.rows;
		for(var i=0; i<tt.length; i++){
			var rr = tt[i];
			var color = "white";
			if(rr == row){
				color = "#A1A1A1";
			}
			var columns = tt[i].getElementsByTagName("td");
			for(var j=0; j<columns.length; j++){
				columns[j].style.backgroundColor=color;
			};
		};
	}

	function confirmDecision(){
		if(!confirm('Brisanje! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	function confirmDecisionSkip(){
		if(!confirm('Ovom opcijom preskacete uvoz podataka aktuelnog zapisa! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	function confirmArchivingDecision(){
		if(!confirm('Arhiviranje! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	function confirmUnarchivingDecision(){
		if(!confirm('Izvadi iz arhive! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	function confirmSetAvailableToThePublicDecision(){
		if(!confirm('Postavi na javni uvid! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	function confirmSetUnavailableToThePublicDecision(){
		if(!confirm('Povuci sa javnog uvida! Da li ste sigurni?'))
			return false;
		else
			return true;
	}
	
	var formId = '';

	function setFocus(){
		if(formId != ''){
			var form = document.getElementById(formId); 
			if(form!=null){
				var inputs = form.elements;
				if(inputs!=null && inputs.length>0)
					for(var i=0;i<inputs.length;i++){
						if((inputs[i].tagName.toLowerCase()=='textarea') || (inputs[i].tagName.toLowerCase()=='select') || ((inputs[i].tagName.toLowerCase()=='input') && (inputs[i].type.toLowerCase()=='text'))){
								inputs[i].focus();
								break;
						};
							
					};
			};
		};
	}
	

	
	function setMenus(){
		var sideMenu = document.getElementById("sideMenu");
		sideMenu.style.top = getScrollY() + 300 + "px";
	}
	
	function getScrollY() {
		  var  scrOfY = 0;
		  if( typeof( window.pageYOffset ) == 'number' ) {
		    //Netscape compliant
		    scrOfY = window.pageYOffset;
		  } else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
		    //DOM compliant
		    scrOfY = document.body.scrollTop;
		  } else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
		    //IE6 standards compliant mode
		    scrOfY = document.documentElement.scrollTop;
		  }
		  return scrOfY;
		}
	
	function calculateSize(panelName){
		var thePanel = document.getElementById(panelName);
		if(thePanel != null) {
			thePanel.style.maxHeight = (getDocumentHeight() - 200) + 'px';
			thePanel.style.maxWidth = (getDocumentWidth() - 100) + 'px';
		}
	}
	
	function getDocumentHeight() {
		  var myHeight = 0;
		  if( typeof( window.innerWidth ) == 'number' ) {
		    //Non-IE
		    myHeight = window.innerHeight;
		  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
		    //IE 6+ in 'standards compliant mode'
		    myHeight = document.documentElement.clientHeight;
		  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
		    //IE 4 compatible
		    myHeight = document.body.clientHeight;
		  }
		  return myHeight;
	}

	function getDocumentWidth() {
		  var myWidth = 0;
		  if( typeof( window.innerWidth ) == 'number' ) {
		    //Non-IE
		    myWidth = window.innerWidth;
		  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
		    //IE 6+ in 'standards compliant mode'
		    myWidth = document.documentElement.clientWidth;
		  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
		    //IE 4 compatible
		    myWidth = document.body.clientWidth;
		  }
		  return myWidth;
		}
	
	var pleaseWait = new Date();
	function pleaseWaitColorOn(){
		temp = new Date();
		if(temp.getTime() - pleaseWait.getTime()>=1000){
			plWait = document.getElementById("pleaseWait");
			if(plWait!=null){
				plWait.style.filter = "alpha(opacity=30)";
    			plWait.style.MozOpacity = "0.3"; 
				plWait.style.opacity = "0.3";
    			plWait.style.backgroundColor = "#dddddd";
			}
		}
	}
	
	var activeElId = null;
	
	function onStart(){
		if((activeElId == null) || (activeElId.match(/Input$/))){
			if((activeElId != null) && (activeElId.match(/Input$/)) && (document.activeElement != document.getElementById(activeElId))){
				activeElListId = activeElId.replace(new RegExp("Input", 'g'), "List");
				autocompleteList = document.getElementById(activeElListId);
				if(autocompleteList != null){
					autocompleteList.style.visibility = 'hidden';
				}
				activeElId = null;
			} else {
				plWait = document.getElementById("pleaseWait");
				if(plWait!=null){
					plWait.style.filter = "alpha(opacity=0)";
		    		plWait.style.MozOpacity = "0"; 
					plWait.style.opacity = "0";
					plWait.style.backgroundColor="#ffffff";
					plWait.style.display = "block";
				}
				pleaseWait = new Date();
				setTimeout("pleaseWaitColorOn()",1000);
				var w = 800;
				if(document.all){
					w = document.body.clientWidth;
				}else{
					w = window.innerWidth;
				}
				var pb = document.getElementById("progressBar");
				pb.style.left = (w-100)+"px";
				pb.style.top  = (getScrollY() + 120)+"px";
				pb.style.visibility="visible";
			}
		} else {
			activeElId = null;
		}
	}
	
	function onStop(){
		pleaseWait.setTime(new Date()-1001);
		plWait = document.getElementById("pleaseWait");
		if(plWait!=null)
			plWait.style.display = "none";
		var pb = document.getElementById("progressBar");
		if(pb != null)
			pb.style.visibility="hidden"; 		 
	}
	


	function keyDownHandler(e, submitName){
		var KeyID = (window.event) ? event.keyCode : e.keyCode;
		if (KeyID == 13){
			btnSubmit = document.getElementById(submitName);
			btnSubmit.click();
			if (window.event) {
				 if (event.preventDefault) event.preventDefault();
			     if (event.stopPropagation) event.stopPropagation();
			} else {
				 if (e.preventDefault) e.preventDefault();
			     if (e.stopPropagation) e.stopPropagation();
			};
		};
	}
	
	function menuClick(e, panelName, formName){
		var thePanel=SimpleTogglePanelManager.panels.get(panelName);
		if(thePanel.status=="false"){
			SimpleTogglePanelManager.toggleOnClient(e,panelName);
		} else {
			SimpleTogglePanelManager.toggleOnClient(e,panelName);
			SimpleTogglePanelManager.toggleOnClient(e,panelName);
		}
		formId = formName;
		setFocus();
	}
	
	 function pastOrPresentDate(day){
		 var curDt = new Date();
         if (curDt.getTime() - day.date.getTime() >= 0) return true; 
         else return false;  
     }
	  
	 function disabledFutureDateStyleClass(day){
         if (pastOrPresentDate(day)) 
        	 return day.styleClass;
         else 
        	 return 'rich-calendar-boundary-dates';
     } 
	 
	 function disablementPeviousDate(day){
		 var curDt = new Date();
         if (curDt.getTime() - day.date.getTime() < 0) return true; 
         else return false;  
     }
	 
	 function disablementPeviousDateStyleClass(day){
         if (disablementPeviousDate(day)) 
        	 return day.styleClass;
         else 
        	 return 'rf-cal-boundary-day';
     }
	 
	 function disablementFutureDate(day){
		 var curDt = new Date();
         if (curDt.getTime() - day.date.getTime() > 0) return true; 
         else return false;  
     }
	 
	 function disablementFutureStyleClass(day){
         if (disablementFutureDate(day)) 
        	 return day.styleClass;
         else 
        	 return 'rf-cal-boundary-day';
     }
	 
	 function calculateSizeInputTextarea(panelName){
			var thePanel = document.getElementById(panelName);
			thePanel.style.minWidth = (getDocumentWidth() - 100) + 'px';
		}
	 
	 function pickControlNumber(autocompleteBox, controlNumberField){
		 temp = controlNumberField.value;
		 controlNumberField.value=autocompleteBox.value;
		 if(controlNumberField.value.indexOf('(BISIS)') != -1){
			 autocompleteBox.value=temp;
		 }
	 }
	
	function getSelectedText()
	{
	  var selectedText=(
	        window.getSelection
	        ?
	            window.getSelection()
	        :
	            document.getSelection
	            ?
	                document.getSelection()
	            :
	                document.selection.createRange().text
	     );
	 return selectedText;
	}
	
	var copiedReference='';
	var copiedNotClear = false;
	
	function copySelected(source, target, autocomplete, color){
		var sourceComponent = document.getElementById(source);
		var targetComponent = document.getElementById(target);
		var selectedText = getSelectedText().toString();
		if(copiedNotClear)
			sourceComponent.innerHTML = sourceComponent.textContent||sourceComponent.innerText;
		if(autocomplete){
			targetComponent.focus();
			targetComponent.value = '';
			targetComponent.value = selectedText;
			
			var evt;
			if(window.isIE) {
				evt = document.createEventObject("KeyboardEvent");
			} else {
				evt = document.createEvent("KeyboardEvent");
			}
			if (evt && evt.initKeyboardEvent) {
				//Chrome done
				evt.initKeyboardEvent("keydown", true, true, document.defaultView, false, false, false, false, 39, 39);	
				Object.defineProperty(evt, 'keyCode', {
                get : function() {
                    return this.keyCodeVal;
                }
				});     
				Object.defineProperty(evt, 'which', {
							get : function() {
								return this.keyCodeVal;
							}
				});   
				evt.keyCodeVal = 39;
			}else if (evt && evt.initKeyEvent) {
				//FF done
				evt.initKeyEvent("keypress", false, false, null,
								false, false, false, false, 39, 0);					
			} else {
				// IE done
				evt.cancelBubble=false;
				evt.keyCode = 65;
				evt.type="keydown";
			}
			if(window.isIE) {
				targetComponent.fireEvent("onkeydown", evt);
			} else {
				targetComponent.dispatchEvent(evt);
			} 
		} else {
			targetComponent.value = selectedText;
		}
		sourceComponent.innerHTML = sourceComponent.innerHTML.replace(targetComponent.value, '<span style="background-color:'+color+'">'+targetComponent.value+'</span>');
		copiedReference = sourceComponent.innerHTML;
	}
	
	function handlepaste (elem, e) {
		copiedNotClear = true;
		var event = e || window.event;
		if (event && event.clipboardData && event.clipboardData.getData) {// Webkit - get data from clipboard, put into editdiv, cleanup, then cancel event
			elem.innerHTML = event.clipboardData.getData('text/plain');
			copiedReference = elem.innerHTML;
			if (event.preventDefault) {
				event.stopPropagation();
				event.preventDefault();
			}
			copiedNotClear = false;
	        return false;
	    } else if (window && window.clipboardData && window.clipboardData.getData) {// IE
	        elem.innerHTML = window.clipboardData.getData('Text');
			copiedReference = elem.innerHTML;
			if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
			copiedNotClear = false;
			return false;
	    } else {
			return true;
	    }
	}
	
	function validateJournalForm (idISSNPolja) {
		var issnComponent = document.getElementById(idISSNPolja);
		var issnString = issnComponent.value;
	    var result = true;
	    if(issnString.trim()==""){
	       result = false;
	    }
	    var sablonZaIssn = /\d{4}-\d{3}[\dx]/i;
	    var deloviStr = issnString.split(";");
	    
	    for (var iBr = 0; ((iBr<deloviStr.length)&&(result==true)); iBr++) {
	       var deoStr = deloviStr[iBr];
	       if(deoStr.indexOf("(pISSN)")!=-1){
	          deoStr = deoStr.substring(0, deoStr.indexOf("(pISSN)"));
	       }
	       else if(deoStr.indexOf("(eISSN)")!=-1){
	          deoStr = deoStr.substring(0, deoStr.indexOf("(eISSN)"));
	       }
	       else if(deoStr.indexOf("(oldISSN)")!=-1){
	          deoStr = deoStr.substring(0, deoStr.indexOf("(oldISSN)"));
	       }
	       if(deoStr.length!=9){
	          result = false;
	          break;
	       }
	       result = sablonZaIssn.test(deoStr);
	    }
	    if(result==false){
	    	issnComponent.value = null;
	    	issnComponent.focus();
	    }	
	    return true;
	}
	
	window.myfaces = window.myfaces || {}; 
    myfaces.config = myfaces.config || {}; 
    myfaces.config.no_portlet_env = true; 
	
	
	