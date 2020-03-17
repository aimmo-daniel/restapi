package com.study.restapi.common;

import com.study.restapi.accounts.Account;
import com.study.restapi.accounts.AccountRepository;
import com.study.restapi.accounts.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {



    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName() {
        // Given
        String username = "sangjin@gmail.com";
        String password = "sangjin";
        Set<AccountRole> roles = new HashSet<>(Arrays.asList(AccountRole.ADMIN, AccountRole.USER));

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(roles)
                .build();

        // When
        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    public void findByUsernameFail() {
        // Expected
        String username = "random@email.com";

        expectedException
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(username);
        }
    }

}