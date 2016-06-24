import ReactDOM from "react-dom";
import React from "react";
import Output from "./Output";
import CreateNaturalPerson from "./CreateNaturalPerson";

ReactDOM.render(
    <div>
        <CreateNaturalPerson />
        <h1>Messages received</h1>
        <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>
    </div>
    , document.getElementById('example')
);