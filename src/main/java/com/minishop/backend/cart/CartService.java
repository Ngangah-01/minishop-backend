package com.minishop.backend.cart;

import com.minishop.backend.cart.dto.AddToCartRequest;
import com.minishop.backend.cart.dto.CartResponse;
import com.minishop.backend.cart.dto.UpdateCartItemRequest;
import com.minishop.backend.common.exception.ResourceNotFoundException;
import com.minishop.backend.product.Product;
import com.minishop.backend.product.ProductRepository;
import com.minishop.backend.security.CurrentUserProvider;
import com.minishop.backend.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserProvider currentUserProvider;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public CartResponse getMyCart() {
        User user = currentUserProvider.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(()-> cartRepository.save(new Cart(user)));

        return CartResponse.from(cart);
    }

    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCartForCurrentUser();
        Product product = findProductOrThrow(request.productId());

        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int currentQuantity = (existingItem != null) ? existingItem.getQuantity() : 0;
        int targetQuantity = currentQuantity + request.quantity();

        validateStock(product, targetQuantity);

        if (existingItem != null) {
            existingItem.setQuantity(targetQuantity);
        } else {
            CartItem cartItem = new CartItem(product, request.quantity());
            cart.addItem(cartItem);
        }

        cartRepository.save(cart);
        return CartResponse.from(cart);
    }

    public CartResponse updateCartItem(Long productId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCartForCurrentUser();
        Product product = findProductOrThrow(productId);

        int newQuantity = request.quantity();

        // If quantity set to 0 or less, treat as removal
        if (newQuantity <= 0) {
            return removeFromCart(productId);
        }

        validateStock(product, newQuantity);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(product, 0);
                    cart.addItem(newItem);
                    return newItem;
                });

        cartItem.setQuantity(newQuantity);
        cartRepository.save(cart);

        return CartResponse.from(cart);
    }

    public CartResponse removeFromCart(Long productId) {
        User user = currentUserProvider.getCurrentUser();

        cartRepository.findByUser(user).ifPresent(cart -> {
            cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .ifPresent(cart::removeItem);
            cartRepository.save(cart);
        });

        return getMyCart();
    }

    public void clearCart() {
        User user = currentUserProvider.getCurrentUser();
        cartRepository.findByUser(user).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }


//8********************************************************************
    // ==========================================
    // Private Helper Methods (DRY Enforcement)
    // ==========================================

//    preventing a user from modifying another user's cart


    private Cart getOrCreateCartForCurrentUser() {
        User user = currentUserProvider.getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private void validateStock(Product product, int requestedQuantity) {
        if (requestedQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough stock available for '" + product.getName() + "'. " +
                            "Requested: " + requestedQuantity + ", Available: " + product.getStockQuantity()
            );
        }
    }
}