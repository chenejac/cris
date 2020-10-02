<#include "harvard_concepts.ftl">
<#t><#-- ako monografija ima autora-->
<#t><#if mainAuthor?? && mainAuthor.name.lastname?trim != "">
<#t>	<#assign mainAuthor=mainAuthor>
<#t>	<#assign otherAuthors=otherAuthors>
<#t>	<@harvard_all_authors/>
<#t><#-- ako nema autore, ima urednike -->	
<#t><#elseif editors?size &gt; 0>
<#t>	<#assign editors=editors>
<#t>	<@harvard_editors/>
<#t></#if>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>&nbsp;<i>${someTitle!""}</i>
<#t><@harvard_edition/>
<#t><@harvard_publisher/>
