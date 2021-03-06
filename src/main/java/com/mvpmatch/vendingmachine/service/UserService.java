package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.domain.Role;
import com.mvpmatch.vendingmachine.domain.User;
import com.mvpmatch.vendingmachine.dto.UserDTO;
import com.mvpmatch.vendingmachine.mapper.UserMapper;
import com.mvpmatch.vendingmachine.repository.RoleRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        User user = new User(userDTO.getUsername().toLowerCase(), passwordEncoder.encode(userDTO.getPassword()));

        if (!userDTO.getRoles().isEmpty()) {
            Set<Role> authorities = userDTO
                    .getRoles()
                    .stream()
                    .map(roleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setRoles(authorities);
        }
        return userMapper.toDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getByUsername(String username){
        return  userRepository.
                findByUsername(username).map(userMapper::toDTO);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
