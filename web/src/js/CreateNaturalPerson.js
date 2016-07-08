import React from "react";

export default () => {

    const postCreateNaturalPerson = () => fetch("http://localhost:8123/persoon/geboorte", {
        method: 'post',
        body: new FormData(document.getElementById('createPerson'))
    })
        .then(response => console.log(response))
        .catch(err => console.error(err));

    return (
        <div>
            <h1>Creeër een Natuurlijk persoon</h1>
            <hr/>
            <form className="form-horizontal" id="createPerson" onSubmit={
                e => {
                    e.preventDefault();
                    postCreateNaturalPerson();
            }}>
                <div className="form-group">
                    <label for="voornaam" className="col-sm-2 control-label">Voornaam</label>
                    <div className="col-sm-10">
                        <input type="text" className="form-control" name="voornaam" id="voornaam"
                               placeholder="Voornaam"/>
                    </div>
                </div>

                <div className="form-group">
                    <label for="achternaam" className="col-sm-2 control-label">Achternaam</label>
                    <div className="col-sm-10">
                        <input type="text" className="form-control" name="achternaam" id="achternaam"
                               placeholder="Achternaam"/>
                    </div>
                </div>

                <div className="form-group">
                    <label for="radio1" className="col-sm-2 control-label">Geslacht</label>
                    <div className="col-sm-10">

                        <div className="radio">
                            <label>
                                <input type="radio" name="geslacht" id="onbekend" defaultValue="onbekend"
                                       defaultChecked={true}/>
                                Onbekend
                            </label>
                        </div>
                        <div className="radio">
                            <label>
                                <input type="radio" name="geslacht" id="vrouw" defaultValue="vrouw"/>
                                Vrouw
                            </label>
                        </div>
                        <div className="radio">
                            <label for="man">
                                <input type="radio" name="geslacht" id="man" defaultValue="man"/>
                                Man</label>
                        </div>
                    </div>
                </div>

                <div className="form-group">
                    <label for="geboortedatum" className="col-sm-2 control-label">Geboortedatum</label>
                    <div className="col-sm-10">
                        <input type="date" id="geboortedatum" className="form-control"/>
                    </div>
                </div>

                <input name="partij" type="hidden" value="000505"/>

                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <button type="submit" className="btn btn-default">Opslaan</button>
                    </div>
                </div>
            </form>
        </div>
    )

};