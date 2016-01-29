package de.vorb.platon.security;

import de.vorb.platon.util.CurrentTimeProvider;

import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.annotation.Repeat;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Instant;

@RunWith(MockitoJUnitRunner.class)
public class HmacRequestVerifierTest {

    @Mock
    private SecretKeyProvider secretKeyProvider;

    @Mock
    private CurrentTimeProvider currentTimeProvider;

    private HmacRequestVerifier requestVerifier;

    @Before
    public void setUp() throws Exception {

        // generate a secret key once and always return that secret key
        final SecretKey secretKey = KeyGenerator.getInstance(
                HmacRequestVerifier.HMAC_ALGORITHM.toString()).generateKey();
        Mockito.when(secretKeyProvider.getSecretKey()).thenReturn(secretKey);

        requestVerifier = new HmacRequestVerifier(secretKeyProvider, currentTimeProvider);

    }

    @Test
    @Repeat(10)
    public void testGetSignatureTokenIsRepeatable() throws Exception {

        final String identifier = "comment/1";
        final Instant expirationDate = Instant.now();

        final byte[] firstSignatureToken = requestVerifier.getSignatureToken(identifier, expirationDate);
        final byte[] secondSignatureToken = requestVerifier.getSignatureToken(identifier, expirationDate);

        Truth.assertThat(firstSignatureToken).isEqualTo(secondSignatureToken);

    }

    @Test
    public void testTokenExpiration() throws Exception {

        final String identifier = "comment/1";

        final Instant currentTime = Instant.now();
        Mockito.when(currentTimeProvider.get()).thenReturn(currentTime);

        final Instant expirationDate = currentTime.minusMillis(1); // token expired 1ms ago
        final byte[] signatureToken = requestVerifier.getSignatureToken(identifier, expirationDate);

        final boolean validity = requestVerifier.isRequestValid(identifier, expirationDate, signatureToken);

        Truth.assertThat(validity).isFalse();

    }

    @Test
    public void testCannotFakeExpirationDate() throws Exception {

        final String identifier = "comment/1";

        final Instant currentTime = Instant.now();
        Mockito.when(currentTimeProvider.get()).thenReturn(currentTime);

        final Instant expirationDate = currentTime.minusMillis(1);
        final byte[] signatureToken = requestVerifier.getSignatureToken(identifier, expirationDate);

        // a user attempts to set the expiration date manually (without changing the token)
        final Instant fakedExpirationDate = currentTime;

        final boolean validity = requestVerifier.isRequestValid(identifier, fakedExpirationDate, signatureToken);

        Truth.assertThat(validity).isFalse();

    }

}
