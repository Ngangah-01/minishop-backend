package io.cart;

import io.cart.dto.AddToCartRequest;
import io.cart.dto.CartResponse;
import io.cart.dto.UpdateCartItemRequest;
import io.common.exception.ResourceNotFoundException;
import io.product.Product;
import io.product.ProductVariant;
import io.product.ProductRepository;
import io.security.CurrentUserProvider;
import io.user.User;
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

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCartForCurrentUser();
        Product product = findProductOrThrow(request.productId());
        ProductVariant variant = findVariantOrThrow(product, request.variantId());

        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int currentQuantity = (existingItem != null) ? existingItem.getQuantity() : 0;
        int targetQuantity = currentQuantity + request.quantity();

        if (existingItem != null && !sameVariant(existingItem.getVariant(), variant)) {
            throw new IllegalArgumentException(
                    "A cart can contain only one variant of a product; remove the current item first");
        }
        validateStock(product, variant, targetQuantity);

        if (existingItem != null) {
            existingItem.setQuantity(targetQuantity);
        } else {
            CartItem cartItem = new CartItem(product, variant, request.quantity());
            cart.addItem(cartItem);
        }

        cartRepository.save(cart);
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long productId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCartForCurrentUser();
        Product product = findProductOrThrow(productId);

        int newQuantity = request.quantity();

        // If quantity set to 0 or less, treat as removal
        if (newQuantity <= 0) {
            return removeFromCart(productId);
        }

        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);
        if (existingItem == null && !product.getVariants().isEmpty()) {
            throw new IllegalArgumentException(
                    "Select a variant using the add-to-cart endpoint before updating this product");
        }
        validateStock(product, existingItem == null ? null : existingItem.getVariant(), newQuantity);

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

    @Transactional
    public CartResponse removeFromCart(Long productId) {
        User user = currentUserProvider.getCurrentUser();

        cartRepository.findByUser(user).ifPresent(cart -> {
            cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .ifPresent(cart::removeItem);
            cartRepository.save(cart);
        });

        return getMyCart();
    }

    @Transactional
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

    private void validateStock(Product product, ProductVariant variant, int requestedQuantity) {
        if (variant == null && !product.getVariants().isEmpty()) {
            throw new IllegalArgumentException("A variant must be selected for this product");
        }
        int available = variant == null ? product.getStockQuantity() : variant.getStockQuantity();
        if (requestedQuantity > available) {
            throw new IllegalArgumentException(
                    "Not enough stock available for '" + product.getName() + "'. " +
                        "Requested: " + requestedQuantity + ", Available: " + available
            );
        }
    }

    private ProductVariant findVariantOrThrow(Product product, Long variantId) {
        if (product.getVariants().isEmpty()) {
            if (variantId != null) {
                throw new IllegalArgumentException("This product does not have variants");
            }
            return null;
        }
        if (variantId == null) {
            throw new IllegalArgumentException("A variant must be selected for this product");
        }
        return product.getVariants().stream()
                .filter(variant -> variant.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Variant with id " + variantId + " not found for product " + product.getId()));
    }

    private boolean sameVariant(ProductVariant current, ProductVariant requested) {
        return current == null ? requested == null
                : requested != null && current.getId().equals(requested.getId());
    }
}