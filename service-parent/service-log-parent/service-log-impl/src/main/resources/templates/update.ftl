【${log.table}】修改记录【${log.sourceId}】：
<#if diffMap?exists>
	<#list diffMap?keys as key> 
	【${key}】改为【${diffMap[key]}】
	</#list>
</#if>
