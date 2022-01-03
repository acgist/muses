<!DOCTYPE HTML>
<html>
	<head>
		<title>系统错误</title>
		<#include "/include/head.ftl">
		<meta name="keywords" content="系统错误" />
		<meta name="description" content="系统错误" />
		
		<#include "/include/resources.ftl">
	</head>

	<body>
		<#include "/include/header.ftl">
		<main>
			<p>系统错误</p>
			<#if message??>
			<p>错误描述：${message.code} - ${message.message}</p>
			</#if>
			<p>系统时间：${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
		</main>
		<#include "/include/footer.ftl">
	</body>
</html>