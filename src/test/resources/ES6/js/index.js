// File: index.js

import {tools} from './js/tools.js'
import {User} from './js/user.js'

let me = new User('James', 'Bond');

console.log(me.fullName());
console.log(me.visiting());
console.log(me.visiting());
console.log(me.doubleAge());
console.log(tools.doubleIt(2));
