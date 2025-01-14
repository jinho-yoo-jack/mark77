package jack.labs.mark77.repository;

import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest()
@ActiveProfiles("test")
public class CartRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void insertTest() {
        Cart cart = new Cart();
        User user = User.builder()
            .id("jhy7342")
            .password("1234")
            .nickname("유진호 멘토")
            .role("ROLE_USER")
            .build();
        cart.setUser(user);
        user.setCart(cart);

        userRepository.save(user);

        User newUser = userRepository.findById("jhy7342")
            .orElseThrow(() -> new NoSuchElementException("Not Found"));

//        Cart newCart = cartRepository.findByUserId(newUser.getId())
//            .orElseThrow(() -> new NoSuchElementException("Not Found"));


        Assertions.assertEquals(newUser.getNickname(), user.getNickname());
//        Assertions.assertEquals(newCart.getUser().getId(), newUser.getId());
    }

    @Test
    public void selectTest() {

    }

    @Test
    public void updateTest() {

    }
}
