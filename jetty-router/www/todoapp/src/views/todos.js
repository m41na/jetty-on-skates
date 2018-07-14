;
(function () {

    load('www/zjdbc.js');
    var appViews = require('www/themes/basic/view/app-view.js');

    var todos = new dao.Zjdbc();
    todos.initDataSource();
    print('created todos DAO instance');

    var TodosView = function () {
        appViews.AbstractView.call();
        print('extending abstract view instance');
        this.model = {};
    };

    // TodosView derives from AbstractView
    TodosView.prototype = Object.create(appViews.AbstractView.prototype);
    TodosView.prototype.constructor = TodosView;

    //override getTitle
    TodosView.prototype.getTitle = function () {
        return "ToDo tasks";
    };

    //override getDestFile
    TodosView.prototype.getDestFile = function () {
        return "www/tasks/todos.html";
    };

    //override getMetaTags
    TodosView.prototype.getMetaTags = function () {
        return ["<link type=\"text/css\" href=\"/app/css/todos.css\" rel=\"stylesheet\">"];
    };

    //override getStyles
    TodosView.prototype.getStyles = function () {
        return ["<link type=\"text/css\" href=\"/app/css/todos.css\" rel=\"stylesheet\">"];
    };

    //override getScripts
    TodosView.prototype.getScripts = function (top) {
        return !top ? ["<script type=\"text/javascript\" src=\"/app/js/todos.js\"></script>"] : [];
    };

    //override getModel
    TodosView.prototype.getModel = function () {
        return this.model;
    };

    //override mergeTemplate
    TodosView.prototype.mergeTemplate = function (name, markup) {
        var context = this.getModel();
        var html = appViews.ejs.render(markup, context, {filename: true});
        return html;
    };

    //override getContent
    TodosView.prototype.getContent = function () {
        try {
            var markup = this.loadMarkup("tasks/todos.html");
            return this.mergeTemplate("todos-list", markup);
        } catch (error) {
            error.printStackTrace(java.lang.System.err);
            throw new ViewException("ZestyView.buildContent: ".concat(error));
        }
    };

    //override handler method
    TodosView.prototype.handle = function (req, res) {
        var self = this;
        var onSuccess = function (tasks, msg) {
            self.model = {"tasks": tasks, "page": self};
            res.setContentType("text/html;charset=utf-8");
            res.send(self.getContent.call(self));
        };
        todos.retrieveByRange(0, 100, onSuccess, this.onError);
    };

    appViews.views.build("get", "/todos", "", TodosView);
})();