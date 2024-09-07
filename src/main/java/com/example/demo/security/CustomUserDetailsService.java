package com.example.demo.security;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//    private final UserRepository userRepository;
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    @Autowired
    private RepoService repoService;



    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
       // User user = userRepository.findUserByEmail(email);

        com.example.demo.models.User user1 = new com.example.demo.models.User();
        try {
            repoService.getResult("select usser, password from TeamDB " // ,
                    + " where usser = " + "'" + user + "'", new ResultSetCallback() {
                @Override
                public void result(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        user1.setUsser(rs.getString(1));
                        user1.setPassword(rs.getString(2));
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user1.getUsser())
                        .password(user1.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
}