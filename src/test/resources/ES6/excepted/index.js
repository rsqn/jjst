class ModuleRegistry {

    registryMap;

    constructor() {
        this.registryMap = new Map();
    }

    register(name, script) {
        if (!this.registryMap.has(name)) {
            this.registryMap.set(name, script);
        }
    }

    get(name) {
        return this.registryMap.get(name);
    }

}

let mr = new ModuleRegistry();
mr.register('./tools.js', {
    doubleIt(n) {
        return n * 2;
    }
});

mr.register('./js/tools.js', {
    doubleIt(n) {
        return n * 2;
    }
});

let UserFn = function() {

    let firstName, lastName;
    let visits;

    return {
        User: function(_firstName, _lastName) {
            firstName = _firstName;
            lastName = _lastName;
            visits = 0;
        }, 
        fullName: function() {
            return `${firstName} ${lastName}`;
        },
        doubleAge: function () {
            return mr.get('./tools.js').doubleIt(visits);
        },
        visiting: function() {
            return visits += 1;
        }
    };
}

console.log('UserFn: ' + UserFn);

mr.register('./js/user.js', UserFn);

// emulate the User class
let UserClass = mr.get('./js/user.js');

console.log('');
console.log('====== user 1 ========');
// emulate the new instance
let user1 = UserClass();
user1.User('James', 'Bond');

console.log(`user1.firstName: ${user1.firstName}`);
console.log(`user1.fullName: ${user1.fullName()}`);
console.log(`user1.visiting: ${user1.visiting()}`);
console.log(`user1.visiting: ${user1.visiting()}`);

console.log('');
console.log('====== user 2 ========');

let user2 = UserClass();
user2.User('Second', 'User');

console.log(`user2.fullName: ${user2.fullName()}`);
console.log(`user2.visiting: ${user2.visiting()}`);
console.log(`user2.visiting: ${user2.visiting()}`);
console.log(`user2.visiting: ${user2.visiting()}`);
console.log(`user2.visiting: ${user2.visiting()}`);

console.log('');
console.log('====== reaccess ========');

// user1
console.log(`user1.fullName: ${user1.fullName()}`);
console.log(`user1.doubleAge: ${user1.doubleAge()}`);

// user2
console.log(`user2.fullName: ${user2.fullName()}`);
console.log(`user2.doubleAge: ${user2.doubleAge()}`);


// If it is not a class export will return function directly
console.log('');
console.log('====== tools.js ========');
console.log(mr.get('./js/tools.js').doubleIt(2));
