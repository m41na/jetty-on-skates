//load('../jvm-npm.js');

let Template7 = require('./template7.js');

//var template = new java.lang.String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("template.html")));

var template = "<p>Hello, my name is {{firstName}} {{lastName}}</p>";
 
// compile it with Template7
var compiledTemplate = Template7.compile(template);
 
// Now we may render our compiled template by passing required context
var context = {
    firstName: 'John',
    lastName: 'Doe'
};
var html = compiledTemplate(context);

print(html);