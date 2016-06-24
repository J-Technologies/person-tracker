import ReactDOM from 'react-dom'
import React from 'react'

let socket = new WebSocket('ws://localhost:8123/todos/websocket');

socket.onmessage = (msg) => document.getElementsByTagName('pre')[0].innerHTML += msg.data + '\n';

ReactDOM.render(
    <h1>A react component</h1>,
    document.getElementById('example')
);