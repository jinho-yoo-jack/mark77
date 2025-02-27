package jack.labs.mark77.service;

import jack.labs.mark77.dto.RespAddNewProductToCart;
import jack.labs.mark77.dto.WishItem;
import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.CartDetail;
import jack.labs.mark77.entity.Product;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.repository.CartDetailRepository;
import jack.labs.mark77.repository.CartRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public RespAddNewProductToCart addNewProductToCart(WishItem item) {
        String userId = jwtService.getUserId(); // 로그인한 유저만 장바구니에 물건을 담을 수 있겠구나
        User user = userService.findById(userId);
        Product product = productService.getProductById(item.getProductId());
        CartDetail cartDetail = CartDetail.builder()
            .cartId(getCart(user))
            .products(product)
            .size(item.getSize())
            .quantity(1L)
            .build();

        cartDetailRepository.save(cartDetail);
        return RespAddNewProductToCart.builder()
                .result("success")
                .build();
    }

    private long getCart(User user) {
        // User.cart_id is null -> make new cart
        // else getCart(); -> return Cart
        if (!hasCart(user)) return makeCart(user).getId();
        else return user.getCart().getId();
    }

    private Cart makeCart(User u) {
        Cart cart = cartRepository.save(new Cart());
        u.setCart(cart);
        return cart;
    }

    private boolean hasCart(User user) {
        return user.getCart() != null;
    }
}
