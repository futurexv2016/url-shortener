import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: `${process.env.REACT_APP_BACKEND_URL}/api/v1/shortening`,
});

export default axiosInstance;
