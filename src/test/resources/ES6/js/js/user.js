import {doubleIt} from './tools.js'

class User {
    constructor(firstName, lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.visits = 0;
    }

    fullName() {
        return `${this.firstName} ${this.lastName}`;
    }

    doubleVisit() {
        return doubleIt(this.visits);
    }

    visiting() {
        return this.visits += 1;
    }
}
export {User};
