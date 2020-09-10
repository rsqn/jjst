import {doubleIt} from './tools.js'

export class User {
    constructor(firstName) {
        this.firstName = firstName;
    }

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

// TODO add support
//export {User};
