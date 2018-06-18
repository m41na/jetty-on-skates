function ping(){
    print("sending response from js");
    return 'ping invoked from js';
}

var Date = Packages.java.util.Date

print(zesty.status().concat(" @ ").concat(new Date().toString()));

var router = zesty.router();

router.get('/ping', function(req, res) {
    res.setContentType("text/html; charset=utf-8");
    var out = res.getWriter();
    out.println(zesty.status().concat(" @ ").concat(new Date().toString()));
});

router.get('/ping', function(req, res) {
    res.setContentType("text/html; charset=utf-8");
    var out = res.getWriter();
    out.println(zesty.status().concat(" @ ").concat(new Date().toString()));
});

router.post('/login', function(req, res){
    
});

router.start()
