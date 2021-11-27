<!DOCTYPE HTML>
<html>
	<head>
		<title>登录</title>
		<#include "/include/head.ftl">
		<meta name="keywords" content="登录" />
		<meta name="description" content="登录" />
		
		<#include "/include/resources.ftl">
	</head>

	<body>
	<#include "/include/header.ftl">
	<main>
		<div>
			<form method="post" action="/login">
				<input name="token" type="hidden" value="${token}" />
				<div>
					<label for="username">账号：</label>
					<input tabindex="1" required="required" name="username" type="text" id="username" placeholder="账号" />
				</div>
				<div>
					<label for="password">密码：</label>
					<input tabindex="2" required="required" name="password" type="password" id="password" placeholder="密码" />
				</div>
				<div>
					<button type="submit">登陆</button>
				</div>
			</form>
		</div>
	</main>
	<#include "/include/footer.ftl">
	</body>
</html>