<#include "pmf_concepts.ftl">
<@harvard_authors/>
<#if publicationYear??>&nbsp;(${publicationYear})</#if>
&nbsp;${someTitle!""}.&nbsp;
In:
<#-- ako monografija kojoj pripada rad ima autora-->
<#if monograph.mainAuthor?? && monograph.mainAuthor.name.lastname?trim != "">
	<#assign mainAuthor=monograph.mainAuthor>
	<#assign otherAuthors=monograph.otherAuthors>
	<@harvard_authors/>
<#-- ako nema autore, ima urednike -->	
<#elseif monograph.editors?size &gt; 0>
	<#assign editors=monograph.editors>
	<@harvard_editors/>
</#if>
<#t><i>${monograph.someTitle!""}</i>
<#t><#if monograph.editionTitle??>.&nbsp;${monograph.editionTitle?trim}
<#t><#elseif monograph.editionNumber?? && editionNumber &gt;1>.${editionNumber}
<#t></#if>	
<#t><#assign publisher=monograph.publisher>
<#t><@harvard_publisher/>
<#t><@harvard_pages/>   
