//jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/jetty-router-0.1.0-shaded.jar:./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar zjdbc-test.js

load('zjdbc-spring.js');
//********************************************//
// Initialize db for testing
//********************************************//
var assert = org.junit.Assert;

var math = Java.type('java.lang.Math');

function random(min, max){
    return (math.random() * ( max - min )) + min;
}

var config = {
    "driverClass": "org.h2.Driver",
    "url": "jdbc:h2:./data/todos.spring.js_db_test;DB_CLOSE_DELAY=-1",
    "username": "sa",
    "password": "sa"
};

let db = new dao.DB(config);
db.init();

db.createTable([
    "CREATE TABLE IF NOT EXISTS tbl_todos (",
    "  task varchar(25) NOT NULL,",
    "  completed boolean DEFAULT false,",
    "  date_created datetime default current_timestamp,",
    "  PRIMARY KEY (task)",
    ")"
].join(""), (res,msg)=>print(res,msg), (err)=>print(err));

var data = ['buy milk', 'work out', 'watch game', 'hit gym', 'go to meeting'];

//clear database records before commencing tests
db.clearAllTasks((res, msg)=>{
    print(res, msg);
    //insert new batch of test data
    db.createTasks(data, (res, msg)=>print(res, msg), (error)=>print(error));
}, (error)=>print(error));

//********************************************//
// Test methods in db API
//********************************************//
function testRetrieveTask(test, name, onSuccess, onError){
    if(test) db.retrieveTask(name, onSuccess, onError);
}

testRetrieveTask(true, 'buy milk',
    function(task, msg){
        print(task);
        assert.assertEquals("Expecting 'buy milk'", 'buy milk', task.name);
        print('name='+task.name+', completed='+task.completed);
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testCreateTask(test, name, onSuccess, onError){
    if(test) db.createTask(name, onSuccess, onError);
}

let title = "task at " + random(0, 100);

testCreateTask(false, title,
    function(task, msg){
        assert.assertEquals('Expecting \'' + title + '\'', title, task.task);
        print(msg);
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testUpdateDone(test, name, done, onSuccess, onError){
    if(test) db.updateDone(name, done, onSuccess, onError);
}

testUpdateDone(true, 'watch game', true, 
    function(res, msg){
        assert.assertEquals('Expecting \'1\'', '1', res.toString());
        print(msg);
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testUpdateName(test, name, newname, onSuccess, onError){
    if(test) db.updateName(name, newname, onSuccess, onError);
}

testUpdateName(true, 'watch game', 'watch soccer', 
    function(res, msg){
        assert.assertEquals('Expecting \'1\'', '1', res.toString());
        print(msg);
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testDeleteTask(test, name, onSuccess, onError){
    if(test) db.deleteTask(name, onSuccess, onError);
}

testDeleteTask(true, 'buy milk', 
    function(res, msg){
        assert.assertEquals('Expecting \'1\'', '1', res.toString());
        print(msg);
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testRetrieveByRange(test, start, end, onSuccess, onError){
    if(test) db.retrieveByRange(start, end, onSuccess, onError);
}

testRetrieveByRange(true, 0, 10, 
    function(tasks, msg){
        assert.assertEquals("Expecting 4", "4", tasks.length.toString());
        print(msg);
        tasks.forEach(task => print('name='+task.name+', completed='+task.completed));
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

function testRetrieveByDone(test, completed, onSuccess, onError){
    if(test) db.retrieveByDone(completed, onSuccess, onError);
}

testRetrieveByDone(true, true, 
    function(tasks, msg){
        print(tasks.length, msg);
        tasks.forEach (task => {
            print('name='+task.name+', completed='+task.completed);
            assert.assertEquals('Expecting \'done\'', true, task.completed);
        });
    },
    function(msg){
        print(msg);
        assert.fail(msg);
    }
);

