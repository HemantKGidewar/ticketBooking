package ticket.booking.util;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class UserServiceUtil {

    public static String hashPassword(String originalPassword) {
        return BCrypt.hashpw(originalPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String originalPassword, String hashedPassword) {
        return BCrypt.checkpw(originalPassword, hashedPassword);
    }

    public static int readInt(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scan.hasNextInt()) {
                return scan.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.next(); // discard the invalid token
            }
        }
    }

}
