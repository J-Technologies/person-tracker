import React from "react";
import InputField from "./form/InputField";
import SubmitButton from "./form/SubmitButton";


export default ({}) => {

    return (
        <div>
            <form className="form-horizontal" id="searchPerson" onSubmit={
                e => {
                    e.preventDefault();
                }}>
                <InputField id="bsn" label="BSN nummer"/>

                <SubmitButton />

            </form>
            <div className="results">
                some results....

            </div>
        </div>
    )
}