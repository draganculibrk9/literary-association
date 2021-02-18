import bookService from '../services/bookService'

export const getBooks = (myBooks) => {
    return async dispatch => {
        const books = !myBooks ? await bookService.getBooks() : await bookService.getMyBooks()

        dispatch({
            type: 'GET_BOOKS',
            books
        })

        dispatch({
            type: 'SET_MY_BOOKS',
            myBooks
        })
    }
}

export const searchBooks = (query) => {
    return async dispatch => {
        const books = await bookService.searchBooks(query)

        dispatch({
            type: 'GET_BOOKS',
            books: books ? books['content'] : []
        })

        dispatch({
            type: 'SET_PAGE',
            page: query['page']
        })

        dispatch({
            type: 'SET_TOTAL_PAGES',
            totalPages: books['totalPages']
        })
    }
}

export const clearSearch = () => {
    return async dispatch => {
        dispatch({
            type: 'GET_BOOKS',
            books: []
        })

        dispatch({
            type: 'SET_PAGE',
            page: 0
        })

        dispatch({
            type: 'SET_TOTAL_PAGES',
            totalPages: 0
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

const reducer = (state = { list: [], shown: null, myBooks: false }, action) => {
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
        case 'SET_MY_BOOKS': {
            return {
                ...state,
                myBooks: action.myBooks
            }
        }
        default:
            return state
    }
}

export default reducer
