import {
    Button,
    Grid,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow, TextField, withStyles
} from "@material-ui/core";
import {useContext, useEffect, useState} from "react";
import {GlobalContext} from "../context/GlobalState";
import axios from "../utils/AxiosConfig";


export const SummaryUrlTable = () => {
    const [page, setPage] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [urlSearch, setUrlSearch] = useState('');
    const {urls, setUrls, shortenedUrl} = useContext(GlobalContext);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const fetchURLList = () => {
        const queryString = !urlSearch ? `page=${page}&size=${rowsPerPage}&sort=createdAt,desc`
            : `shortenedUrl=${urlSearch}&page=${page}&size=${rowsPerPage}&sort=createdAt,desc`;

        axios({
            'method': 'GET',
            'url': `?${queryString}`
        })
            .then((response) => {
                setUrls(response.data.content);
                setTotalElements(response.data.totalElements);
            })
            .catch((error) => {
                console.error(error)
            });
    };

    const searchShortURL = () => {
        fetchURLList();
    }

    const onChangeUrlSearch = (e) => {
        setUrlSearch(e.target.value);
    }

    useEffect(() => {
        fetchURLList()
        // eslint-disable-next-line
    }, [page, rowsPerPage, shortenedUrl]);

    const StyledTableCell = withStyles((theme) => ({
        head: {
            backgroundColor: theme.palette.common.black,
            color: theme.palette.common.white,
        }
    }))(TableCell);

    return (
        <div id='url-table'>
            <div className='Form'>
                <Grid container item spacing={2}>
                    <Grid item xs>
                        <TextField
                            id='searchUrl'
                            size='small'
                            fullWidth
                            label='Enter short url (excluding domain)'
                            variant='outlined'
                            onChange={onChangeUrlSearch}/>
                    </Grid>
                    <Grid item xs>
                        <div className='Button'>
                            <Button
                                color='primary'
                                size='large'
                                onClick={searchShortURL} variant='contained'>Search shortened URL</Button>
                        </div>
                    </Grid>
                </Grid>
            </div>
            <br/><br/>
            <hr/>
            <h2>URL Summary Table</h2>
            <TableContainer component={Paper}>
                <Table aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <StyledTableCell>Shortened Url</StyledTableCell>
                            <StyledTableCell>Original Url</StyledTableCell>
                            <StyledTableCell>Created Time</StyledTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {urls.map((row) => (row &&
                            <TableRow key={row.id}>
                                <TableCell component="th" scope="row">
                                    {row.shortenedUrl}
                                </TableCell>
                                <TableCell>{row.url}</TableCell>
                                <TableCell>{row.createdAt}</TableCell>
                            </TableRow>

                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[5, 10, 25]}
                component="div"
                count={totalElements}
                rowsPerPage={rowsPerPage}
                page={page}
                onChangePage={handleChangePage}
                onChangeRowsPerPage={handleChangeRowsPerPage}
            />
        </div>
    );
}
