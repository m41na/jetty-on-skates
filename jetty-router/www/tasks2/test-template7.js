load('www/tasks2/template7.js');

let markup = [
"<div class=\"list-block\">" +
  "<ul>" +
    "{{#each items}}" +
    "<li class=\"item-content\">" +
      "<div class=\"item-inner\">" +
        "<div class=\"item-title\">{{title}}</div>" +
      "</div>" +
    "</li>" +
    "{{/each}}" +
  "</ul>" +
"</div>"].join("");

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




