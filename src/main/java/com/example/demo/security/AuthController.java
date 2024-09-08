package com.example.demo.security;


import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RepoService repoService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody AuthRequest loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                            loginReq.getPassword()));

            com.example.demo.models.User user1 = new com.example.demo.models.User();
            user1.setUsser(authentication.getName());
            user1.setPassword(loginReq.getPassword());
            repoService.getResult("select usser, password, Service_Order, Working_Book,"
                    + " Invoice, Reports,"
                    + " New_Ext , Hidden_Menu, Acquittance from TeamDB " // ,
                    + " where usser = " + "'" + authentication.getName() + "'", new ResultSetCallback() {
                @Override
                public void result(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                       // user1.setUsser(rs.getString(1));
                       // user1.setPassword(rs.getString(2));
                        user1.setService_Order(rs.getString(3));
                        user1.setWorking_Book(rs.getString(4));
                        user1.setInvoice(rs.getString(5));
                        user1.setReports(rs.getString(6));
                        user1.setNew_Ext(rs.getString(7));
                        user1.setHidden_Menu(rs.getString(8));
                        user1.setAcquittance(rs.getString(9));
                    }
                }
            });
            String token = jwtUtil.createToken(user1);
            LoginRes loginRes = new LoginRes(user1,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username");// e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}