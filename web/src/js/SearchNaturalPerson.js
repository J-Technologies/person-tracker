import React, {Component} from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";

export default class SearchNaturalPerson extends Component {

    constructor(props) {
        super(props);
        this.state = {results: []};
    }

    searchPerson(bsn) {
        fetch("http://localhost:8124/persoon/" + document.getElementById('bsn').value)
            .then(response => response.json())
            .then(result => this.setState({results: [result]}))
            .catch(() => this.setState({results: []}));
    }

    render() {
        const {results}= this.state;

        return (
            <div>
                <h1>Zoek natuurlijk persoon op</h1>
                <form className="form-horizontal" id="searchPerson" onSubmit={
                    e => {
                        e.preventDefault();
                        if (document.getElementById('bsn').value !== '') {
                            this.searchPerson(document.getElementById('bsn').value)
                        }
                    }}>
                    <InputField id="bsn" label="BSN nummer"/>

                    <SubmitButton label="Zoek"/>

                </form>
                <div className="results">

                    <h2>Resultaat:</h2>
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th>BSN</th>
                            <th>Naam</th>
                            <th>Overleden</th>
                        </tr>
                        </thead>
                        <tbody>
                        {results.map(person =>
                            <tr key={person.bsn}>
                                <td>{person.bsn}</td>
                                <td>{person.naam}</td>
                                <td>{person.isOverleden ? 'Ja' : 'Nee'}</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}