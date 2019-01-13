let zesty = Java.type('com.practicaldime.zesty.app.AppProvider');

let app = zesty.provide({
    appctx: "/bricks",
    assets: "",
    engine: "freemarker"
});

print('zesty bricks provider completed');
var router = app.router();

router.get('/', function (req, res) {
	print('RENDER THE GAME');
    res.render('bricks', {});
});

router.listen(8082, 'localhost', function(result){
    print(result);
});