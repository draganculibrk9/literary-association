import React from 'react'
import { Button, ListGroup } from 'react-bootstrap'
import { useHistory } from 'react-router-dom'

const BookListItem = ({ id, title, openAccess, basicInfo, text }) => {
    const history = useHistory()

    const seeBookDetails = () => history.push(`/dashboard/books/${id}`)

    return (
        <ListGroup.Item>
            <span><a>{title}</a>&nbsp;<Button
                onClick={seeBookDetails}>{openAccess ? 'Download' : 'Purchase'}</Button></span>
            <span>{basicInfo}</span>
            <div>{text}</div>
        </ListGroup.Item>
    )
}

export default BookListItem
