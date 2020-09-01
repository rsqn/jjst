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


mr.register('./js/user.js',
    class User {
        constructor(firstName, lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.visits = 0;
        }

        fullName() {
            return `${this.firstName} ${this.lastName}`;
        }

        doubleAge() {
            return mr.get('./tools.js').doubleIt(this.visits);
        }

        visiting() {
            return this.visits += 1;
        }
    }
)

let tools = mr.get('./js/tools.js');
let User = mr.get('./js/user.js')

let jb = new User('James', 'Bond');

console.log(jb.fullName());
console.log(jb.visiting());
console.log(jb.visiting());
console.log(jb.doubleAge())


console.log(tools.doubleIt(2));
