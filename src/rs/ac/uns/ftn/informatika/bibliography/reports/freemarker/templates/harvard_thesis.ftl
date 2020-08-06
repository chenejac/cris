<#include "harvard_concepts.ftl">
<#t>${author.name.lastname?upper_case} 
<#t><@nameInitial author.name.firstname/>
<#t><#if publicationYear??>&nbsp;(${publicationYear})</#if>
<i>${someTitle!""}</i>.&nbsp;(${localizedStudyType}),&nbsp;${institution.someName}