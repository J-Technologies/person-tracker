import React, {Component} from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";


export default class SearchNaturalPerson extends Component {

    constructor(props) {
        super(props);
        this.state = {results: []}
    }

    render() {
        const {results}= this.state;

        return (
            <div>
                <h1>Zoek natuurlijk persoon op</h1>
                <form className="form-horizontal" id="searchPerson" onSubmit={
                    e => {
                        e.preventDefault();
                        this.setState({
                            results: [
                                {bsn: 1234, fullName: "Gerard de Leeuw"},
                                {bsn: 4321, fullName: "Eric-jan Malotoux"},
                                {bsn: 2345, fullName: "Teije van Sloten"}
                            ]
                        });
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
                        </tr>
                        </thead>
                        <tbody>
                        {results.map(person =>
                            <tr key={person.bsn}>
                                <td>{person.bsn}</td>
                                <td>{person.fullName}</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}