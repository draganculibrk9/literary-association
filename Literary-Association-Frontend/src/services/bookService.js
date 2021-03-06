import axios from 'axios'

const BASE_URL = process.env.REACT_APP_BASE_URL

const searchBooks = async (query) => {
    const response = await axios.post(`${BASE_URL}/book/search`, query)
    return response.data
}

const getGenres = async () => {
    const response = await axios.get(`${BASE_URL}/book/genre`)
    return response.data
}

const getBooks = async () => {
    const response = await axios.get(`${BASE_URL}/book`)
    return response.data
}

const getBook = async (id) => {
    const response = await axios.get(`${BASE_URL}/book/${id}`)
    return response.data
}

const downloadBook = async (title, downloadUrl) => {
    const response = await axios.get(downloadUrl ? downloadUrl : `${BASE_URL}/book/${title}/download`, { responseType: 'blob' })
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.style = 'display: none'
    link.href = url
    link.setAttribute('download', `${title}.pdf`)
    document.body.appendChild(link)
    link.click()
    window.URL.revokeObjectURL(url)
}

const getRetailersForBook = async (id) => {
    const response = await axios.get(`${BASE_URL}/book/${id}/retailer`)
    return response.data
}

const getMyBooks = async () => {
    const response = await axios.get(`${BASE_URL}/book/my-books`)
    return response.data
}

const bookService = {
    searchBooks,
    getGenres,
    getBooks,
    getBook,
    getMyBooks,
    downloadBook,
    getRetailersForBook
}


export default bookService
