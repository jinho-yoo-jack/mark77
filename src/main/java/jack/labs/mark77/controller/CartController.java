package jack.labs.mark77.controller;

import jack.labs.mark77.dto.ReqAddNewProduct;
import jack.labs.mark77.dto.WishItem;
import jack.labs.mark77.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;


    @PostMapping("/add")
    public String addItem(@RequestBody ReqAddNewProduct requestMessage) {
        return cartService.addNewProductToCart(WishItem.of(requestMessage));
    }
}
