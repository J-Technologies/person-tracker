import React, {Component, PropTypes} from "react";

const propTypes = {
    name: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    buttons: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        label: PropTypes.string.isRequired
    })).isRequired
};

class RadioButtonField extends Component {

    renderRadioOption(item, name) {
        return (
            <div key={item.id} className="radio">
                <label>
                    <input type="radio" name={name} id={item.id} defaultValue={item.id}/>
                    {item.label}
                </label>
            </div>
        )
    }

    render() {
        const {label, name, buttons} = this.props;
        return (
            <div className="form-group">

                <label className="col-sm-2 control-label">{label}</label>
                <div className="col-sm-10">

                    {buttons.map(item => this.renderRadioOption(item, name))}

                </div>
            </div>
        )
    }
}

RadioButtonField.propTypes = propTypes;

export default RadioButtonField