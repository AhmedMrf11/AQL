package org.example.partie01.exercice1;

public interface UserRepository {
    User findById(long id);
    void save(User user);
}
