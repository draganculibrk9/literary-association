import React from 'react'
import ReaderRegistration from './components/ReaderRegistration'
import WriterRegistration from './components/WriterRegistration'
import { Switch, Route, Redirect } from 'react-router-dom'
import Login from './components/Login'
import Dashboard from './components/Dashboard'
import Logout from './components/Logout'
import axios from 'axios'
import store from './store'
import SocketComponent from './components/SocketComponent'
import Toaster from './components/Toaster'

axios.interceptors.request.use(
    request => {
        const state = store.getState()
        const token = state.user.token

        if (token) {
            request.headers['Authorization'] = `Bearer ${token}`
        }
        return request
    },
    error => Promise.reject(error)
)

const App = () => {
    return (
        <div className='container'>
            <SocketComponent/>
            <Toaster/>
            <Switch>
                <Route path='/login'>
                    <Login/>
                </Route>
                <Route path='/logout'>
                    <Logout/>
                </Route>
                <Route path='/reader-registration'>
                    <ReaderRegistration/>
                </Route>
                <Route path='/writer-registration'>
                    <WriterRegistration/>
                </Route>
                <Route path='/dashboard'>
                    <Dashboard/>
                </Route>
                <Route exact path='/'>
                    <Redirect to='/login'/>
                </Route>
            </Switch>
        </div>
    )
}

export default App
