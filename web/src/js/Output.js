import React from "react";

export default ({websocketUrl}) => {

    let outputNode;

    let socket = new WebSocket(websocketUrl);
    socket.onmessage = (msg) => outputNode.innerHTML += msg.data + '\n';
    
    return (
        <div>
            <h1>Messages received</h1>
            <pre ref={node => outputNode = node}/>
        </div>
    )
};