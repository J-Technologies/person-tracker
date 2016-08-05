import React from "react";

export default ({websocketUrl}) => {

    let outputNode;

    const createSocket = () => {
        let socket = new WebSocket(websocketUrl);
        socket.onmessage = msg => outputNode.innerHTML += msg.data + '\n';
        socket.onerror = err => setTimeout(createSocket, 10000);
    };

    createSocket();

    const switchVisible = node => node.style.display === 'none' ? node.style.display = 'block' : node.style.display = 'none';

    return (
        <div className="logging-output">
            <div className="container">

                <h4 onClick={() => switchVisible(outputNode)}>
                    Show Logging +
                </h4>

                <pre ref={node => outputNode = node}/>

            </div>
        </div>
    )
};