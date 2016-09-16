import React from "react";
import {Link} from "react-router";

export default ({to, label}) => {

    return (
        <li>
            <Link to={to} activeClassName="active">
                {label}
            </Link>
        </li>
    )
};