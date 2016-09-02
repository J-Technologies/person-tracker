import React from "react";
import InputField from "./form/InputField";
import RadioButtonField from "./form/RadioButtonField";
import SubmitButton from "./form/SubmitButton";
import service from "./service/PersonService"

export default () => {

    const handleSubmit = e => {
        e.preventDefault();
        service.createNaturalPerson(new FormData(document.getElementById('createPerson')));
    };

    return (
        <div>
            <h1>Schrijf een natuurlijk persoon in</h1>
            <hr/>
            <form className="form-horizontal" id="createPerson" onSubmit={handleSubmit}>

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

                <input name="gemeente" type="hidden" value="0505"/>
                <input name="partij" type="hidden" value="000505"/>
                
                <SubmitButton />
            </form>
        </div>
    )

};