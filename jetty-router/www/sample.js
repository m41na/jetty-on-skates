let Sample = Java.type('com.practicaldime.jesty.app.Sample');

let Date = Java.type('java.util.Date');
let DateFormat = Java.type('java.text.SimpleDateFormat');

function now() {
    return new DateFormat("hh:mm:ss a").format(new Date());
}

let app = new Sample();

let func = function(val, mod){
    switch(mod){
        case 0:
            return val.toUpperCase();
        case 1:
            return val.split("").reverse().join("");
        default:
            return now() + " : " + val;
    }
};

print(app.echo("Test App", -1, func));