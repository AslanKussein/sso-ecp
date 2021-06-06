package kz.crtr.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class a {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.print(encoder.encode("ASTANA"));
    }
}
