<#include "harvard_concepts.ftl"/>
<#t><@harvard_all_authors/>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>
<#t>&nbsp;${someTitle!""}
<#t><#if journal.someName??>.&nbsp;<i>${journal.someName}</i></#if>
<#t><#if volume??>,&nbsp;${volume?trim}</#if><#if number??>&nbsp;(${number?trim})</#if>
<#t><@harvard_pages/>

