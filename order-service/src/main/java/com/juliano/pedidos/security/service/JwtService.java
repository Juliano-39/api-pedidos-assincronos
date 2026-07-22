package com.juliano.pedidos.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    // Chave que futuramente será armazenada em variável de ambiente 
    // ou serviço de gerenciamento de segredos
    private static final String SECRET_KEY = "WQvQH03CFphCk369xsIQctFtHV7N2TkCT3pdPdiR52o=";

    // Método para gerar um token JWT para um usuário
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>()) // Pode adicionar claims personalizadas aqui
                .setSubject(userDetails.getUsername()) // Define o assunto do token como o nome de usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiração do token (10 horas)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta
                .compact(); // Compacta o token em uma string
    }

    // Método para extrair o nome de usuário do token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Método de verificação de validade por usuário ou expiração
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Método para verificar se o token expirou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Método para extrair a data de expiração do token JWT
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método genérico para extrair qualquer claim do token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Método para extrair todas as claims do token JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Define a chave de assinatura para validar o token
                .build()
                .parseClaimsJws(token) // Analisa o token JWT
                .getBody(); // Retorna o corpo do token, que contém as claims   
    }

    // Método para obter a chave de assinatura a partir da chave secreta
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica a chave secreta em bytes
        return Keys.hmacShaKeyFor(keyBytes); // Gera a chave de assinatura HMAC-SHA a partir dos bytes
    }

}