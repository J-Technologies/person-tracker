import React from "react";

const ViewContent = ({websocketUrl}) => {

    let socket = new WebSocket(websocketUrl);
    socket.onmessage = (msg) => node.innerHTML += msg.data + '\n';
    
    return (
        <pre ref={node => outputNode = node}/>
    )
};

export default ViewContent;