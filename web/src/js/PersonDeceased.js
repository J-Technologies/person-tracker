import React, {Component} from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";
import service from "./service/PersonService"

export default class PersonDeceased extends Component {

    handleSubmit(e) {
        e.preventDefault();
        service.postDeceasedPerson(new FormData(document.getElementById('personDeceased')));
    }

    render() {
        return (
            <div>
                <h1>Overleden persoon</h1>
                <hr/>

                <form className="form-horizontal" id="personDeceased" onSubmit={this.handleSubmit}>
                    <InputField id="bsn" label="BSN nummer"/>

                    <InputField id="datum" label="Datum overlijden" type="date"/>

                    <InputField id="gemeente" label="Gemeente" type="text" placeholder="gemeente-code"/>

                    <SubmitButton/>
                </form>
            </div>
        )
    }
}