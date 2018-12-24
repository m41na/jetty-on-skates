load(dist + '/jvm-npm.js');

var window = {};

load(dist + '/handlebars.js');

let source = `
    <div class="wrapper">
        <div id="myDIV" class="header">
            <h2>My To Do List</h2>
            <form id="tasks_form">
                <input type="text" name="task" placeholder="Title...">
                <span onclick="createTask()" class="addBtn">Add</span>
            </form>
        </div>

        <ul id="myUL">
            {{#each task}}
            <li data-name="{{ name }}" {{#if completed}}class="checked"{{/if}}">{{ name }}</li>
            {{/each}}
        </ul> 
    </div>
`;

const renderString = function(model){
    var context = JSON.parse(model);
    var template = Handlebars.compile(source);
    var html = template(context)
    return html;
}

