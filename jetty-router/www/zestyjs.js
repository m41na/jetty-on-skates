let zesty = Java.type('com.practicaldime.zesty.app.AppProvider');

function ping() {
    print("jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/jetty-router-0.1.0-shaded.jar zestyjs.js");
    print("jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/jetty-router-0.1.0-shaded.jar -J-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9099 zestyjs.js");
    //-J-agentlib:jdwp=transport=dt_shmem,server=y,suspend=n --> using terminal
    //-J-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9099 --> using socket
    //-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9009 --> using socket <= jdk1.4
    //java -jar ../target/jetty-router-0.1.0-shaded.jar zestyjs.js
    //java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9099 -jar ../target/jetty-router-0.1.0-shaded.jar zestyjs.js 
    // $/opt/graalvm-ee-1.0.0-rc9/bin/js --jvm --jvm.cp=../target/jetty-router-0.1.0-shaded.jar  zestyjs.js 
    // $/opt/graalvm-ee-1.0.0-rc9/bin/node --jvm --jvm.cp=../target/jetty-router-0.1.0-shaded.jar  zestyjs.js 
    // $/opt/graalvm-ee-1.0.0-rc9/bin/jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/jetty-router-0.1.0-shaded.jar zestyjs.js

    return 'zestyjs ping invoked from js';
}

let Date = Java.type('java.util.Date');
let DateFormat = Java.type('java.text.SimpleDateFormat');

function now() {
    return new DateFormat("hh:mm:ss a").format(new Date());
}

function onSuccess(msg) {
    print(msg);
};

function onError(msg) {
    print(msg);
};

load('./lib/jvm-npm.js');
load('./zjdbc.js');

var db = new dao.DB();
db.init();
print('created todos DAO instance');

let app = zesty.provide({
    appctx: "/app",
    assets: "",
    engine: "freemarker"
});

print('zesty provider completed');
var router = app.router();

router.get('/', function (req, res) {
    res.send(app.status().concat(" @ ").concat(new Date().toString()));
});

router.get('/ping', function (req, res) {
    res.redirect(app.resolve("/ping/server"));
});

router.get('/ping/:name', function (req, res) {
    var name = req.param(":name");
    res.send("ping".concat(" by ").concat(name));
});

router.get('/pong', function (req, res) {
    res.redirect(app.resolve("/wowza"));
});

router.get('/wowza', function (req, res) {
    res.render('wowza', {
        title: 'Wowza!',
        number: Math.floor(Math.random() * 10)
    });
});

router.get('/zesty', function (req, res) {
    res.download("/", "zestyjs.js", null, function () {
        print('download done!');
    });
});

router.get('/upload', function (req, res) {
    res.render('upload', {});
});

router.post('/upload', '', 'multipart/form-data', function (req, res) {
    var dest = req.param('destination');
    req.upload(dest);
    res.redirect(app.resovle("/upload"));
});

router.get("/todos", function (req, res) {
    let start = req.param('start') || 0;
    let size = req.param('size') || 10;
    db.retrieveByRange(start, size, function (tasks, msg) {
        var model = {
            "tasks": tasks,
            title: "TodosJs List"
        };
        res.render("todos", model);
    }, onError);
});

router.get("/todos/done", function (req, res) {
    var completed = req.param("complete");
    db.retrieveByDone(completed, function (tasks, msg) {
        var model = {
            "tasks": tasks
        };
        res.render("todos", model);
    }, onError);
});

router.get("/todos/refresh", function (req, res) {
    let start = req.param('start') || 0;
    let size = req.param('size') || 10;
    db.retrieveByRange(start, size, function (tasks, msg) {
        res.json(tasks);
    }, onError);
});

router.post("/todos", function (req, res) {
    var task = req.param("task");
    db.createTask(task, onSuccess, onError);

    db.retrieveTask(task, function (created, msg) {
        res.json(created);
    }, onError);
});

router.put("/todos/done", function (req, res) {
    var task = req.param("task");
    var done = req.param("complete");
    db.updateDone(task, done, onSuccess, onError);

    db.retrieveTask(task, function (updated, msg) {
        res.json(updated);
    }, onError);
});

router.put("/todos/rename", function (req, res) {
    var task = req.param("task");
    var newName = req.param("newName");
    db.updateName(task, newName, onSuccess, onError);

    db.retrieveTask(newName, function (updated, msg) {
        res.json(updated);
    }, onError);
});

router.delete("/todos", function (req, res) {
    var task = req.param("name");
    db.deleteTask(task, onSuccess, onError);

    res.redirect(303, app.resolve("/todos/refresh"));
});

router.wordpress("/var/www/wordpress", "http://localhost:9000");

router.listen(8080, 'localhost', function(result){
    print(result);
});