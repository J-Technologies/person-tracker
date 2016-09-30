import React, {Component} from "react";
import InputField from "./form/InputField";
import RadioButtonField from "./form/RadioButtonField";
import SubmitButton from "./form/SubmitButton";
import service from "./service/PersonService";
import _ from "lodash";

export default class PersonDeceased extends Component {

    constructor(props) {
        super(props);
        this.state = {
            ouders: 0
        }
    }

    handleSubmit(e) {
        e.preventDefault();
        service.createNaturalPerson(new FormData(document.getElementById('createPerson')));
    };

    addOuder(e) {
        e.preventDefault();
        const {ouders} = this.state;
        if (ouders < 2) {
            this.setState({ouders: ouders + 1})
        }
    };

    render() {
        const {ouders} = this.state;

        return (
            <div>
                <h1>Schrijf een natuurlijk persoon in</h1>
                <hr/>
                <form className="form-horizontal" id="createPerson" onSubmit={this.handleSubmit}>

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

                    <div style={{
                        display: 'flex',
                        flexDirection: 'row',
                        justifyContent: 'space-between'

                    }}>
                        <h3>Ouders</h3>
                        <button className="btn btn-success"
                                disabled={ouders >= 2}
                                style={{alignSelf: 'center'}}
                                type="button"
                                onClick={this.addOuder.bind(this)}>
                            add
                        </button>
                    </div>

                    <hr/>

                    {_.range(ouders).map(i => <InputField key={i} id={`ouder${i}`} label="Ouder bsn"/>)}

                    { ouders > 0 &&
                    <button className="btn btn-danger" style={{float: 'right'}}
                            onClick={() => this.setState({ouders: (ouders - 1)})}>
                        Verwijder ouder
                    </button>
                    }

                    <SubmitButton />
                </form>
            </div>
        )
    }
};