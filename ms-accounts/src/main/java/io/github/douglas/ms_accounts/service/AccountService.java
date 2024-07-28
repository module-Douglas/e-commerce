package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.*;

import java.util.UUID;

public interface AccountService {

    AccountDTO register(RegisterAccountDTO request);

    AccessTokenDTO login(LoginDTO loginRequest);

    AccountDTO getUserDetails(UUID id);

    void updatePassword(UpdatePasswordDTO request);
}
