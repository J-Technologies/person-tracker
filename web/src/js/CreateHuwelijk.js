import React from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";
import service from "./service/PersonService";

export default () => {

    const handleSubmit = e => {
        e.preventDefault();
        service.createHuwelijk(new FormData(document.getElementById('createHuwelijk')))
    };

    return (
        <div>
            <h1>Maak een huwelijk aan</h1>
            <hr/>
            <form className="form-horizontal" id="createHuwelijk" onSubmit={handleSubmit}>

                <InputField id="bsn1" label="Burgerservicenummer" required={true}/>

                <InputField id="bsn2" label="Burgerservicenummer" required={true}/>

                <InputField id="datum" label="Datum" type="date"/>

                <input name="gemeente" type="hidden" value="0505"/>

                <SubmitButton />
            </form>
        </div>
    )

};