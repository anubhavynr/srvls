const initialState = {
    firstName: 'Max',
    lastName: 'Mustermann',
    isAuthenticated: true,
    tenantId: null,
};

export const user = (state = initialState, action) => {
    switch (action.type) {
        default:
            return state;
    }    
};
