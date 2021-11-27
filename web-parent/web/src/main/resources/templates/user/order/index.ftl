<!DOCTYPE HTML>
<html>
	<head>
		<title>订单</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="订单" />
		<meta name="description" content="订单" />
		
		<#include "/include/resources.ftl">
	</head>

	<body>
	<#include "/include/header.ftl">
	<main>
		<div>
			<form method="post" action="/user/order">
				<input name="token" type="hidden" value="${token}" />
				<div>
					<input tabindex="1" required="required" name="orderId" type="text" placeholder="订单号" />
				</div>
				<div>
					<button type="submit">提交订单</button>
				</div>
			</form>
			<#if orderId??>
			<div>
				<p>交易订单：${orderId}</p>
			</div>
			</#if>
		</div>
	</main>
	<#include "/include/footer.ftl">
	</body>
</html>