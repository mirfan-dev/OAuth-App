package com.auth.service.impl;

import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.exception.ResourceNotFoundException;
import com.auth.helper.UserHelper;
import com.auth.repository.UserRepository;
import com.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())){
                throw new ResourceNotFoundException("User already exist with given email");
        }

        User user =modelMapper.map(userDto,User.class);

        User savedUser=userRepository.save(user);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found with this email : "+email));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        UUID uuid= UserHelper.parseUUID(userId);
        User existingUser=userRepository.findById(uuid)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with given id: "+userId));
        if (userDto.getName()!=null) existingUser.setName(userDto.getName());
        if (userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());
        if (userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        if (userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());
        existingUser.setEnable(userDto.isEnable());

        User savedUser=userRepository.save(existingUser);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {

        UUID uuid= UserHelper.parseUUID(userId);
        User user=userRepository.findById(uuid)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with given id: "+userId));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uuid= UserHelper.parseUUID(userId);
        User user=userRepository.findById(uuid)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with given id: "+userId));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .toList();
    }
}
