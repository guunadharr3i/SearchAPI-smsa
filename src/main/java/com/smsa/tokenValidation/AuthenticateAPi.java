/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.tokenValidation;

import com.smsa.DTO.RoleMenuDTO;
import com.smsa.encryption.AESUtil;
import com.smsa.entity.UserLogonHstDtl;
import com.smsa.entity.UserRoleGeoMappingMST;
import com.smsa.repository.RoleMenuMstRepository;
import com.smsa.repository.UserLogonHstRepository;
import com.smsa.repository.UserRoleGeoMappingMSTRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author abcom
 */
@Service
@Transactional
public class AuthenticateAPi {


    @Value("${aes.auth.key}")
    private String secretKey;

    @Value("${aes.auth.vi.key}")
    private String viKey;

    @Value("${token.time}")
    private Long tokenTime;

    @Autowired
    public static UserRoleGeoMappingMSTRepository userRoleGeoMappingMSTRepository;
    
    @Autowired
    public static RoleMenuMstRepository roleMenuMstRepository;
    
    @Autowired
    public static UserLogonHstRepository userLogonHstRepository;

    private static final String SECRET_KEY = "mySuperSecureSecretKeyThatIsAtLeast32Bytes!";

    private static final Logger logger = LogManager.getLogger(AuthenticateAPi.class);

    public String validateAndRefreshAccessToken(Map<String, String> tokenRequest) {
        logger.info("Inside validateRefresh accesstoken method");
        try {
            String oldToken = AESUtil.decrypt(tokenRequest.get("token"), secretKey, viKey);
            if (oldToken == null || oldToken.isEmpty()) {
                logger.warn("No token provided for refresh");
                return "Token is required";
            }
        } catch (Exception e) {
            logger.error("exception : "+e.getMessage());
            return null;
        }
        return null;
    }

    private static Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String validateToken(String token) throws JwtException {
        logger.info("Validating token");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String loginId = claims.getId();
            UserRoleGeoMappingMST data = userRoleGeoMappingMSTRepository.findByUserIdAndDeletedDateIsNull(loginId);
            if (data.getActiveStatus().equals("N")) {
                return "Token invalid or user logged out";
            }

            if (loginId == null || loginId.isEmpty()) {
                logger.error("Token does not contain valid login ID");
                throw new JwtException("Token does not contain a valid login ID");
            }

            String newJwt = generateAccessTokenData(data.getUserId(), data.getRoleId());

            logger.info("Token validated and refreshed for loginId: {}", loginId);
            return newJwt;

        } catch (JwtException e) {
            logger.error("Token validation failed: {}", e.getMessage(), e);
            throw new JwtException("Token validation failed: " + e.getMessage());
        }
    }

    public String generateAccessTokenData(String userId, Long roleId) {
        logger.debug("Generating access token for: {}", userId);

        List<RoleMenuDTO> roleMenuDetails = roleMenuMstRepository.findRoleMenuDetails(roleId);

        List<Map<String, Object>> roleMenuList = roleMenuDetails.stream().map(dto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("roleId", dto.getRoleId());
            map.put("roleName", dto.getRoleName());
            map.put("menuId", dto.getMenuId());
            map.put("menuName", dto.getMenuName());
            return map;
        }).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject("access")
                .setIssuedAt(new Date())
                .setId(userId)
                .claim("menuRoles", roleMenuList)
                .setExpiration(new Date(System.currentTimeMillis() + (10 * 60 * 100 * tokenTime)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
  public void verifyValidateUserDevice(String token, String newAccessToken, String deviceHash) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getId();

        UserLogonHstDtl userLogonHstDtl = userLogonHstRepository.findByUserIdAndDeviceHash(userId, deviceHash);

        if (userLogonHstDtl == null) {
            throw new JwtException("Token Device token and hash ");
        }
        if (userLogonHstDtl.getAccessToken() == null) {
            throw new JwtException("Please login token and hash expire.");
        }

        userLogonHstDtl.setAccessToken(newAccessToken);
        userLogonHstRepository.save(userLogonHstDtl);

    }
}
