<!DOCTYPE html>
<html lang="ZH-cn">
<head>
	<title>登陆页面</title>
	<meta charset="utf-8" />
	<meta name="author" content="acgist" />
	<meta name="description" content="登陆页面" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous" />
	<link href="https://getbootstrap.com/docs/4.0/examples/signin/signin.css" rel="stylesheet" crossorigin="anonymous" />
	<style type="text/css">
	p{position:relative;}
	.image{position:absolute;top:0.2rem;right:0.4rem;z-index:9999;}
	</style>
</head>
<body>
	<div class="container">
		<form class="form-signin" method="post" action="/oauth2/login">
			<h2 class="form-signin-heading">登陆</h2>
			<#if message??>
			<div class="alert alert-danger" role="alert">${message}</div>
			</#if>
			<p>
				<label for="type" class="sr-only">登陆方式</label>
				<select id="type" name="type" class="form-control" onchange="change()">
					<option value="sms">短信</option>
					<option value="password" selected="selected">密码</option>
				</select>
			</p>
			<p>
				<label for="username" class="sr-only">用户名称</label>
				<input type="text" id="username" name="username" class="form-control" placeholder="用户名称" />
			</p>
			<p>
				<label for="password" class="sr-only">用户密码</label>
				<input type="password" id="password" name="password" class="form-control" placeholder="用户密码" />
			</p>
			<p>
				<label for="code" class="sr-only">图形验证码</label>
				<input type="text" id="code" name="code" class="form-control" placeholder="图形验证码" />
				<img id="image" class="image" alt="图形验证码" src="/oauth2/code.jpg" />
			</p>
			<p>
				<label for="smsCode" class="sr-only">短信验证码</label>
				<input type="text" id="smsCode" name="smsCode" class="form-control" placeholder="短信验证码" onfocus="sms()" />
			</p>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button>
		</form>
		<script type="text/javascript">
		function sms() {
			var ajax = new XMLHttpRequest();
			ajax.open('get', '/oauth2/code.sms?mobile=' + document.getElementById('username').value);
			ajax.send();
		}
		function change() {
			const type = document.getElementById("type").value;
			console.log(type)
			switch(type) {
			case 'sms':
				document.getElementsByTagName('form')[0].setAttribute('action', '/oauth2/login/sms');
				document.getElementById('password').hidden = true;
				document.getElementById('code').hidden = true;
				document.getElementById('image').hidden = true;
				document.getElementById('smsCode').hidden = false;
				break;
			case 'password':
				document.getElementsByTagName('form')[0].setAttribute('action', '/oauth2/login');
				document.getElementById('password').hidden = false;
				document.getElementById('code').hidden = false;
				document.getElementById('image').hidden = false;
				document.getElementById('smsCode').hidden = true;
				break;
			}
		}
		change();
		</script>
	</div>
</body>
</html>