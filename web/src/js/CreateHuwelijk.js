import React from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";

export default () => {

    const handleSubmit = e => {
        e.preventDefault();
        alert('not implemented yet')
    };

    return (
        <div>
            <h1>Maak een huwelijk aan</h1>
            <hr/>
            <form className="form-horizontal" id="createPerson" onSubmit={handleSubmit}>

                <InputField id="bsn" label="Burgerservicenummer"/>

                <InputField id="bsnpartner" label="Burgerservicenummer partner"/>

                <SubmitButton />
            </form>
        </div>
    )

};