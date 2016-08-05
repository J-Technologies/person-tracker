import React from "react";

export default ({websocketUrl}) => {

    const createSocket = () => {
        let socket = new WebSocket(websocketUrl);
        socket.onmessage = msg => outputNode.innerHTML += msg.data + '\n';
        socket.onerror = err => setTimeout(createSocket, 10000);
    };

    createSocket();

    let outputNode;

    return (
        <div className="logging-output">
            <div className="container">

                <h3>
                    Logging
                </h3>

                <pre ref={node => outputNode = node} />

            </div>
        </div>
    )
};