
const DECEASED_URL = "persoon/overlijden";
const GEBOORTE_URL = "persoon/geboorte";
const HUWELIJK_URL = "persoon/huwelijk";

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

    createNaturalPerson(body) {
        this._executePost(GEBOORTE_URL, body);
    }

    putDeceasedPerson(body = {bsn: '', datum: '10/10/1940', gemeente: "0505"}) {
        this._executePost(DECEASED_URL, body);
    }

    createHuwelijk(body) {
        this._executePost(HUWELIJK_URL, body);
    }
}

export default new PersonService()