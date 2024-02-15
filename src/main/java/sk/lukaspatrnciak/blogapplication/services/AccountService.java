package sk.lukaspatrnciak.blogapplication.services;

import sk.lukaspatrnciak.blogapplication.models.Account;
import sk.lukaspatrnciak.blogapplication.models.Authority;
import sk.lukaspatrnciak.blogapplication.repositories.AccountRepository;
import sk.lukaspatrnciak.blogapplication.repositories.AuthorityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;

    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository, AuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
    }

    public Account save(Account account) {

        if (account.getId() == null) {
            if (account.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
                account.setAuthorities(authorities);
            }
            account.setCreatedAt(LocalDateTime.now());
        }
        account.setUpdatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
}
