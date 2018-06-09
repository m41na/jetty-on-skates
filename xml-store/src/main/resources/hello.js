print('Hello World!');

var jsapp = Java.type('com.jarredweb.xmlstore.js.JsApp');

var result = jsapp.hello('John Doe');
print(result);

var fun1 = function(name) {
    print('Hi there from Javascript, ' + name);
    return "greetings from javascript";
};

var fun2 = function (object) {
    print("JS Class Definition: " + Object.prototype.toString.call(object));
};

//var js = Js.static;
var js = Java.type('com.jarredweb.xmlstore.js.Js');

var express = js.require('express');

var app = new express();

