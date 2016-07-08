import React from "react";

export default ({label = "Opslaan"}) => {

    return (
        <div className="form-group">
            <div className="col-sm-offset-2 col-sm-10">
                
                <button type="submit" className="btn btn-default">
                    {label}
                </button>
                
            </div>
        </div>
    )
};