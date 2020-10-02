<#include "harvard_concepts.ftl">
<#t><@harvard_all_authors/>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>&nbsp;${someTitle!""}.&nbsp;In: 
<#t><#if proceedings.editors?size &gt; 0>
<#t><#assign editors=proceedings.editors/>
<#t><@harvard_editors/>
<#t></#if>
<#t><i>${proceedings.someTitle!""}
<#t><#if proceedings.conference.place?? && proceedings.conference.place!="">,&nbsp;${proceedings.conference.place}</#if>
<#t><#if proceedings.conference.year??>,&nbsp;${proceedings.conference.year?string("0")}</#if>
<#t></i>
<#t><#assign publisher=proceedings.publisher>
<#t><@harvard_publisher/>
<#t><@harvard_pages/>  

