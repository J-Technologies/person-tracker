import ReactDOM from 'react-dom'
import React from 'react'
import Output from './Output'

let socket = new WebSocket('ws://localhost:8123/persoon/websocket');
socket.onmessage = (msg) => document.getElementsByTagName('pre')[0].innerHTML += msg.data + '\n';

ReactDOM.render(
    <div>
        <h1>Messages received</h1>
        <Output websocketUrl="ws://localhost:8123/persoon/websocket" />
    </div>
    , document.getElementById('example')
);