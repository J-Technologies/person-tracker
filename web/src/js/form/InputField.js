import React from "react";

export default ({id, label = "", type = "text"}) => {

    return (
        <div className="form-group">
            
            <label htmlFor={id} className="col-sm-2 control-label">{label}</label>
            
            <div className="col-sm-10">
                
                <input type={type} className="form-control" name={id} id={id} placeholder={label}/>
            
            </div>
        </div>
    )
};