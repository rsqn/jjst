// File: index.js

import {doubleIt} from './js/tools.js'
import {User} from './js/user.js'
import {UrlTools} from './js/api/tools.js'
import * as math from './js/api/math.js'

// emulate the User class
console.log('');
console.log('====== user 1 ========');
let user1 = new User('James', 'Bond');

console.log(`user1.firstName: ${user1.firstName}`); // member should be protected by closure!
console.log(`user1.fullName(): ${user1.fullName()}`);
console.log(`user1.visiting(): ${user1.visiting()}`);
console.log(`user1.visiting(): ${user1.visiting()}`);
console.log(`user1.doubleVisit(): ${user1.doubleVisit()}`);

console.log('');
console.log('====== user 2 ========');

let user2 = new User('Second', 'User');

console.log(`user2.fullName(): ${user2.fullName()}`);
console.log(`user2.visiting(): ${user2.visiting()}`);
console.log(`user2.visiting(): ${user2.visiting()}`);
console.log(`user2.visiting(): ${user2.visiting()}`);
console.log(`user2.visiting(): ${user2.visiting()}`);
console.log(`user2.doubleVisit(): ${user2.doubleVisit()}`);

console.log('');
console.log('====== reaccess ========');

// user1
console.log(`user1.fullName(): ${user1.fullName()}`);
console.log(`user1.doubleVisit(): ${user1.doubleVisit()}`);

// user2
console.log(`user2.fullName(): ${user2.fullName()}`);
console.log(`user2.doubleVisit(): ${user2.doubleVisit()}`);


// If it is not a class export will return function directly
console.log('');
console.log('====== tools.js ========');
console.log(`tools.doubleIt(): ${doubleIt(2)}`);

console.log('');
console.log('====== tools.js as UrlTools class========');
let urlTools = new UrlTools();
console.log(`urlTools.getUrlParameter(): ${urlTools.getUrlParameter('https://localhost:8080/something')}`);

console.log('');
console.log('====== math.js as math all functions========');
console.log(`math.addToOne(): ${math.addOne(10)}`);
console.log(`math.addToOne(): ${math.minusOne(10)}`);
console.log(`math.addToOne(): ${math.timesOne(10)}`);
