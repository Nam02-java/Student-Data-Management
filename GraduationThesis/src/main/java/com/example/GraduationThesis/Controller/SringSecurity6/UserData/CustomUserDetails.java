package com.example.GraduationThesis.Controller.SringSecurity6.UserData;

import com.example.GraduationThesis.Model.Enitity.Users.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {


    Users user;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        // return List.of(new SimpleGrantedAuthority(user.getListRoles().toString()));

        return this.authorities;
    }

    public static CustomUserDetails mapUserToUserDetail(Users user) {
        List<GrantedAuthority> listAuthorities = user.getListRoles()
                .stream().map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                .collect(Collectors.toList());

        System.out.println(user);

        // get roles form user entity
        return new CustomUserDetails(
                user,
                listAuthorities
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
