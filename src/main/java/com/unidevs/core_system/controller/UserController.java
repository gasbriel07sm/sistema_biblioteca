package com.unidevs.core_system.controller;

import com.unidevs.core_system.entity.User;
import com.unidevs.core_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var usuarioId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/usuario/" + usuarioId)).build();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<User> getUserById(@PathVariable("usuarioId") String usuarioId) {
        var usuario = userService.getUserById(usuarioId);
        if (usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        var usuarios = userService.listUsers();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("usuarioId") String usuarioId,
                                               @RequestBody UpdateUserDto updateUserDto){
        userService.updateUserById(usuarioId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteById(@PathVariable("usuarioId") String usuarioId){
        userService.deleteById(usuarioId);
        return ResponseEntity.noContent().build();
    }
}