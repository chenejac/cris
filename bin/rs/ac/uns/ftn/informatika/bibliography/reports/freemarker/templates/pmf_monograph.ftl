<#include "pmf_concepts.ftl">
<#-- ako monografija ima autora-->
<#if mainAuthor?? && mainAuthor.name.lastname?trim != "">
	<#assign mainAuthor=mainAuthor>
	<#assign otherAuthors=otherAuthors>
	<@harvard_authors/>
<#-- ako nema autore, ima urednike -->	
<#elseif editors?size &gt; 0>
	<#assign editors=editors>
	<@harvard_editors/>
</#if>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>&nbsp;<i>${someTitle!""}</i>
<#t><@harvard_edition/>
<#t><@harvard_publisher/>
