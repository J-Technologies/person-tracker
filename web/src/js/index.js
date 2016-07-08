import ReactDOM from "react-dom";
import React from "react";
import HeaderNav from "./HeaderNav";
import Output from "./Output";
import CreateNaturalPerson from "./CreateNaturalPerson";

ReactDOM.render(
    <div>
        <HeaderNav title="BRP">
            <li className="active"><a href="#">Creeër persoon</a></li>
        </HeaderNav>
        <CreateNaturalPerson />
        <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>
    </div>
    , document.getElementById('brp')
);