package jack.labs.mark77.service;

import jack.labs.mark77.dto.WishItem;
import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.CartDetail;
import jack.labs.mark77.entity.Product;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.repository.CartDetailRepository;
import jack.labs.mark77.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    private final UserService userService;
    private final ProductService productService;
    private final JwtService jwtService;

    public String addNewProductToCart(WishItem item) {
        String userId = jwtService.getUserId();
        User user = userService.findById(userId);
        Cart cart = makeCart(user);
        Product product = productService.getProductById(item.getProductId());
        CartDetail cartDetail = new CartDetail(cart.getId(), item.getSize(), String.valueOf(1), product);
        cartDetailRepository.save(cartDetail);

        return "success";
    }

    private Cart makeCart(User s){
        Cart cart = Cart.builder()
                .user(s)
                .build();
        return cartRepository.save(cart);

    }

    private boolean hasCart(String userId){
        return cartRepository.findByUserId(userId).isPresent();

    }
}
