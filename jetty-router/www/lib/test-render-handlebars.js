const window = {};

load(dist + '/jvm-npm.js');
load(dist + '/handlebars.js');
var source = "\n    <div class=\"wrapper\">\n        <div id=\"myDIV\" class=\"header\">\n            <h2>My To Do List</h2>\n            <form id=\"tasks_form\">\n                <input type=\"text\" name=\"task\" placeholder=\"Title...\">\n                <span onclick=\"createTask()\" class=\"addBtn\">Add</span>\n            </form>\n        </div>\n\n        <ul id=\"myUL\">\n            {{#each task}}\n            <li data-name=\"{{ name }}\" {{#if completed}}class=\"checked\"{{/if}}\">{{ name }}</li>\n            {{/each}}\n        </ul> \n    </div>\n";

let renderString = function(model) {
  var context = JSON.parse(model);
  context.tasks.forEach(e=>print(e.name));
  var template = Handlebars.compile(source);
  var html = template(context);
  return html;
};

print(renderString(model));
