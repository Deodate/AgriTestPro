import axios from 'axios'

const USERS_REST_API_URL = '';

class UserService {
    getusers(){
        return axios.get(USERS_REST_API_URL);
    }
}

export default new UserService();