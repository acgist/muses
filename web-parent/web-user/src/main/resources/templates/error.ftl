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
		<header>
			<p>系统错误</p>
		</header>
		<div>
			<p>错误描述：${message}</p>
			<p>系统时间：${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
		</div>
		<#include "/include/footer.ftl">
	</body>
</html>