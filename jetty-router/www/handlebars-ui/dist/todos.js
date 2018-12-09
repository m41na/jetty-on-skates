"use strict";

var window = {};

load(dist + '/jvm-npm.js');
let render = require(dist + '/handlebars.js');

var source = "\n    <div class=\"wrapper\">\n        <div id=\"myDIV\" class=\"header\">\n            <h2>My To Do List</h2>\n            <form id=\"tasks_form\">\n                <input type=\"text\" name=\"task\" placeholder=\"Title...\">\n                <span onclick=\"createTask()\" class=\"addBtn\">Add</span>\n            </form>\n        </div>\n\n        <ul id=\"myUL\">\n            {{#each task}}\n            <li data-name=\"{{ name }}\" {{#if completed}}class=\"checked\"{{/if}}\">{{ name }}</li>\n            {{/each}}\n        </ul> \n    </div>\n";

function renderString(json) {
  print('JSON PAYLOAD ==>> ' + json);
  let context = JSON.parse(json);
  print('HANDLEBARS message ==>> ' + render.log(Handlebars.logger.DEBUG, 'handlebars is working'));
  var template = render.compile(source);
  var html = template(context);
  return html;
};
//# sourceMappingURL=todos.js.map
