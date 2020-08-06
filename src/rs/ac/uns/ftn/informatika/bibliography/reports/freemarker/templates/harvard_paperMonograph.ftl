<#include "harvard_concepts.ftl">
<#t><@harvard_all_authors/>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>
<#t>&nbsp;${someTitle!""}.&nbsp;
<#t>In:
<#t><#-- ako monografija kojoj pripada rad ima autora-->
<#t><#if monograph.mainAuthor?? && monograph.mainAuthor.name.lastname?trim != "">
<#t>	<#assign mainAuthor=monograph.mainAuthor>
<#t>	<#assign otherAuthors=monograph.otherAuthors>
<#t>	<@harvard_authors/>
<#t><#-- ako nema autore, ima urednike -->	
<#t><#elseif monograph.editors?size &gt; 0>
<#t>	<#assign editors=monograph.editors>
<#t>	<@harvard_editors/>
<#t></#if>
<#t><i>${monograph.someTitle!""}</i>
<#t><#if monograph.editionTitle??>.&nbsp;${monograph.editionTitle?trim}
<#t><#elseif monograph.editionNumber?? && editionNumber &gt;1>.${editionNumber}
<#t></#if>	
<#t><#assign publisher=monograph.publisher>
<#t><@harvard_publisher/>
<#t><@harvard_pages/>   
