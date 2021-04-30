import React, {createContext, useReducer} from 'react';
import {SET_URL, SET_SHORTENED_URL} from '../constants/constants';

import appReducer from './AppReducer';

const initialState = {
    urls: [],
    shortenedUrl: ''
};

export const GlobalContext = createContext(initialState);

export const GlobalProvider = ({ children }) => {
    const [state, dispatch] = useReducer(appReducer, initialState);

    const setUrls = (urls) => {
        dispatch({
            type: SET_URL,
            payload: [...urls]
        });
    }

    const setShortenedUrlContext = (urls) => {
        dispatch({
            type: SET_SHORTENED_URL,
            payload: [...urls]
        });
    }

    return (
        <GlobalContext.Provider
            value={{
                urls: state.urls,
                shortenedUrl: state.shortenedUrl,
                setUrls,
                setShortenedUrlContext
            }}
        >
            {children}
        </GlobalContext.Provider>
    );
};
