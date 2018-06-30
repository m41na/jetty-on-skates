load('www/zjdbc.js');

//********************************************//
// Initialize db for testing
//********************************************//

var config = {};
config.properties = {
    "jdbc.driverClassName": "org.h2.Driver",
    "jdbc.url": "jdbc:h2:./data/todos.js_db_test;DB_CLOSE_DELAY=-1",
    "jdbc.username": "sa",
    "jdbc.password": "sa"
};

function onCreate(msg){
    print(msg);
}

var zjdbc = new dao.Zjdbc()
zjdbc.initConfig(config);
zjdbc.initDataSource();

zjdbc.createTable([
    "CREATE TABLE IF NOT EXISTS tbl_todos (",
    "  task varchar(25) UNIQUE NOT NULL,",
    "  completed boolean DEFAULT false,",
    "  date_created datetime default current_timestamp,",
    "  PRIMARY KEY (task)",
    ")"
].join(""), onCreate, onCreate);

var data = [
    "merge into tbl_todos (task, completed) key(task) values ('buy milk', false);",
    "merge into tbl_todos (task, completed) key(task) values ('work out', true);",
    "merge into tbl_todos (task, completed) key(task) values ('watch game', false);",
    "merge into tbl_todos (task, completed) key(task) values ('hit gym', false);",
    "merge into tbl_todos (task, completed) key(task) values ('go to meeting', true);"
];

zjdbc.insertBatch(data, function(res, msg){print(msg);}, function(msg){print(msg);});

//********************************************//
// Test methods in db API
//********************************************//

function testRetrieveTask(test, name, onSuccess, onError){
    if(test) zjdbc.retrieveTask(name, onSuccess, onError);
}

testRetrieveTask(false, 'buy milk',
    function(task, msg){
        print('name='+task.name+', completed='+task.completed);
    },
    function(msg){
        print(msg);
    }
);

function testCreateTask(test, name, onSuccess, onError){
    if(test) zjdbc.createTask(name, onSuccess, onError);
}

testCreateTask(false, 'task at ' + now(),
    function(task, msg){
        print(msg);
    },
    function(msg){
        print(msg);
    }
);

function testUpdateDone(test, name, done, onSuccess, onError){
    if(test) zjdbc.updateDone(name, done, onSuccess, onError);
};

testUpdateDone(false, 'watch game', false, 
    function(res, msg){
        print(msg);
    },
    function(msg){
        print(msg);
    }
);

function testUpdateName(test, name, newname, onSuccess, onError){
    if(test) zjdbc.updateName(name, newname, onSuccess, onError);
};

testUpdateName(false, 'watch game', 'watch soccer', 
    function(res, msg){
        print(msg);
    },
    function(msg){
        print(msg);
    }
);

function testDeleteTask(test, name, onSuccess, onError){
    if(test) zjdbc.deleteTask(name, onSuccess, onError);
};

testDeleteTask(true, 'buy milk', 
    function(res, msg){
        print(msg);
    },
    function(msg){
        print(msg);
    }
);

function testRetrieveByRange(test, start, end, onSuccess, onError){
    if(test) zjdbc.retrieveByRange(0, 100, onSuccess, onError);
};

testRetrieveByRange(true, 0,10, 
    function(tasks, msg){
        for each (var task in tasks){
            print('name='+task.name+', completed='+task.completed);
        }
    },
    function(msg){
        print(msg);
    }
);

function testRetrieveByDone(test, completed, onSuccess, onError){
    if(test) zjdbc.retrieveByDone(completed, onSuccess, onError);
};

testRetrieveByDone(false, true, 
    function(tasks, msg){
        for each (var task in tasks){
            print('name='+task.name+', completed='+task.completed);
        }
    },
    function(msg){
        print(msg);
    }
);