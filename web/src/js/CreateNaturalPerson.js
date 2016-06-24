import React from "react";

export default () => {

    return (
        <div>
            <h1>Creeër een Natuurlijk persoon</h1>
            <hr/>
            <form onSubmit={
                e => {
                    e.preventDefault();
                    fetch("http://localhost:8123/persoon/geboorte", {method: 'get'})
                        .then(response => console.log(response))
                        .catch(err => console.log(err))
            }}>
                <label>Voornaam</label> <input name="voornaam" type="text"/>
                <label>Achternaam</label> <input name="achternaam" type="text"/>
                <label>Geslacht</label> <input name="geslacht" type="text"/>
                <label>Geboortedatum</label> <input name="geboortedatum" type="date"/>
                <input name="partij" type="hidden" value="000505"/>

                <button>submit</button>
            </form>
        </div>
    )
};