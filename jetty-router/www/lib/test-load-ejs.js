load('www/lib/jvm-npm.js');
let fs = require('www/lib/node_modules/file-system/file-system.js');


var template = "<p>Hello, my name is {{firstName}} {{lastName}}</p>";
let ejs = load('www/lib/ejs.js'),
    html = ejs.render(template, {
    firstName: 'John',
    lastName: 'Doe'});

print(html);