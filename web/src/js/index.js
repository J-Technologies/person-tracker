var socket = new WebSocket('ws://localhost:8123/todos/websocket');

socket.onmessage = function (msg) {
    document.getElementsByTagName('pre')[0].innerHTML += msg.data + '\n';
    console.log(msg.data);
};
