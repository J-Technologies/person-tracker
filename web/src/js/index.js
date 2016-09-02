import ReactDOM from "react-dom";
import React from "react";
import {Router, Route, hashHistory, IndexRoute} from "react-router";
import App from "./App";
import CreateNaturalPerson from "./CreateNaturalPerson";
import PersonDeceased from "./PersonDeceased";
import SearchNaturalPerson from "./SearchNaturalPerson";
import CreateHuwelijk from "./CreateHuwelijk";
import Welcome from "./Welcome";

ReactDOM.render(
    <Router history={hashHistory}>
        <Route path="/" component={App}>

            <IndexRoute component={Welcome}/>

            <Route path="/createNaturalPerson" component={CreateNaturalPerson}/>
            <Route path="/searchNaturalPerson" component={SearchNaturalPerson}/>
            <Route path="/setPersonDeceased" component={PersonDeceased}/>
            <Route path="/createHuwelijk" component={CreateHuwelijk}/>

        </Route>

    </Router>
    , document.getElementById('brp'));