package com.juliano.pedidos.security.model;

/**
 * Enum que representa os papéis de negócio da aplicação.
 * O mapeamento para as authorities do Spring Security é realizado
 * pelo próprio enum para reduzir o acoplamento entre domínio e
 * infraestrutura.
 */
public enum Role {

    ADMIN("ROLE_ADMIN"),

    CUSTOMER("ROLE_CUSTOMER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}