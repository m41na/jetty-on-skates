function ping(){
    print("jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/Zestyjs.jar zesty.js");
    return 'zestyjs ping invoked from js';
}

function now(){
    return new java.text.SimpleDateFormat("hh:mm:ss").format(new java.util.Date())
}

function onSuccess(msg){
    print(msg);
};

function onError(msg){
    print(msg);
};

load('www/zjdbc.js');

var todos = new dao.Zjdbc();
todos.initDataSource();
print('created todos DAO instance');

var Date = Packages.java.util.Date;
var Paths = Packages.java.nio.file.Paths;

print(zesty.status().concat(" @ ").concat(new Date().toString()));

var www = Paths.get(__dirname, 'www');

zesty.assets(www);
//zesty.use('view', 'jtwig');

var router = zesty.router();

router.get('/ping', function(req, res) {
    res.send(zesty.status().concat(" @ ").concat(new Date().toString()));
});

router.get('/ping/:name', function(req, res) {
    var name = res.getParam(":name");
    res.send("ping".concat(" by ").concat(name));
});

router.get('/pong', function(req, res) {
    res.redirect("/wowza");
});

router.get('/wowza', function (req, res) {
    res.render('wowza', { title: 'Wowza!', number:  Math.floor(Math.random() * 10) });
});

router.get('/zesty', function(req, res) {
    res.download("www/zestyjs.js", null, null, function(){print('download done!')});
});

router.get('/upload', function(req, res) {
    res.render('upload', {});
});

router.post('/upload', '', 'multipart/form-data', function(req, res) {
    var dest = req.param('destination');
    req.upload(dest);
    res.redirect("/upload");
});

router.get("/todos", function(req, res) {
    todos.retrieveByRange(0, 100, function(tasks, msg){
        var model = {"tasks": tasks};
        res.render("todos", model);  
    }, onError);
});

router.get("/todos/done", function(req, res) {
    var completed = req.param("complete");
    todos.retrieveByDone(completed, function(tasks, msg){
        var model = {"tasks": tasks};
        res.render("todos", model);
    }, onError);
});

router.get("/todos/refresh", function(req, res) {
    todos.retrieveByRange(0, 100, function(tasks, msg){
        res.json(tasks);
    }, onError);
});

router.post("/todos", function(req, res) {
    var task = req.param("task");
    todos.createTask(task, onSuccess, onError);

    todos.retrieveTask(task, function(created, msg){
        res.json(created);
    }, onError);
});

router.put("/todos/done", function(req, res) {
    var task = req.param("task");
    var done = req.param("complete");
    todos.updateDone(task, done, onSuccess, onError);

    todos.retrieveTask(task, function(updated, msg){
        res.json(updated);
    }, onError);
});

router.put("/todos/rename", function(req, res) {
    var task = req.param("task");
    var newName = req.param("newName");
    todos.updateName(task, newName, onSuccess, onError);

    todos.retrieveTask(newName, function(updated, msg){
        res.json(updated);
    }, onError);
});

router.delete("/todos", function(req, res) {
    var task = req.param("name");
    todos.deleteTask(task, onSuccess, onError);

    res.redirect("/todos/refresh");
});

router.listen(8080, 'localhost', function(){print('Zesty app listening on port 8080!')})