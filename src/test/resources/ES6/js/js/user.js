import {doubleIt} from './tools.js'

export class User {
    constructor(firstName, lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.visits = 0;
    }

    fullName() {
        return concatString(this.firstName, this.lastName);
    }

    doubleVisit() {
        return doubleIt(this.visits);
    }

    visiting() {
        return this.visits += 1;
    }
}

function concatString(str1, str2) {
    return `${str1} ${str2}`;
}

// TODO add support
//export {User};
