【${log.updatedName}】修改【${tableMapping.tableName?default(tableMapping.table)}】记录【${log.sourceId}】：
<#if diffMap?exists>
	<#list diffMap?keys as key> 
	属性【${fieldMap[key].name}】从【${diffMap[key]}】改为【${sourceMap[key]}】
	</#list>
</#if>