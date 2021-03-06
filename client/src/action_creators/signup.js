import axios from 'axios';

import {URL} from '../constant';

export function SignUp(data){
    return  dispatch => {
        axios.post(URL+"user/signup", data)
          .then(function (response) {
            console.log(response.data)
            return dispatch({ type : "SIGNUP_RESULT", payload : response.data } )
          })
          .catch(function (error) {
            return dispatch({ type : "SIGNUP_ERROR", payload : error } )
          });
         
     }
}
