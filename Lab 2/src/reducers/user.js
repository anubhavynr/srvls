const initialState = {
    firstName: 'Max',
    lastName: 'Mustermann',
    isAuthenticated: false,
    tenantId: null,
};

export const user = (state = initialState, action) => {
    switch (action.type) {
        default:
            return state;
    }    
};
