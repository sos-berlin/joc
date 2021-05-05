package com.sos.joc.classes.security;

import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSSecurityHashSettings {

    private static final String HASH_FORMAT_BASE64_FORMAT = "org.apache.shiro.crypto.hash.format.Base64Format";
    private static final String HASH_FORMAT_HEX_FORMAT = "org.apache.shiro.crypto.hash.format.HexFormat";
    private static final String DEFAULT_PASSWORD_MATCHER = "org.apache.shiro.authc.credential.PasswordMatcher";
    private static final int DEFAULT_GENERATED_SALT_SIZE = 16;
    private static final String DEFAULT_ALGORITHM = "SHA-512";
    private static final String DEFAULT_HASH_FORMAT = "org.apache.shiro.crypto.hash.format.Shiro1CryptFormat";
    private static final String INI_REALM_CREDENTIALS_MATCHER = "iniRealm.credentialsMatcher";
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSSecurityHashSettings.class);
    private String hashingAlgorithm;
    private int hashIterations;
    private String passwordMatcher;
    private String credentialsMatcher;
    private String format;
    private String passwordService;
    private String hashService;
    private String privateSalt;
    private Boolean generatePublicSalt;

    public boolean isCrypt() {
        return (passwordMatcher != null && credentialsMatcher != null && hashingAlgorithm != null && DEFAULT_PASSWORD_MATCHER.equals(
                passwordMatcher));
    }

    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    public void setHashingAlgorithm(String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public String getPasswordMatcher() {
        return passwordMatcher;
    }

    public void setMain(Section main) throws UnsupportedEncodingException {
        this.hashingAlgorithm = DEFAULT_ALGORITHM;
        this.format = DEFAULT_HASH_FORMAT;
        this.hashIterations = DefaultPasswordService.DEFAULT_HASH_ITERATIONS;

        this.credentialsMatcher = main.get(INI_REALM_CREDENTIALS_MATCHER);
        if (credentialsMatcher != null && credentialsMatcher.startsWith("$")) {
            credentialsMatcher = credentialsMatcher.substring(1);
        }

        this.passwordMatcher = main.get(credentialsMatcher);

        if (passwordMatcher != null) {

            String passwordServiceKey = main.get(credentialsMatcher + ".passwordService");
            if (passwordServiceKey != null && passwordServiceKey.startsWith("$")) {
                passwordServiceKey = passwordServiceKey.substring(1);
            }
            if (passwordServiceKey != null) {
                this.passwordService = main.get(passwordServiceKey);
                String formatKey = main.get(passwordServiceKey + ".hashFormat");
                if (formatKey != null && formatKey.startsWith("$")) {
                    formatKey = formatKey.substring(1);
                }
                if (formatKey != null) {
                    this.format = main.get(formatKey);
                    if (isDefaultFormat()) {
                        this.hashIterations = DefaultPasswordService.DEFAULT_HASH_ITERATIONS;
                    } else {
                        this.hashIterations = 1;
                    }
                } else {
                    this.format = DEFAULT_HASH_FORMAT;
                }

                if (this.format == null) {
                    LOGGER.warn("Could not set hashformat with key: " + formatKey);
                    LOGGER.warn("Using default format: " + DEFAULT_HASH_FORMAT);
                    this.format = DEFAULT_HASH_FORMAT;
                }
                String hashServiceKey = main.get(passwordServiceKey + ".hashService");
                if (hashServiceKey != null && hashServiceKey.startsWith("$")) {
                    hashServiceKey = hashServiceKey.substring(1);
                }

                if (hashServiceKey != null) {
                    this.hashService = main.get(hashServiceKey);
                    String alg = main.get(hashServiceKey + ".hashAlgorithmName");
                    if (alg != null) {
                        this.hashingAlgorithm = alg;
                    }
                    String s = main.get(hashServiceKey + ".privateSalt");
                    if (s != null) {
                        byte[] decoded = Base64.decodeBase64(s);
                        s = new String(decoded, "UTF-8");
                        this.privateSalt = s;
                    }

                    s = main.get(hashServiceKey + ".generatePublicSalt");
                    if (s != null) {
                        this.generatePublicSalt = s.equals("true");
                    }

                    try {
                        String iter = main.get(hashServiceKey + ".hashIterations");
                        if (iter != null) {
                            this.hashIterations = Integer.parseInt(main.get(hashServiceKey + ".hashIterations"));
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warn("hashIterations is not int. Using default");
                        this.hashIterations = DefaultPasswordService.DEFAULT_HASH_ITERATIONS;
                    }
                }
            }

            if (credentialsMatcher != null && hashingAlgorithm == null) {
                LOGGER.warn("Unknown Credential/Password matcher: " + passwordMatcher);
            }
        }
        LOGGER.debug("Using hash algorithm: " + hashingAlgorithm);
    }

    public ByteSource getRandomSalt() {
        int byteSize = DEFAULT_GENERATED_SALT_SIZE;
        return new SecureRandomNumberGenerator().nextBytes(byteSize);
    }

    public String getCredentialsMatcher() {
        return credentialsMatcher;
    }

    public String getFormat() {
        return format;
    }

    public String getPasswordService() {
        return passwordService;
    }

    public String getHashService() {
        return hashService;
    }

    public boolean isDefaultFormat() {
        return DEFAULT_HASH_FORMAT.equals(format);
    }

    public HashFormat getFormatObject() {
        if (isDefaultFormat()) {
            return new org.apache.shiro.crypto.hash.format.Shiro1CryptFormat();
        }
        if (HASH_FORMAT_HEX_FORMAT.equals(format)) {
            return new org.apache.shiro.crypto.hash.format.HexFormat();
        }
        if (HASH_FORMAT_BASE64_FORMAT.equals(format)) {
            return new org.apache.shiro.crypto.hash.format.Base64Format();
        }

        return null;
    }

    public ByteSource getPrivateSalt() {
        if (privateSalt != null) {
            return ByteSource.Util.bytes(privateSalt);
        } else {
            return null;
        }
    }

    public Boolean isGeneratePublicSalt() {
        if (generatePublicSalt == null) {
            return isDefaultFormat();
        } else {
            return generatePublicSalt;
        }
    }

}
