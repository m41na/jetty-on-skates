load('www/tasks2/tmpl.min.js');
load('www/themes/basic/view/app-view.js');

var view = new appView.TodosView({});

let markup = ["<h3>{%=o.title%}</h3>" +
"<p>Released under the" +
"<a href=\"{%=o.license.url%}\">{%=o.license.name%}</a>.</p>" +
"<h4>Features</h4>" +
"<ul>" +
"{% for (var i=0; i<o.features.length; i++) { %}" +
    "<li>{%=o.features[i]%}</li>" +
"{% } %}" +
"</ul>"].join("");

let context = {
    "title": "JavaScript Templates",
    "license": {
        "name": "MIT license",
        "url": "https://opensource.org/licenses/MIT"
    },
    "features": [
        "lightweight & fast",
        "powerful",
        "zero dependencies"
    ]
};

let fs = {};

fs.readFileSync = function(){
    return view.loadMarkup("tasks2/test-tmpl.html");;
};

let filename = "test-tmpl.html";

tmpl.load = function(){
    //return markup;
    print("Loading " + filename);
    return fs.readFileSync(filename, "utf8");
};

print(tmpl('template', context));




