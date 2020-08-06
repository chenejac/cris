<#include "harvard_concepts.ftl">

<#macro harvard_authors>
<#t>${mainAuthor.name.lastname?upper_case}
<#t><@nameInitial mainAuthor.name.firstname/>
<#t><#list otherAuthors as author>
<#t><#if author_has_next>,&nbsp;<#else> and </#if>
<#t>${author.name.lastname?upper_case}
<#t><@nameInitial author.name.firstname/>
<#t></#list>
</#macro>


<#macro harvard_editors>
<#if editors?size=1>${editors[0].name.lastname?upper_case}<@nameInitial editors[0].name.firstname/>(ed.)
<#elseif editors?size &gt;3>${editors[0].name.lastname?upper_case}<@nameInitial editors[0].name.firstname/>&nbsp;et al.(eds.)
<#else>
<#t><#list editors as editor>		
<#t><#if editor_index &gt; 0>
<#t><#if editor_has_next>,&nbsp;<#else> and </#if>
<#t></#if> 
<#t>${editor.name.lastname?upper_case}<@nameInitial editor.name.firstname/>		
<#t></#list>&nbsp;(eds.)
</#if>
</#macro>


<#macro harvard_edition>
<#t><#if editionTitle??>.&nbsp;${editionTitle?trim}
<#t><#elseif editionNumber?? && editionNumber &gt;1>.${editionNumber}
<#t></#if>	
</#macro>


<#macro harvard_publisher>
<#if publisher.originalPublisher.place?? && publisher.originalPublisher.place?trim?length &gt; 0>
<#t>.&nbsp;${publisher.originalPublisher.place}
<#t></#if>
<#t><#if publisher.originalPublisher.name?? && publisher.originalPublisher.name?length &gt; 0>:&nbsp;${publisher.originalPublisher.name}</#if>
</#macro>

<#macro harvard_pages>
<#t><#if startPage??>
<#t><#if endPage??><#if startPage=endPage>,&nbsp;p. ${startPage}
<#t><#else>,&nbsp;pp. ${startPage}-${endPage}
<#t></#if>
<#t><#else>,&nbsp;p.${startPage}
<#t></#if>
<#t></#if>
</#macro>
