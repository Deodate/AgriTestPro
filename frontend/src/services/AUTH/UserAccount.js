import axios from "axios";

const USERS_REST_API_URL = "http://localhost:8088/api/auth/signup";

class UserAccount {

    createAccount(account){
        return axios.post(USERS_REST_API_URL, account);
    }

}

export default new UserAccount()