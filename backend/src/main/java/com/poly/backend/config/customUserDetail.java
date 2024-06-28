//package com.poly.backend.config;
//
//import com.poly.backend.entity.Instructor;
//import com.poly.backend.repository.InstructorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class customUserDetail implements UserDetailsService {
//    @Autowired
//    InstructorRepository accountDAO;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Instructor accounts = accountDAO.findTeacherByEmailEdu(username);
//        if (accounts == null) {
//            throw new UsernameNotFoundException("user không tồn tại!!!");
//        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_"+(accounts.isAdmin()?"ADMIN":"USER")));
//        return new User(username, accounts.getPassword(),authorities);
//    }
//}
