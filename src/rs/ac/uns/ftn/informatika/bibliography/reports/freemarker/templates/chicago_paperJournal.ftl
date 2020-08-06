<#t>${mainAuthor.name.firstname}&nbsp;${mainAuthor.name.lastname}
<#t><#list otherAuthors as author>
<#t><#if author_has_next>,&nbsp;<#else> and </#if>
<#t>${author.name.firstname}&nbsp;${author.name.lastname}
</#list>
,&nbsp;"${title.content}",<i>${journal.name.content}</i>&nbsp;${volume}&nbsp;(${publicationYear}):${startPage}-${endPage}
