var path = require("path");
var express = require("express");
var cors = require('cors')

var DIST_DIR = path.join(__dirname, "/"),
    PORT = 3000,
    app = express();

//Enable cors on all requests
app.use(cors())

//Serving the files on the dist folder
app.use(express.static(DIST_DIR));

//Send index.html when the user access the web
app.get("/", function(req, res) {
    res.sendFile(path.join(DIST_DIR, "index.html"));
});

app.get("/:page", function(req, res) {
    res.sendFile(path.join(DIST_DIR, `${req.params.page}.html`));
});

app.listen(PORT, function() {
    console.log(`Dev express-server listening on port ${PORT}!`)
});