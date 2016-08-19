import React, {Component} from "react";
import Header from "./header/Header";
import Nav from "./header/Nav";
import Output from "./Output";
import {Link} from "react-router";

export default class App extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Header title="BRP">
                    <Nav to="/createNaturalPerson" label="Creeër persoon"/>
                    <Nav to="/searchNaturalPerson" label="Zoek persoon"/>
                </Header>

                {this.props.children}

                <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>
            </div>
        )
    }
}