package com.example.PSP.models;

import com.example.PSP.dtos.CartDto;
import com.example.PSP.util.CartDtoConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requestor_id", nullable = false)
    private Long requestorId;

    @Column(name = "requestor_first_name", nullable = false)
    private String requestorFirstName;

    @Column(name = "requestor_last_name", nullable = false)
    private String requestorLastName;

    @Column(name = "requestor_email", nullable = false)
    private String requestorEmail;

    @Convert(converter = CartDtoConverter.class)
    @Column(name = "cart", nullable = false)
    private CartDto cart;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Session() {
    }

    public Session(Long requestorId, String requestorFirstName, String requestorLastName, String requestorEmail, CartDto cart, User user) {
        this.requestorId = requestorId;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestorEmail = requestorEmail;
        this.cart = cart;
        this.user = user;
    }

    public String getRequestorEmail() {
        return requestorEmail;
    }

    public void setRequestorEmail(String requestorEmail) {
        this.requestorEmail = requestorEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(Long requestorId) {
        this.requestorId = requestorId;
    }

    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    public CartDto getCart() {
        return cart;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
