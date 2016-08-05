import React, {Component} from "react";
import HeaderNav from "./HeaderNav";
import Output from "./Output";
import CreateNaturalPerson from "./CreateNaturalPerson";
import SearchNaturalPerson from "./SearchNaturalPerson";


export default class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            menu: [ "Creeër persoon", "Zoek persoon" ],
            active: "Creeër persoon"
        }
    }

    static renderView(menu) {
        switch(menu) {
            case "Creeër persoon":
                return <CreateNaturalPerson />;
            case "Zoek persoon":
                return <SearchNaturalPerson />;
        }
    }

    render() {
        const {menu, active} = this.state;

        return (
            <div>

                <HeaderNav title="BRP">
                    {menu.map(menuItem =>
                        <li key={menuItem}
                            className={active === menuItem ? 'active': ''}
                            onClick={() => this.setState({active: menuItem})}>

                            <a href="#">{menuItem}</a>

                        </li>)
                    }
                </HeaderNav>

                {App.renderView(active)}

                <Output websocketUrl="ws://localhost:8123/persoon/websocket"/>

            </div>
        )
    }
}