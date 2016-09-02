import React, {Component} from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";

export default class PersonDeceased extends Component {

    render() {
        return (
            <div>
                <h1>Overleden persoon</h1>
                <hr/>

                <form className="form-horizontal" id="searchPerson" onSubmit={
                    e => {
                        e.preventDefault();
                        alert('not implemented yet')
                    }}>
                    <InputField id="bsn" label="BSN nummer"/>

                    <InputField id="datum" label="Datum overlijden" type="date"/>

                    <InputField id="gemeente" label="Gemeente" type="text" placeholder="gemeente-code"/>

                    <SubmitButton/>
                </form>
            </div>
        )
    }
}