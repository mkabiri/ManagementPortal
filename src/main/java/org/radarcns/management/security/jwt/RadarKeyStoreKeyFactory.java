package org.radarcns.management.security.jwt;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * Similar to Spring's
 * {@link org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory}. However
 * this class does not assume a specific key type, while the Spring factory assumes RSA keys.
 */
public class RadarKeyStoreKeyFactory {
    private static final Logger logger = LoggerFactory.getLogger(RadarKeyStoreKeyFactory.class);

    private final List<Resource> resources;

    private final char[] password;
    private final KeyStore store;

    public RadarKeyStoreKeyFactory(@Nonnull List<Resource> resources, @Nonnull char[] password) {
        this.resources = Objects.requireNonNull(resources);
        if (this.resources.isEmpty()) {
            throw new IllegalArgumentException("No JWT keystore resource paths specified.");
        }
        this.password = Objects.requireNonNull(password);

        KeyStore localStore = null;
        for (Resource resource : resources) {
            if (!resource.exists()) {
                continue;
            }
            try {
                String fileName = resource.getFilename().toLowerCase(Locale.US);
                if (fileName.endsWith(".pfx") || fileName.endsWith(".p12")) {
                    localStore = KeyStore.getInstance("PKCS12");
                } else {
                    localStore = KeyStore.getInstance("jks");
                }
                localStore.load(resource.getInputStream(), this.password);
                logger.debug("Loaded JWT key store {}", resource);
                break;
            } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException
                    | IOException ex) {
                localStore = null;
                logger.error("Cannot load JWT key store {}", ex);
            }
        }
        if (localStore == null) {
            throw new IllegalArgumentException("Cannot load any of the given JWT key stores "
                    + resources);
        }

        this.store = localStore;
    }

    /**
     * Get a key pair from the store using the store password.
     * @param alias key pair alias
     * @return loaded key pair or {@code null} if the key store does not contain a loadable key with
     *         given alias.
     * @throws IllegalArgumentException if the key alias password is wrong or the key cannot
     *                                  loaded.
     */
    public @Nullable KeyPair getKeyPair(@Nullable String alias) {
        return getKeyPair(alias, password);
    }

    /**
     * Get a key pair from the store with a given alias and password.
     * @param alias key pair alias
     * @param password key pair password
     * @return loaded key pair or {@code null} if the key store does not contain a loadable key with
     *         given alias.
     * @throws IllegalArgumentException if the key alias password is wrong or the key cannot
     *                                  loaded.
     */
    public @Nullable KeyPair getKeyPair(@Nullable String alias, char[] password) {
        try {
            PrivateKey key = (PrivateKey) store.getKey(alias, password);
            if (key == null) {
                logger.warn("JWT key store {} does not contain private key pair for alias {}",
                        resources, alias);
                return null;
            }
            Certificate cert = store.getCertificate(alias);
            if (cert == null) {
                logger.warn("JWT key store {} does not contain certificate pair for alias {}",
                        resources, alias);
                return null;
            }
            PublicKey publicKey = cert.getPublicKey();
            if (publicKey == null) {
                logger.warn("JWT key store {} does not contain public key pair for alias {}",
                        resources, alias);
                return null;
            }
            return new KeyPair(publicKey, key);
        } catch (NoSuchAlgorithmException ex) {
            logger.warn("JWT key store {} contains unknown algorithm for key pair with alias {}: {}",
                    resources, alias, ex.toString());
            return null;
        } catch (UnrecoverableKeyException | KeyStoreException ex) {
            throw new IllegalArgumentException("JWT key store " + resources
                    + " contains unrecoverable key pair with alias "
                    + alias + " (the password may be wrong)", ex);
        }
    }
}
