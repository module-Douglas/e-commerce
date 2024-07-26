package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.dto.LoginDTO;
import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.dto.UserDTO;

public interface UserService {

    UserDTO register(RegisterUserDTO request);

    AccessTokenDTO login(LoginDTO loginRequest);

    UserDTO getUserDetails(String id);
}
