
const DECEASED_URL = "persoon/overlijden";
const GEBOORTE_URL = "persoon/geboorte";

class PersonService {

    constructor() {
        this.url = "http://localhost:8123/"
    }

    _executePost(urlContext, body) {
        fetch(this.url + urlContext, {
            method: 'post',
            body
        })
            .then(response => console.log(response))
            .catch(err => console.error(err));
    }

    postCreateNaturalPerson(body) {
        this._executePost(GEBOORTE_URL, body);
    }

    postDeceasedPerson(body = {bsn: '', datum: '10/10/1940', gemeente: "0505"}) {
        this._executePost(DECEASED_URL, body);
    }
}

export default new PersonService()