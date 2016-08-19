import React, {Component, PropTypes} from "react";

const propTypes = {
    id: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    type: PropTypes.oneOf(['text', 'date'])
};

class InputField extends Component {

    render() {
        const {id, label, type} = this.props;
        return (
            <div className="form-group">

                <label htmlFor={id} className="col-sm-2 control-label">{label}</label>

                <div className="col-sm-10">

                    <input type={type} className="form-control" name={id} id={id} placeholder={label}/>

                </div>
            </div>
        )
    }
}

InputField.propTypes = propTypes;

export default InputField