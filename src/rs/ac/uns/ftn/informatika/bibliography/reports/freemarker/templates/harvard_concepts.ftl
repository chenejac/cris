<#macro harvard_authors>
<#t>${mainAuthor.name.lastname?upper_case}
<#t><@nameInitial mainAuthor.name.firstname/>
<#t><#if otherAuthors?size &gt;2>&nbsp;et al.<#else>
<#t><#list otherAuthors as author>
<#t><#if author_has_next>,&nbsp;<#else> and </#if>
<#t>${author.name.lastname?upper_case}<@nameInitial author.name.firstname/>
<#t></#list></#if>
</#macro>


<#macro harvard_all_authors>
<#t>${mainAuthor.name.lastname?upper_case}
<#t><@nameInitial mainAuthor.name.firstname/>
<#t><#list otherAuthors as author>
<#t><#if author_has_next>,&nbsp;<#else> and </#if>
<#t>${author.name.lastname?upper_case}
<#t><@nameInitial author.name.firstname/>
<#t></#list>
</#macro>

<#macro nameInitial name>
<@compress>
<#t><#if name?length &gt;1>
<#t><#if (name?upper_case?starts_with("LJ") || name?upper_case?starts_with("NJ"))>,&nbsp;${name?substring(0,2)?upper_case}.
<#t><#else>,&nbsp;${name?substring(0,1)?upper_case}.</#if>
<#t><#elseif name?length=1>,&nbsp;${name?upper_case}.</#if>
</@compress>
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
<#t></#if>
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
