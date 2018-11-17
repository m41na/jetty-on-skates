import React from 'react';
import './favicon.ico';

import TodoList from './containers/todolist';

class App extends React.Component {

    render() {
        return ( 
            <TodoList />
        );
    }
}

export default App;