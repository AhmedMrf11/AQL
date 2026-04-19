package org.example.partie01.exercice1;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur non trouve");
        }
        return user;
    }

    public void createUser(User user) {
        if (userRepository.findById(user.getId()) != null) {
            throw new IllegalArgumentException("Utilisateur deja existant");
        }
        userRepository.save(user);
    }
}
