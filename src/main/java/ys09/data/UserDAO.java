package ys09.data;

import ys09.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    List<User> getUsers(Limits limits);

    User getUser(String UserName, String password);

    void assignToken(String token, long id);

    User getUserToken(String token);

    int addUser(User usr);

    Optional<User> getById(long id);

    int SetUserStatus(long id, int status);

}
