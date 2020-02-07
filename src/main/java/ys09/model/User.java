package ys09.model;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private final long id;
    private final String email;
    private final String name;
    private final String surname;
    private final String password;
    private final long role;

    public User(long id, String email, String name, String surname, String password, long role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = generate_hashed_password(password, name);
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getEmail() { return email; }

    public String getName() {
        return name;
    }

    public String getSurname() { return surname; }

    public String getPassword() { return password; }

    public long getRole() { return role; }


    public static String generate_hashed_password(String pre_hashed_password, String string_for_salt) {

        String hash;
        byte[] salt = string_for_salt.getBytes();
        hash = get_SHA_512_SecurePassword(pre_hashed_password, salt);
        return hash;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
