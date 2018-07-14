load('www/jvm-npm.js');

var appViews = require('www/themes/basic/view/app-view.js');

var Date = Packages.java.util.Date;

var app = zesty.provide({assets: "assets"});

var router = app.router();

appViews.views.routes(router).scaffold("www/todoapp/src/views");

router.get('/', function(req, res) {
    res.send(app.status().concat(" @ ").concat(new Date().toString()));
});

router.listen(8080, 'localhost', function(){print('Zesty app listening on port 8080!');});