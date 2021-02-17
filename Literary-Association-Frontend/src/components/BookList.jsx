import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { ListGroup, Table } from 'react-bootstrap'
import { searchBooks, getBooks } from '../reducers/bookReducer'
import BookListItem from './BookListItem'

const BookList = ({ myBooks }) => {
    const dispatch = useDispatch()

    useEffect(() => {
        if (myBooks) {
            dispatch(getBooks(myBooks))
        } else {
            dispatch(searchBooks({
                page: 1,
                searchParams: []
            }))
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
                <ListGroup>
                    {
                        books.map(b => <BookListItem key={b.id}/>)
                    }
                </ListGroup>
            </div>
        )
    }
}

export default BookList
