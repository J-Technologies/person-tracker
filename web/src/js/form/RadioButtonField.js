import React from "react";

export default ({name = "noop", label = "Noop", buttons = [{id: 'noop', label: 'Noop'}]}) => {

    const renderRadioOption = (item) => (
        <div className="radio" key={item.id}>
            <label>
                
                <input type="radio" name={name} id={item.id} defaultValue={item.id}/>
                
                {item.label}
                
            </label>
        </div>
    );

    return (
        <div className="form-group">
            
            <label className="col-sm-2 control-label">{label}</label>
            <div className="col-sm-10">

                {buttons.map(item => renderRadioOption(item))}
                
            </div>
        </div>
    )
};