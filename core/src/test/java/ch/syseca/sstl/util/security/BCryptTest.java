package ch.syseca.sstl.util.security;


import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class BCryptTest {

    @Test
    public void test() {
        final String password = "password";
        final String expectedHash = "$2a$10$fzq6.9k9PKB9z3qwvHJHie.YYI9m44c3a0u4kyVsuP7EOhZstg0Uq";
        String hashed;

        // Hash a password for the first time
        hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        //assertEquals(expectedHash, hashed);
        //System.out.println(hashed);
        assertTrue(BCrypt.checkpw(password, hashed));
        assertTrue(BCrypt.checkpw(password, expectedHash));

        //hashed = BCrypt.hashpw("a super duper very long passsword that is easy to remember", BCrypt.gensalt(10));
        //System.out.println(hashed);

    }
}