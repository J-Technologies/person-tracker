import ReactDOM from 'react-dom'
import React from 'react'

let socket = new WebSocket('ws://localhost:8123/todos/websocket');
socket.onmessage = (msg) => {
    document.getElementsByTagName('pre')[0].innerHTML += msg.data + '\n';
    console.log(msg.data);
};

ReactDOM.render(
    <h1>I should not think</h1>,
    document.getElementById('example')
);