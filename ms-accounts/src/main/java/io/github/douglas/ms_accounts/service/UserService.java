package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO request);

}
