import ReactDOM from "react-dom";
import React from "react";
import {Router, Route, hashHistory} from "react-router";
import App from "./App";
import CreateNaturalPerson from "./CreateNaturalPerson";
import SearchNaturalPerson from "./SearchNaturalPerson";

ReactDOM.render(
    <Router history={hashHistory}>
        <Route path="/" component={App}>

            <Route path="/createNaturalPerson" component={CreateNaturalPerson}/>
            <Route path="/searchNaturalPerson" component={SearchNaturalPerson}/>

        </Route>

    </Router>
    , document.getElementById('brp'));

//<App />