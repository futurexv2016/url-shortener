import {SET_URL, SET_SHORTENED_URL} from "../constants/constants";

export default function appReducer(state, action) {
    switch (action.type) {
        case SET_SHORTENED_URL:
            return {
                ...state,
                shortenedUrl: action.payload,
            };
        case SET_URL:
            return {
                urls: [...action.payload]
            }

        default:
            return state;
    }
};