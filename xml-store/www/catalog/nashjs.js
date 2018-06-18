print(java.lang.System.currentTimeMillis());

var file = new java.io.File("browser.min.js");
print(file.getAbsolutePath());
print(file.absolutePath);

var sayHello = function(name) {
    print('Hello, ' + name + '!');
    return 'hello from javascript';
};

var NashJs = Java.type('com.jarredweb.editor.app.NashJs');

// call the static method
var greetingResult = NashJs.javaHello('Mars');
print(greetingResult);

// create a new intance of MyJavaClass
var runjs = new NashJs();
var calcResult = runjs.add(1, 2);
print(calcResult);

// check out java types
NashJs.printType('Hello');
// class java.lang.String
NashJs.printType(123);
// class java.lang.Integer
NashJs.printType(12.34);
// class java.lang.Double
NashJs.printType(true);
// class java.lang.Boolean

NashJs.printType(new Number(123));
// class jdk.nashorn.internal.objects.NativeNumber
// class jdk.nashorn.api.scripting.ScriptObjectMirror
NashJs.printType(new Date());
// class jdk.nashorn.internal.objects.NativeDate
// class jdk.nashorn.api.scripting.ScriptObjectMirror
NashJs.printType(new RegExp());
// class jdk.nashorn.internal.objects.NativeRegExp
// class jdk.nashorn.api.scripting.ScriptObjectMirror
NashJs.printType({ foo: 'bar' });
// class jdk.nashorn.internal.scripts.J04
// class jdk.nashorn.api.scripting.ScriptObjectMirror

NashJs.printObjectMirror(new Number(123));
//Number: []
NashJs.printObjectMirror(new Date());
//Date: []
NashJs.printObjectMirror(new RegExp());
//RegExp: [lastIndex, source, global, ignoreCase, multiline]
NashJs.printObjectMirror({
    foo: 'bar',
    bar: 'foo'
});
//Object: [foo, bar]

calcResult = runner.add(10, 20);
print('result is ' + calcResult);
//result is 30

function Person(firstName, lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.getFullName = function() {
        return this.firstName + ' ' + this.lastName;
    }
}

var person = new Person('John', 'Doe');
NashJs.getFullName(person);
// Full name is: John Doe

// var Vue = require('www/catalog/vue.js');

// var vue = Vue.component({
//     name: 'sample',
//     data: function() {
//         return {
//             version: 1.0
//         }
//     },
//     methods: {
//         hello: function() {
//             return "you got this!";
//         }
//     }
// });

// var printVueVersion = function() {
//     return vue.data.version;
// }


// var content = readFully('catalog.xml');
// print(content);

// var files = `ls -l`; // get file listing as a string
// var lines = files.split("\n");
// for (var l in lines) {
//     var line = lines[l];
//     print(line);
// }

// print($ENV["JAVA_HOME"])
// print($ENV["PATH"])
// print($ENV.JAVA_HOME)
// print($ENV.PATH)