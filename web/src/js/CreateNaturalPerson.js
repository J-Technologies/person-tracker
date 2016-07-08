import React from "react";
import InputField from "./form/InputField";
import RadioButtonField from "./form/RadioButtonField";
import SubmitButton from "./form/SubmitButton";

export default () => {

    const postCreateNaturalPerson = () => fetch("http://localhost:8123/persoon/geboorte", {
        method: 'post',
        body: new FormData(document.getElementById('createPerson'))
    })
        .then(response => console.log(response))
        .catch(err => console.error(err));

    return (
        <div>
            <h1>Schrijf een natuurlijk persoon in</h1>
            <hr/>
            <form className="form-horizontal" id="createPerson" onSubmit={
                e => {
                    e.preventDefault();
                    postCreateNaturalPerson();
            }}>

                <InputField id="voornaam" label="Voornaam"/>

                <InputField id="achternaam" label="Achternaam"/>

                <RadioButtonField name="geslacht"
                                  label="Gelacht"
                                  buttons={[
                                    {id: 'onbekend', label: 'Onbekend'},
                                    {id: 'vrouw', label: 'Vrouw'},
                                    {id: 'man', label: 'Man'}
                                  ]}/>

                <InputField id="geboortedatum" label="Geboortedatum" type="date"/>

                <input name="partij" type="hidden" value="000505"/>

                <SubmitButton />
            </form>
        </div>
    )

};