const initialState = [
    {name: 'Walk the dog', completed: true}, 
    {name: 'Run a mile', completed: false}, 
    {name: 'Hit the gym', completed: true}, 
    {name: 'Swim an hour', completed: false}
];

const rootReducer = (state = initialState, action) => {

    switch(action.type){
        case "CREATE_TASK": {
            return [...state, action.payload]
        }
        case "RETRIEVE_TASKS": {
            return action.payload;
        }
        case "UPDATE_DONE": {
            return state.map(e=> {
                if(e.name == action.payload.name){
                    return action.payload;
                }
                return e;
            });
        }
        case "DELETE_TASK": {
            return state.filter(e=> e.name !== action.payload);
        }
        default: {
            return state;
        }
    }
}

export default rootReducer;

