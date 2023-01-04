package com.beekeeperpro.data;

import com.beekeeperpro.data.model.User;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final DataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private static User user = null;
    private boolean isLoggingIn = false;

    // private constructor : singleton access
    private LoginRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public static User getLoggedInUser() {
        if (instance.isLoggingIn) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (user == null) throw new RuntimeException("User not logged");
        return user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(User user) {
        LoginRepository.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<User> login(String username, String password) {
        // handle login
        isLoggingIn = true;
        Result<User> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<User>) result).getData());
        }
        isLoggingIn = false;
        return result;
    }
}