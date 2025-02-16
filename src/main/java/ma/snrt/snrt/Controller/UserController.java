package com.unity.erm.unity_erm.Controller;

import com.unity.erm.unity_erm.Entities.AuthRequest;
import com.unity.erm.unity_erm.Entities.User;
import com.unity.erm.unity_erm.Services.JwtService;
import com.unity.erm.unity_erm.Services.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Tag(name = "User",
        description = "This API provides the capability to search User from a User Repository")
public class UserController {
    private final UsersService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Endpoint to test if the API is working
    @GetMapping("/test")
    @Operation(summary = "Test the API", description = "This endpoint is used to test if the API is working")
    public String welcome() {
        return "API is working";
    }


    // Endpoint to add a new user, accessible only to users with ADMIN_ROLES authority
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        try {
            // Exclude ID from User type by creating a new User instance without ID
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            // newUser.setRoles(user.getRoles());
            // Add the new user using the userService
            User savedUser = userService.addUser(newUser);
            // Return a response indicating the user was added successfully

            return new ResponseEntity<>("User added successfully with ID: " + savedUser.getId(), HttpStatus.CREATED);

        } catch (Exception e) {
            // Return a response indicating an error occurred
            return new ResponseEntity<>("Error adding user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to login a user and generate JWT tokens
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
        // Authenticate the user using the authenticationManager
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        // If authentication is successful
        if (authenticate.isAuthenticated()) {
            // Generate an access token and a refresh token
            String token = jwtService.generateToken(authRequest.getEmail());
            String refreshToken = jwtService.generateRefreshToken(authRequest.getEmail());

            // Create a cookie to store the refresh token
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true); // Set the cookie as HttpOnly
            refreshTokenCookie.setPath("/user/refresh-token"); // Set the path for the cookie
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // Set the cookie expiration time to 7 days
            response.addCookie(refreshTokenCookie); // Add the cookie to the response

            // Return the access token in the response
            return ResponseEntity.ok(token);
        } else {
            // If authentication fails, throw an exception
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    // Endpoint to refresh the access token using the refresh token
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        // Extract the username from the refresh token
        String username = jwtService.extractUserName(refreshToken);
        // If the username is valid and the token is valid
        if (username != null && jwtService.validateToken(refreshToken, userService.loadUserByUsername(username))) {
            // Generate a new access token
            String newToken = jwtService.generateToken(username);
            // Return the new access token in the response
            return ResponseEntity.ok(newToken);
        } else {
            // If the refresh token is invalid, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    // Endpoint to get all users, accessible only to users with ADMIN_ROLES authority
    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public ResponseEntity<List<User>> getAllUsers() {
        // Get the list of all users using the userService
        List<User> users = userService.getUsers();
        // Return the list of users in the response
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to get a user by ID, accessible only to users with USER_ROLES authority
    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Get the user by ID using the userService
        User user = userService.getUser(id);
        // Return the user in the response
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Endpoint to get a user by email, accessible only to users with ADMIN_ROLES authority
    @GetMapping("/getUsersByEmail/{email}")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public ResponseEntity<User> getUsersByEmail(@PathVariable String email) {
        // Get the user by email using the userService
        User users = userService.getUserByEmail(email);
        // Return the user in the response
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to delete a user by ID, accessible only to users with ADMIN_ROLES authority
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Delete the user by ID using the userService
        userService.deleteUser(id);
        // Return a response indicating the user was deleted successfully
        return ResponseEntity.ok("User deleted successfully!");
    }

    // Endpoint to update a user by ID, accessible to users with ADMIN_ROLES or USER_ROLES authority
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLES') OR hasAuthority('USER_ROLES')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        // Update the user by ID using the userService
        User updatedUser = userService.updateUser(id, user);
        // Return the updated user in the response
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}