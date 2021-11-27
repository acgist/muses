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
	<div class="container main">
		<div class="login">
			<form method="post" action="/login">
				<input name="token" type="hidden" value="${token}" />
				<div class="form-group">
					<label for="username" class="control-label">账号：</label>
					<input tabindex="1" required="required" name="username" type="text" class="form-control" id="username" placeholder="账号" />
				</div>
				<div class="form-group">
					<label for="password" class="control-label">密码：</label>
					<input tabindex="2" required="required" name="password" type="password" class="form-control" id="password" placeholder="密码" />
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-primary">登陆</button>
				</div>
			</form>
		</div>
	</div>
	<#include "/include/footer.ftl">
	</body>
</html>