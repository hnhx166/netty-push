<!DOCTYPE HTML>
<html>
<head>
    <title>My WebSocket</title>
</head>

<body>
Welcome<br/>
<input id="text" type="text" /><button onclick="send()">Send</button>    <button onclick="closeWebSocket()">Close</button>
<button onclick="MacInfo()">MacInfo</button>
<div id="message">
</div>
</body>
<script type="text/javascript">


function MacInfo(){
	var wshNetwork = new ActiveXObject("WScript.Network");
    alert(wshNetwork.UserDomain);
}
    var websocket = null;
   // initSocket();
    //function initSocket(){
	    //判断当前浏览器是否支持WebSocket
	    if('WebSocket' in window){
	        websocket = new WebSocket("ws://localhost:7080/websocket");
	    }
	    else{
	        alert('Not support websocket')
	    }
	
	    //连接发生错误的回调方法
	    websocket.onerror = function(){
	        setMessageInnerHTML("error");
	    };
	
	    //连接成功建立的回调方法
	    websocket.onopen = function(event){
	        setMessageInnerHTML("open");
	    }
	
	    //接收到消息的回调方法
	    websocket.onmessage = function(event){
	        setMessageInnerHTML(event.data);
	    }
	
	    //连接关闭的回调方法
	    websocket.onclose = function(){
	        setMessageInnerHTML("close");
	        
	        /*if (tryTime < 10) {
	            setTimeout(function () {
	                webSocket = null;
	                tryTime++;
	                initSocket();
	                $("#connectStatu").append( getNowFormatDate()+"  第"+tryTime+"次重连<br/>");
	            }, 3*1000);
	        } else {
	            alert("重连失败.");
	        } */
	    }
    //}
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    //发送消息
    function send(){
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>
</html>