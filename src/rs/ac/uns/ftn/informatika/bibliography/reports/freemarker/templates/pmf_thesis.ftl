<#include "harvard_concepts.ftl">
<#t>${author.name.lastname?upper_case}<#if author.name.firstname?length &gt;0 ><@nameInitial author.name.firstname/></#if>
<#if publicationYear??>&nbsp;(${publicationYear})</#if>
<i>${someTitle!""}</i>.&nbsp;(${localizedStudyType}),&nbsp;${institution.someName}