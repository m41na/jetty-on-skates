function ping(){
    print("jjs --language=es6 -ot -scripting -J-Djava.class.path=../target/jetty-router-0.1.0-shaded.jar zjdbc.js");
    print("java -jar ../target/jetty-router-0.1.0-shaded.jar zjdbc.js");
    return 'zjdbc ping invoked from js';
}

let dao = {};

(function(dao, load){

    var DataSource = Packages.org.apache.commons.dbcp2.BasicDataSource;
    var Task = Packages.com.practicaldime.jesty.todos.Task;

    var DB = function(params){
         this.config = {
            "jdbc.driverClass": params && params['driverClass'] || "org.h2.Driver",
            "jdbc.url":         params && params['url'] || "jdbc:h2:./data/todos.js_db;DB_CLOSE_DELAY=-1",
            "jdbc.username":    params && params['username'] || "sa",
            "jdbc.password":    params && params['password'] || "sa"
        };
        this.ds = undefined;
    };

    DB.prototype.init = function(init){
        var dataSource = new DataSource();
        dataSource.setDriverClassName(this.config["jdbc.driverClass"]);
        dataSource.setUrl(this.config["jdbc.url"]);
        dataSource.setUsername(this.config["jdbc.username"]);
        dataSource.setPassword(this.config["jdbc.password"]);
        this.ds = dataSource;
    };

    DB.prototype.createTable = function(query, onSuccess, onError){
        var con, stmt;
        try{
            con = this.ds.getConnection(); 
            stmt = con.createStatement();
            var result = stmt.execute(query);
            if (result) {
                onSuccess("createTable was successful");
            }
        }
        catch(error){
            onError(error);
        }
        finally{
            if(stmt) stmt.close();
            if(con) con.close();
        }
    };

    DB.prototype.createTasks = function(tasks, onSuccess, onError) {
        var query = "merge into tbl_todos (task, completed) key(task) values (?, false)";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            for(var i= 0; i < tasks.length; i++){
                pst.setString(1, tasks[i]);
                pst.addBatch();
            }        
            var result = pst.executeBatch();
            onSuccess(result, "batch insert was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.clearAllTasks = function (onSuccess, onError) {
        var query = "TRUNCATE table tbl_todos";
        var con, stmt;
        try {
            con = this.ds.getConnection();
            stmt = con.createStatement();
            var result = stmt.executeUpdate(query);
            onSuccess(result, "Table data truncated");
        } catch (error) {
            onError(error);
        } finally {
            if (stmt) stmt.close();
            if (con) con.close();
        }
    };

    DB.prototype.createTask = function(task, onSuccess, onError) {
        var query = "INSERT INTO tbl_todos (task) values (?)";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setString(1, task);
            var result = pst.executeUpdate();
            onSuccess(result, "createTask was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.updateDone = function(task, done, onSuccess, onError) {
        var query = "UPDATE tbl_todos set completed=? where task = ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setBoolean(1, done);
            pst.setString(2, task);
            var result = pst.executeUpdate();
            onSuccess(result, "updated complete status");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.updateName = function(task, newName, onSuccess, onError) {
        var query = "UPDATE tbl_todos set task=? where task = ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setString(1, newName);
            pst.setString(2, task);
            var result = pst.executeUpdate();
            onSuccess(result, "updateName was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.deleteTask = function(task, onSuccess, onError) {
        var query = "DELETE from tbl_todos where task = ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setString(1, task);
            var result = pst.executeUpdate();
            onSuccess(result, "deleteTask was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.retrieveTask = function(name, onSuccess, onError) {
        var query = "SELECT * from tbl_todos where task = ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setString(1, name);
            var rs = pst.executeQuery();
            if (rs.next()) {
                var task = new Task();
                task.completed = rs.getBoolean("completed");
                task.name = rs.getString("task");
                task.created = rs.getDate("date_created");
                onSuccess(task, "retrieveTask was successful");
            } else {
                onSuccess({}, "no task found");
            }
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.retrieveByRange = function(start, size, onSuccess, onError) {
        var query = "SELECT * from tbl_todos limit ? offset ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setInt(1, size);
            pst.setInt(2, start);
            var rs = pst.executeQuery();
            var result = [];
            while (rs.next()) {
                var task = new Task();
                task.completed = rs.getBoolean("completed");
                task.name = rs.getString("task");
                task.created = rs.getDate("date_created");
                result.push(task);
            }
            onSuccess(result, "retrieveByRange was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    DB.prototype.retrieveByDone = function(completed, onSuccess, onError) {
        var query = "SELECT * from tbl_todos where completed = ?";
        var con, pst;
        try {
            con = this.ds.getConnection(); 
            pst = con.prepareStatement(query);
            pst.setBoolean(1, completed);
            var rs = pst.executeQuery();
            var result = [];
            while (rs.next()) {
                var task = new Task();
                task.completed = rs.getBoolean("completed");
                task.name = rs.getString("task");
                task.created = rs.getDate("date_created");
                result.push(task);
            }
            onSuccess(result, "retrieveTasks was successful");
        } catch (error) {
            onError(error);
        }finally{
            if(pst) pst.close();
            if(con) con.close();
        }
    };

    //export DB through 'dao' 
    dao.DB = DB;

    //load data if necessary
    if(load){
        let onError = msg => print(msg);
        let onSuccess = msg => print(msg);
        
        //init database and insert data
        var db = new dao.DB();
        db.init();        
        db.createTable([
            "CREATE TABLE IF NOT EXISTS tbl_todos (",
            "  task varchar(25) UNIQUE NOT NULL,",
            "  completed boolean DEFAULT false,",
            "  date_created datetime default current_timestamp,",
            "  PRIMARY KEY (task)",
            ")"
        ].join(""), onSuccess, onError);

        var data = ['buy milk', 'work out', 'watch game', 'hit gym', 'go to meeting'];
        db.createTasks(data, (res, msg) => print("res=" + res + ", msg=" + msg), onError);

        print("data loaded".concat(" @ ").concat(new Date().toString()));
    }

})(dao, true);

