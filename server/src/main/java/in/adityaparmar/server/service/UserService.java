package in.adityaparmar.server.service;


import in.adityaparmar.server.entity.User;
import in.adityaparmar.server.entity.response.Response;
import in.adityaparmar.server.entity.response.SignInResponse;
import in.adityaparmar.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    Response response = new Response();
    SignInResponse signInResponse = new SignInResponse();

    public SignInResponse SignIn(User data){

        try{

            User user =  userRepository.findUsersByEmailAndPassword(data.getEmail(),data.getPassword());

            if(user==null){


                response.setStatus("error");
                response.setMsg("Email / Password may wrong.");
                signInResponse.setUsers(null);
                signInResponse.setResponse(response);
                return signInResponse;
            }
            else{

                response.setStatus("success");
                response.setMsg("Successfully Logged In with "+user.getEmail());
                signInResponse.setUsers(user);
                signInResponse.setResponse(response);
                return signInResponse;
            }


        }
        catch(Exception e){
            response.setStatus("error");
            response.setMsg("Something went wrong, Try Again.");
            signInResponse.setResponse(response);
            return signInResponse;
        }

    }

    public Response SignUp(User data) {

        try {

            User users = userRepository.findUserByEmail(data.getEmail());
            data.setAll(data.getFirstname() + " " + data.getLastname() + " (" + data.getEmail() + ")");
            if (users == null) {
                userRepository.save(data);
                response.setStatus("success");
                response.setMsg("Account Created Successfully, Proceed to LogIn.");
                return response;
            } else {

                response.setStatus("error");
                response.setMsg("Account is already exist with email id: " + users.getEmail());
                return response;
            }


        } catch (Exception e) {
            response.setStatus("error");
            response.setMsg("Something went wrong, Try Again.");
            return response;
        }
    }

}