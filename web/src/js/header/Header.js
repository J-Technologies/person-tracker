import React from "react";
import {Link} from "react-router";

export default ({title, children}) => {

    return (
        <nav className="navbar navbar-inverse navbar-fixed-top">
            <div className="container">
                <div className="navbar-header">

                    <Link className="navbar-brand" to="/">
                        {title}
                    </Link>

                </div>
                <div className="collapse navbar-collapse">

                    <ul className="nav navbar-nav">
                        {children}
                    </ul>

                </div>
            </div>
        </nav>
    )
};