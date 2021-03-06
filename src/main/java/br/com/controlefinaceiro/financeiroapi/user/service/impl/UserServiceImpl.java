package br.com.controlefinaceiro.financeiroapi.user.service.impl;

import br.com.controlefinaceiro.financeiroapi.user.entity.User;
import br.com.controlefinaceiro.financeiroapi.user.repository.UserRepository;
import br.com.controlefinaceiro.financeiroapi.user.service.UserService;
import br.com.controlefinaceiro.financeiroapi.utils.exception.BusinessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        if(repository.existsByEmail(user.getEmail())){
            throw new BusinessException("Email ja cadastrado.");
        }
        return repository.save(user);
    }

    @Override
    public Optional getById(long idUser) {
        return repository.findById(idUser);
    }

    @Override
    public void delete(User user) {
        prepareUserToUpdateOrDelete(user);
        repository.delete(user);
    }

    @Override
    public User update(User user) {
        prepareUserToUpdateOrDelete(user);
        return repository.save(user);
    }

    private void prepareUserToUpdateOrDelete(User user) {
        if(user == null || user.getId() == null){
            throw new IllegalArgumentException("Obrigatorio usuario.");
        }
        if(!repository.existsById(user.getId())){
            throw new BusinessException("Usuario inexistente.");
        }
    }

    @Override
    public Page find(User user, Pageable pageable) {
        Example<User> example = Example.of(user,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return repository.findAll(example, pageable);
    }

    @Override
    public User find(Long idUser) {
        Optional<User> userOptional = repository.findById(idUser);
        return userOptional.isPresent() ? userOptional.get() : null;
    }

}
