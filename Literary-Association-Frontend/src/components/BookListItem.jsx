import React, { useState } from 'react'
import { Button, Col, ListGroup, Row } from 'react-bootstrap'
import { Link, useHistory } from 'react-router-dom'
import ReactHtmlParser from 'react-html-parser'
import PaymentModal from './PaymentModal'
import bookService from '../services/bookService'
import { useDispatch } from 'react-redux'
import { setBook } from '../reducers/bookReducer'


const BookListItem = ({ id, title, openAccess, basicInfo, text, ISBN, publisher, year }) => {
    const dispatch = useDispatch()
    const history = useHistory()
    const [modalShown, setModalShown] = useState(false)

    const toggleModal = async () => {
        await dispatch(setBook(id))
        setModalShown(!modalShown)
    }

    const downloadOrBuy = () => {
        openAccess ? bookService.downloadBook(title, null) : toggleModal()
    }

    const seeBookDetails = () => history.push(`/dashboard/books/${id}`)


    if (ISBN) {
        return (
            <tr>
                <td>{ISBN}</td>
                <td>{title}</td>
                <td>{publisher}</td>
                <td>{year}</td>
                <td>
                    <Button onClick={seeBookDetails}>Details</Button>
                </td>
            </tr>
        )
    }
    return (
        <>
            <PaymentModal show={modalShown} toggleModal={toggleModal}/>
            <ListGroup.Item>
                <Row className="justify-content-between">
                    <Col>
                        <Link style={{ color: '#1a0dab' }} to={`/dashboard/books/${id}`}>{title}</Link>
                    </Col>
                    <Col>
                        <Button onClick={downloadOrBuy}>{openAccess ? 'Download' : 'Purchase'}</Button>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <p style={{ color: '#006621', fontSize: 13 }}>{basicInfo}</p>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        {ReactHtmlParser(text)}
                    </Col>
                </Row>
            </ListGroup.Item>
        </>
    )
}

export default BookListItem
