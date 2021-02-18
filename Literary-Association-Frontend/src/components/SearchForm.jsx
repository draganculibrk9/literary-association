import React, { useEffect, useState } from 'react'
import { Button, Form } from 'react-bootstrap'
import BooleanOperationButton from './BooleanOperationButton'
import bookService from '../services/bookService'
import { useDispatch, useSelector } from 'react-redux'
import { searchBooks } from '../reducers/bookReducer'

const SearchForm = () => {
    const [genres, setGenres] = useState([])
    const [fields, setFields] = useState({
        title: '',
        writers: '',
        content: '',
        genre: ''
    })
    const [booleanOps, setBooleanOps] = useState({
        title: 'AND',
        writers: 'AND',
        content: 'AND',
        genre: 'AND'
    })

    const [firstLoad, setFirstLoad] = useState(true)

    const setBooleanOp = (name, value) => {
        const newBooleanOps = {
            ...booleanOps
        }
        newBooleanOps[name] = value
        setBooleanOps(newBooleanOps)
    }

    const page = useSelector(state => state.pagination.page)

    useEffect(() => {
        bookService.getGenres().then(result => setGenres(result))
    }, [])

    useEffect(() => {
        if (!firstLoad) {
            search()
        }
    }, [page])

    const dispatch = useDispatch()

    const search = () => {
        const searchParams = []
        for (const field in fields) {
            if (fields[field]) {
                searchParams.push({
                    key: field,
                    value: fields[field].replace('"', ''),
                    phraze: fields[field].includes('"'),
                    booleanOperator: booleanOps[field]
                })
            }
        }

        const query = {
            page,
            searchParams
        }

        dispatch(searchBooks(query))
        setFirstLoad(false)
    }

    const onSubmit = (event) => {
        event.preventDefault()
        search()
    }

    return (
        <Form inline className="justify-content-between" onSubmit={onSubmit}>
            <BooleanOperationButton name="title" setValue={setBooleanOp} value={booleanOps['title']}/>
            <Form.Control
                as="input"
                pattern='^[\p{L}]+([\s]{1,1}[\p{L}]+)*$|^"[\p{L}]+([\s]{1,1}[\p{L}]+)*"$'
                placeholder="title"
                value={fields['title']}
                onChange={({ target }) => setFields({ ...fields, title: target.value })}
            />
            <BooleanOperationButton name="writers" setValue={setBooleanOp} value={booleanOps['writers']}/>
            <Form.Control
                as="input"
                pattern='^[\p{L}]+([\s]{1,1}[\p{L}]+)*$|^"[\p{L}]+([\s]{1,1}[\p{L}]+)*"$'
                placeholder="writers"
                value={fields['writers']}
                onChange={({ target }) => setFields({ ...fields, writers: target.value })}
            />
            <BooleanOperationButton name="content" setValue={setBooleanOp} value={booleanOps['content']}/>
            <Form.Control
                as="input"
                pattern='^[\p{L}]+([\s]{1,1}[\p{L}]+)*$|^"[\p{L}]+([\s]{1,1}[\p{L}]+)*"$'
                placeholder="content"
                value={fields['content']}
                onChange={({ target }) => setFields({ ...fields, content: target.value })}
            />
            <BooleanOperationButton name="genre" setValue={setBooleanOp} value={booleanOps['genre']}/>
            <Form.Control
                as="select"
                value={fields['genre']}
                onChange={({ target }) => setFields({ ...fields, genre: target.value })}
            >
                <option value="">-</option>
                {
                    genres.map(g => (
                        <option value={g} key={g}>{g}</option>
                    ))
                }
            </Form.Control>
            <Button type="submit" variant="secondary">Search</Button>
        </Form>
    )
}

export default SearchForm