const window = {};

const Handlebars = require('www/lib/handlebars.js');

var source = "<div class='body'>{{body}}</div>"

var template = Handlebars.compile(source);

var context = {title: "My New Post", body: "This is my first post!"};
var html    = template(context);

print(html);