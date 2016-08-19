import React from "react";

export default ({title, children}) => {

    return (
        <nav className="navbar navbar-inverse navbar-fixed-top">
            <div className="container">
                <div className="navbar-header">

                    <a className="navbar-brand" href="#">{title}</a>

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