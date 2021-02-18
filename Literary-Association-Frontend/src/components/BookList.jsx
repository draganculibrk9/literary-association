import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { ListGroup, Table } from 'react-bootstrap'
import { clearSearch, getBooks } from '../reducers/bookReducer'
import BookListItem from './BookListItem'
import SearchForm from './SearchForm'
import Pagination from './Pagination'

const BookList = ({ myBooks }) => {
    const dispatch = useDispatch()

    useEffect(() => {
        if (myBooks) {
            dispatch(getBooks(myBooks))
        } else {
            dispatch(clearSearch())
        }
    }, [])

    const books = useSelector(state => state.books.list)

    if (myBooks) {
        return (
            <div>
                <h2>Books</h2>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ISBN</th>
                        <th>Title</th>
                        <th>Publisher</th>
                        <th>Year</th>
                        <th/>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        books.map(book =>
                            <BookListItem key={book.id} id={book.id} ISBN={book.isbn} publisher={book.publisher}
                                          title={book.title}
                                          year={book.year}/>)
                    }
                    </tbody>
                </Table>
            </div>
        )
    } else {
        return (
            <div>
                <h2>Books</h2>
                <SearchForm/>
                <br/>
                <ListGroup>
                    {
                        books.map(b => <BookListItem key={b.id} {...b}/>)
                    }
                </ListGroup>
                <br/>
                <Pagination/>
            </div>
        )
    }
}

export default BookList
