import {Grid} from '@material-ui/core';
import {SummaryUrlTable} from '../components/SummaryUrlTable';
import {ShortUrlForm} from "../components/ShortUrlForm";
import {ExpandShortUrlForm} from "../components/ExpandShortUrlForm";

export const Home = () => {
    return (
        <div className='App'>
            <div className='Form'>
                <br/>
                <Grid container spacing={1}>
                    <ShortUrlForm />
                    <ExpandShortUrlForm />
                </Grid>
            </div>
            <div>
                <SummaryUrlTable/>
            </div>
        </div>
    );
}
