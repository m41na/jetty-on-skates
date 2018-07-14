load('www/themes/basic/view/ejs.js');

let people = ['geddy', 'neil', 'alex'];

html = ejs.render('<%= people.join(", "); %>', {people: people});

print(html);

var markup = [
"<ul>",
  "<% users.forEach(function(user){ %>",
    "<li><%= user.name %></li>",
  "<% }); %>",
"</ul>"].join("");

html = ejs.render(markup, {users: [{id: 1, name: 'steve'}, {id: 2, name: 'mike'}, {id: 3, name: 'Zamu'}]});

print(html);

markup  = [
    "<ul id=\"myUL\">",
        "<% tasks.forEach(function(task){ %>",
        "<li data-name=\"<%= task.name %>\" <% if (task.completed) { %>class=\"checked\"<% } %>><%= task.name %></li>",
        "<% }); %>",
    "</ul> "].join(" ");

html = ejs.render(markup, {tasks: [{name: 'dance', completed: true}, {name: 'swim', completed: false}, {name: 'bake', completed: true}]});

print(html);