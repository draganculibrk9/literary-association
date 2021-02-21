import React from 'react'
import { Button, Col, ListGroup, Row } from 'react-bootstrap'
import { Link, useHistory } from 'react-router-dom'
import ReactHtmlParser from 'react-html-parser'


const BookListItem = ({ id, title, openAccess, basicInfo, text }) => {
    const history = useHistory()

    const seeBookDetails = () => history.push(`/dashboard/books/${id}`)

    return (
        <ListGroup.Item>
            <Row className="justify-content-between">
                <Col>
                    <Link style={{ color: '#1a0dab' }} to={`/dashboard/books/${id}`}>{title}</Link>
                </Col>
                <Col>
                    <Button onClick={seeBookDetails}>{openAccess ? 'Download' : 'Purchase'}</Button>
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
    )
}

export default BookListItem
