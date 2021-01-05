package pl.coderslab.charity.user;

public class UserAlreadyExistException extends Exception {
    private String message;
    public UserAlreadyExistException(String s) {
        this.message = s;
    }
}
