import {Button, Grid, TextField} from "@material-ui/core";
import axios from "../utils/AxiosConfig";
import {useState} from "react";

export const ExpandShortUrlForm = () => {
    const [shortURL, setShortURL] = useState('');
    const [shortURLErrMsg, setShortURLErrMsg] = useState('');

    const onChangeShortURL = (e) => {
        setShortURL(e.target.value);
    }

    const expandShortURL = () => {
        if (!shortURL) {
            setShortURLErrMsg('Invalid shortened URL');
            return;
        }

        setShortURLErrMsg('');

        axios({
            'method': 'GET',
            'url': `/origin?shortenedUrl=${shortURL}`
        }).then((response) => {
            const redirectUrl = (response.data).includes('http://') || (response.data).includes('https://')
                ? response.data : 'http://' + response.data;
            window.open(redirectUrl, "_blank")
        }).catch((e) => {
            if (e.response) {
                console.log(e.response.data);
                setShortURLErrMsg(e.response.data.errorMessage);
            }
        });
    }

    return <Grid container item spacing={2}>
        <Grid item xs>
            <TextField
                id='shortUrl'
                size='small'
                fullWidth
                error={!!shortURLErrMsg}
                label='Enter short url'
                variant='outlined'
                helperText={shortURLErrMsg}
                onChange={onChangeShortURL}/>
        </Grid>

        <Grid item xs>
            <div className='Button'>
                <Button
                    color='primary'
                    size='large'
                    onClick={expandShortURL} variant='contained'>Go to original URL</Button>
            </div>
        </Grid>
    </Grid>
}
