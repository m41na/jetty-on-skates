import { connect } from "react-redux";
import axios from 'axios';
import qs from 'qs';

import {retrieveTasks, createTask, updateDone, deleteTask} from '../actions';

import TodoList from '../components/TodoList';

const mapStateToProps = state => {
  return { todos: state };
};

const mapDispatchToProps = dispatch => ({
    retrieveTasks: ()=> {
        const url = 'http://localhost:8080/app/todos';
        const headers = {'Accept': 'application/json'};
        axios.get(url, null, headers).then(response => {
            let todos = response.data;
            dispatch(retrieveTasks(todos.tasks));
        }).catch(error => {
            console.log('retrieveTasks error ', error);
        });
    },

    createTask: (value) => {
        if (value) {
            const url = 'http://localhost:8080/app/todos';
            const headers = {'Content-Type': 'application/x-www-form-urlencoded'};
            const data = qs.stringify({task: value});
            axios.post(url, data, {headers: headers}).then(response => {
                dispatch(createTask(response.data))
            }).catch(error => {
                console.log('createTask error ', error);
            });
        }
    },

    updateDone: (name, complete) => {
        const url = 'http://localhost:8080/app/todos/done';
        const headers = {'Content-Type': 'application/x-www-form-urlencoded'};
        const data = qs.stringify({task: name, complete: complete});
        axios.put(url, data, {headers: headers}).then(response => {
            let task = response.data;
            dispatch(updateDone(task));
        }).catch(error => {
            console.log('updateDone error ', error);
        });
    },

    deleteTask: (name) => {
        const url = 'http://localhost:8080/app/todos?name=' + name;
        const headers = {'Accept': 'application/json'};
        axios.delete(url, null, headers).then(response => {
            let task = response.data;
            dispatch(deleteTask(task));
        }).catch(error => {
            console.log('deleteTask error ', error);
        }); 
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(TodoList);