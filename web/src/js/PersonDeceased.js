import React, {Component} from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";

export default class PersonDeceased extends Component {

    postDeceasedPerson() {
        fetch("http://localhost:8123/persoon/overlijden", {
            method: 'post',
            body: new FormData(document.getElementById('personDeceased'))
        })
            .then(response => console.log(response))
            .catch(err => console.error(err));
    }

    render() {
        return (
            <div>
                <h1>Overleden persoon</h1>
                <hr/>

                <form className="form-horizontal" id="personDeceased" onSubmit={
                    e => {
                        e.preventDefault();
                        this.postDeceasedPerson();
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