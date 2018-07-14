;
(function () {

    var appViews = require('www/themes/basic/view/app-view.js');

    var HomeView = function () {
        appViews.AbstractView.call();
        print('extending abstract view instance');
    };

    // TodosView derives from AbstractView
    HomeView.prototype = Object.create(appViews.AbstractView.prototype);
    HomeView.prototype.constructor = HomeView;

    HomeView.prototype.getContent = function () {
        return "hello there!!!!!!!!!";
    };

    //override getTitle
    HomeView.prototype.getTitle = function () {
        return "Sweet home!";
    };

    //override handler method
    HomeView.prototype.handle = function (req, res) {
        res.setContentType("text/html;charset=utf-8");
        res.send(this.getContent());
    };

    appViews.views.build("get", "/home", "", HomeView);
})();


