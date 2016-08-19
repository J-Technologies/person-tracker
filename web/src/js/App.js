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
                    <li>
                        <Link to="/createNaturalPerson" activeClassName="active">
                            Creeër persoon
                        </Link>
                    </li>

                    <li>
                        <Link to="/searchNaturalPerson" activeClassName="active">
                            Zoek persoon
                        </Link>
                    </li>
                </HeaderNav>

                {this.props.children}

                <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>
            </div>
        )
    }
}