package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.dto.UserDTO;

public interface UserService {

    UserDTO register(RegisterUserDTO request);

    UserDTO getUserDetails(String id);
}
