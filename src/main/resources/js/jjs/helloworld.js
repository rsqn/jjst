// this is the index source
import {doubleIt} from '/js/jjs/common/Math.js';

function sayHello() {
    const h1 = '<h1>Hello world</h1>';
    const _double = `<p>Double of 10 is: ${doubleIt(10)}</p>`;
    const root = document.getElementById('root');
    root.innerHTML = h1 + _double;
}


