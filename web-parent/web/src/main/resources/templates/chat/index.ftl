<!DOCTYPE HTML>
<html>
	<head>
		<title>聊天</title>
		<#include "/include/head.ftl">
		<meta name="keywords" content="聊天" />
		<meta name="description" content="聊天" />
		
		<#include "/include/resources.ftl">
	</head>

	<body>
	<#include "/include/header.ftl">
	<main>
		<div>
			<textarea id="message"></textarea>
			<button onclick="send()">发送</button>
		</div>
	</main>
	<#include "/include/footer.ftl">
	<script type="text/javascript">
	var client = null;
	if(WebSocket) {
		client = new WebSocket("ws://localhost:8080/chat.socket");
	} else {
		alert("浏览器不支持WebSocket");
	}
	client.onopen = function(event) {
		console.log("打开WebSocket成功");
	}
	client.onerror = function() {
		console.log("打开WebSocket异常");
	};
	client.onmessage = function(event) {
		console.log("收到WebSocket信息：" + event.data);
		notice(event.data);
	}
	client.onclose = function() {
		console.log("关闭WebSocket成功");
	}
	window.onbeforeunload = function() {
		client.close();
	}
	if(Notification && Notification.permission !== "granted"){
		Notification.requestPermission(function(status){
			if(Notification.permission !== status){
				Notification.permission = status;
			}
		});
	}
	function send() {
		var message = document.getElementById("message").value;
		if(message && message != '') {
			console.log("发送WebSocket信息：" + message);
			client.send(message);
		}
	}
	function notice(message) {
		if(!Notification) {
			alert("浏览器不支持！");
			return;
		}
		var options = {
			lang : "utf-8",
			icon : "https://static.acgist.com/logo.png",
			body : message
		};
		if(Notification && Notification.permission === "granted") {
			var notify = new Notification("你收到一条通知！", options);
			notify.onshow = function(){
				console.log("你看到了通知！");
			};
			notify.onclick = function() {
				console.log("你点击了通知！");
			};
			notify.onclose = function() {
				console.log("你关闭了通知！");
			};
			notify.onerror = function() {
				console.log("发生了一点意外！");
			}
		} else {
			alert("没有获得权限!");
		}
	}
	</script>
	</body>
</html>