import React, {Component} from "react";
import HeaderNav from "./HeaderNav";
import Output from "./Output";
import {Link} from "react-router";

export default class App extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <HeaderNav title="BRP">
                    <li onClick={() => this.setState({active: menuItem})}>
                        <Link to="/createNaturalPerson">Creeër persoon</Link>
                    </li>

                    <li onClick={() => this.setState({active: menuItem})}>
                        <Link to="/searchNaturalPerson">Zoek persoon</Link>
                    </li>
                </HeaderNav>

                {this.props.children}

                <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>
            </div>
        )
    }
}