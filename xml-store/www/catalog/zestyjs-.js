function ping(){
    print("sending response from js");
    return 'ping invoked from js';
}

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
    res.redirect("/wowza", 302);
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

router.post('/upload', function(req, res) {
    res.upload('upload');
});

router.listen(8080, 'localhost', function(){print('Zesty app listening on port 8080!')})
