package io.cart;

import io.cart.dto.AddToCartRequest;
import io.cart.dto.CartResponse;
import io.cart.dto.UpdateCartItemRequest;
import io.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getMyCart() {

        CartResponse cart = cartService.getMyCart();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart retrieved successfully",
                        cart
                )
        );
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request
    ) {

        CartResponse cart =
                cartService.addToCart(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product added to cart successfully",
                        cart
                )
        );
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        CartResponse cart =
                cartService.updateCartItem(
                        productId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart item updated successfully",
                        cart
                )
        );
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @PathVariable Long productId
    ) {

        CartResponse cart =
                cartService.removeFromCart(productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product removed from cart successfully",
                        cart
                )
        );
    }

    @DeleteMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {

        cartService.clearCart();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart cleared successfully",
                        null
                )
        );
    }
}