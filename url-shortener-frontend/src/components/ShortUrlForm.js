import {Button, Grid, TextField} from "@material-ui/core";
import {useContext, useState} from "react";
import axios from "../utils/AxiosConfig";
import {GlobalContext} from "../context/GlobalState";

export const ShortUrlForm = () => {
    const [urlErrMsg, setUrlErrMsg] = useState('');
    const [copyBtnText, setCopyBtnText] = useState('Copy');
    const [copyBtnColor, setCopyBtnColor] = useState('primary');
    const [url, setUrl] = useState('');
    const [shortenedUrl, setShortenedUrl] = useState('');
    const {setShortenedUrlContext} = useContext(GlobalContext);

    const createShortUrl = () => {
        const regex = new RegExp('[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)');

        if (!regex.test(url)) {
            setUrlErrMsg('Unable to shorten that link. It is not a valid url.');
            return;
        }

        setUrlErrMsg('');

        axios({
            'method': 'POST',
            'data': {'url': url}
        })
            .then((response) => {
                setShortenedUrl(response.data.shortenedUrl);
                setShortenedUrlContext(response.data.shortenedUrl);
                setUrl(response.data.shortenedUrl);
            })
            .catch((error) => {
                console.error(error)
            });
    }

    const copyShortURL = () => {
        navigator.clipboard.writeText(shortenedUrl);
        setCopyBtnText('Copied');
        setCopyBtnColor('default')
        setTimeout(() => {
            setCopyBtnText('Copy');
            setCopyBtnColor('primary');
        }, 1000);
    }

    const onChangeLongUrl= (e) => {
        if (e.target.value !== shortenedUrl && shortenedUrl) {
            setShortenedUrl('');
        }

        setUrl(e.target.value);
    }

    return <Grid container item spacing={2}>
        <Grid item xs>
            <TextField
                id='url'
                size='small'
                fullWidth
                error={!!urlErrMsg}
                label='Enter long url'
                variant='outlined'
                helperText={urlErrMsg}
                value={url}
                onChange={onChangeLongUrl}/>
        </Grid>

        <Grid item xs>
            <div className='Button'>
                {!shortenedUrl && <Button
                    color='primary'
                    size='large'
                    onClick={createShortUrl} variant='contained'>Shorten</Button>}

                {shortenedUrl && <Button
                    color={copyBtnColor}
                    size='large'
                    onClick={copyShortURL} variant='contained'>{copyBtnText}</Button>}
            </div>
        </Grid>
    </Grid>
}
