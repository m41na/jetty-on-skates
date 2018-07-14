//declare global variable to export functions
var appView = {};

//load ejs 
var ejs = require('ejs.js');
appView.ejs = ejs;

(function () {
    var ViewException = Packages.com.jarredweb.jesty.view.ViewException;
    var FtlViewEngine = Packages.com.jarredweb.jesty.view.ftl.FtlViewEngine;
    var Template = Packages.freemarker.template.Template;
    var ByteArray = Java.type("byte[]");

    var AbstractView = function () {
        print('creating abstract view instance');
    };

    /**
     * name of view engine in use
     * return as string
     */
    AbstractView.prototype.getEngine = function () {
        return "freemarker";
    };

    /**
     * path to layout file to use
     * return as string
     */
    AbstractView.prototype.getLayout = function () {
        return "/themes/basic/basic.ftl";
    };

    /**
     * title of generated page
     * return as string
     */
    AbstractView.prototype.getTitle = function () {
        return "Hello";
    };

    /**
     * charset type to assign generated page
     * return as string
     */
    AbstractView.prototype.getCharset = function () {
        return "UTF-8";
    };

    /**
     * describe type of response to tramsmit back
     * return as string
     */
    AbstractView.prototype.getContentType = function () {
        return "text/html;charset=utf-8";
    };

    /**
     * generate response content
     * return type that refelcts the content type
     */
    AbstractView.prototype.getContent = function () {

    };

    /**
     * create model object for the view
     * return as map
     */
    AbstractView.prototype.getModel = function () {

    };

    /**
     * path to output file which to save generated content
     * return as string
     */
    AbstractView.prototype.getDestFile = function () {
        return "www/cache/hello.html";
    };

    /**
     * write generated content to file
     * template: path to output generated content
     * content: the use generated content
     * return void
     */
    AbstractView.prototype.writeTemplate = function (template, content) {
        var out, outChannel;
        try {
            out = new java.io.FileOutputStream(new java.io.File("www", template));
            outChannel = java.nio.channels, Channels.newChannel(out);
            var buffer = java.nio.ByteBuffer.wrap(content.getBytes());
            outChannel.write(buffer);
        } catch (error) {
            throw new ViewException("AbstractView.loadMarkup: ".concat(error));
        } finally {
            if (outChannel)
                outChannel.close();
            if (out)
                out.close();
        }
    };

    /**
     * load markup to use for generating content
     * template: path to markup file to use for generating content if need be
     * return as string
     */
    AbstractView.prototype.loadMarkup = function (template) {
        var ins, inChannel;
        try {
            ins = new java.io.FileInputStream(new java.io.File("www", template));
            inChannel = java.nio.channels.Channels.newChannel(ins);
            var bytes = new java.io.ByteArrayOutputStream();
            var buffer = java.nio.ByteBuffer.allocate(4096);
            var bytesRead = inChannel.read(buffer); //read into buffer.
            while (bytesRead != -1) {
                buffer.flip();  //make buffer ready for read

                if (buffer.hasRemaining()) {
                    var xfer = new ByteArray(buffer.limit()); //transfer buffer bytes to a different aray
                    buffer.get(xfer);
                    bytes.write(xfer); // read entire array backing buffer
                }

                buffer.clear(); //make buffer ready for writing
                bytesRead = inChannel.read(buffer);
            }
            //return file content
            var content = new java.lang.String(bytes.toByteArray());
            return content;
        } catch (error) {
            throw new ViewException("AbstractView.loadMarkup: ".concat(error));
        } finally {
            if (inChannel)
                inChannel.close();
            if (ins)
                ins.close();
        }
    };

    /**
     * merge the markup content together with the model data to generate view content
     * name: unique name for generated markup, especially for caching reasons:
     * markup: markup to use for generating view content
     * model: association of key-value pairs to resolve view values
     * return as string array
     */
    AbstractView.prototype.mergeTemplate = function (name, markup) {
        try {
            switch (this.getEngine()) {
                case "freemarker":
                    var template = new Template(name, markup, FtlViewEngine.getConfiguration().getEnvironment());
                    var output = new java.io.StringWriter();
                    template.process(this.getModel(), output);
                    return output.toString();
                case "jtwig":
                    return null;
                default:
                    throw new ViewException("unsupported template engine");
            }
        } catch (error) {
            error.printStackTrace(java.lang.System.err);
            throw new ViewException(error);
        }
    };

    /**
     * get list of metatags to apply to generated page
     * return as string array
     */
    AbstractView.prototype.getMetaTags = function () {
        return [];
    };

    /**
     * get list of styles to apply to generated page
     * return as string array
     */
    AbstractView.prototype.getStyles = function () {
        return [];
    };

    /**
     * get list of scripts to apply to generated page
     * top: if true add inside <head> else add at the end of <body>
     * return as string array
     */
    AbstractView.prototype.getScripts = function (top) {
        return top ? [] : [];
    };

    /**
     * Handle request and send back response
     * req: servlet request associated with http request
     * res: servlet response associated with http response
     */
    AbstractView.prototype.handle = function (req, res) {
        res.send("You need to implement a handler for this view");
    };

    /**
     * generic error handler method
     */
    AbstractView.prototype.handle.onError = function (msg) {
        print(msg);
    };

    //export class using global variable
    appView.AbstractView = AbstractView;


    //create app-helper class to add handlers dynamically
    var ViewHandlers = function () {
        this.router;
    };

    ViewHandlers.prototype.routes = function (router) {
        this.router = router;
        return this;
    };

    //create and add handler to routes
    ViewHandlers.prototype.build = function (method, path, contentType, view) {
        switch (method.toLowerCase()) {
            case "get":
                this.router.get(path, '', contentType, function (req, res) {
                    //handle request in view
                    new view().handle(req, res);
                });
                break;
            default:
            {
                throw new ViewException('could not create view controller');
            }
        }
        ;
    };

    //load all file in folder to build the views
    ViewHandlers.prototype.scaffold = function (folder) {
        var files = new java.io.File(folder).listFiles();
        for each(var file in files) {
            if (file.isDirectory()) {
                this.scaffold(file.getPath());
            } else {
                print('-----------loading ' + file.getPath());
                load(file);
            }
        }
    };

    //export class using global variable
    appView.views = new ViewHandlers();

    //export classes using module
    module.exports = appView;
})();