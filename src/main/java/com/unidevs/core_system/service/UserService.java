package com.unidevs.core_system.service;

import com.unidevs.core_system.controller.dto.CreateUserDto;
import com.unidevs.core_system.controller.dto.UpdateUserDto;
import com.unidevs.core_system.entity.User;
import com.unidevs.core_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(
                createUserDto.usuarioRA(),
                createUserDto.usuarioNome(),
                createUserDto.usuarioSenha()
        );
        var saved = userRepository.save(entity);
        return saved.getUsuarioId();
    }

    public Optional<User> getUserById(String usuarioId){
        return userRepository.findById(UUID.fromString(usuarioId));
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(String usuarioId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(usuarioId);
        var usuarioExists = userRepository.findById(id);

        if (usuarioExists.isPresent()){
            var usuario = usuarioExists.get();
            if (updateUserDto.usuarioNome() != null){
                usuario.setUsuarioNome(updateUserDto.usuarioNome());
            }
            if (updateUserDto.usuarioSenha() != null){
                usuario.setUsuarioSenha(updateUserDto.usuarioSenha());
            }
            userRepository.save(usuario);
        }
    }

    public void deleteById(String usuarioId){
        var id = UUID.fromString(usuarioId);
        var usuarioExists = userRepository.existsById(id);
        if (usuarioExists){
            userRepository.deleteById(id);
        }
    }
}