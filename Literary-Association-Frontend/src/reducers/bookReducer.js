import bookService from '../services/bookService'

export const getBooks = () => {
    return async dispatch => {
        const books = await bookService.getBooks()

        dispatch({
            type: 'GET_BOOKS',
            books
        })
    }
}

export const setBook = (id) => {
    return async dispatch => {
        const book = await bookService.getBook(id)

        dispatch({
            type: 'SET_BOOK',
            book
        })
    }
}

const reducer = (state = { list: [], shown: null }, action) => {
    switch (action.type) {
    case 'GET_BOOKS': {
        return {
            ...state,
            list: action.books
        }
    }
    case 'SET_BOOK': {
        return {
            ...state,
            shown: action.book
        }
    }
    default:
        return state
    }
}

export default reducer