export const setPage = (page) => {
    return async dispatch => {
        dispatch({
            type: 'SET_PAGE',
            page
        })
    }
}

export const setTotalPages = (totalPages) => {
    return async dispatch => {
        dispatch({
            type: 'SET_TOTAL_PAGES',
            totalPages
        })
    }
}

const reducer = (state = { page: 0, totalPages: 0 }, action) => {
    switch (action.type) {
        case 'SET_PAGE': {
            return {
                ...state,
                page: action.page
            }
        }
        case 'SET_TOTAL_PAGES': {
            return {
                ...state,
                totalPages: action.totalPages
            }
        }
        default:
            return state
    }
}

export default reducer