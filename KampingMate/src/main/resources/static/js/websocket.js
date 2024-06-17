var socket = new WebSocket("ws://localhost:8090/chat");

socket.onopen = function() {
	console.log("WebSocket 연결 성공");
};

socket.onmessage = function(event) {
	var message = event.data;
	console.log("받은 메시지: " + message);
	// 받은 메시지를 화면에 표시하는 예시
	var messageElement = document.createElement('div');
	messageElement.textContent = message;
	document.getElementById('messages').appendChild(messageElement);
};

socket.onclose = function(event) {
	if (event.wasClean) {
		console.log('WebSocket 연결 종료');
	} else {
		console.error('WebSocket 연결 끊김');
	}
};

socket.onerror = function(error) {
	console.error("WebSocket 에러 발생: " + error.message);
};

function sendMessage() {
	var message = document.getElementById('messageInput').value;
	socket.send(message);
	document.getElementById('messageInput').value = "";
}