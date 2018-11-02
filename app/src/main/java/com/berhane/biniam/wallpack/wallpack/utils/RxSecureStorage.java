/*
 * DayTime:10/26/18 12:38 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")
public class RxSecureStorage {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private Context context;
    private String alias;
    private KeyStore keyStore;
    private RxSharedPreferences sharedPreferences;

    public RxSecureStorage(Context context, String alias) {
        this.context = context.getApplicationContext();
        this.alias = alias;
        SharedPreferences prefs =
                context.getSharedPreferences(
                        String.format("%s-%s", context.getPackageName(), alias), Context.MODE_PRIVATE);
        this.sharedPreferences = RxSharedPreferences.create(prefs);
    }

    public static RxSecureStorage create(Context context, String alias) {
        return new RxSecureStorage(context, alias);
    }

    private static Cipher getCipher() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
                // error in android 6: InvalidKeyException: Need RSA private or public key
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            } else {
                // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            }
        } catch (Exception exception) {
            throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
        }
    }

    private static void closeQuietely(Closeable c) {
        try {
            c.close();
        } catch (Throwable ignored) {
        }
    }

    private void initIfNecessary() throws Exception {
        if (keyStore != null) {
            return;
        }
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
            if (!keyStore.containsAlias(alias)) {
                // Generate a key pair for encryption
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec spec =
                        new KeyPairGeneratorSpec.Builder(context.getApplicationContext())
                                .setAlias(alias)
                                .setSubject(new X500Principal("CN=" + alias))
                                .setSerialNumber(BigInteger.TEN)
                                .setStartDate(start.getTime())
                                .setEndDate(end.getTime())
                                .build();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", AndroidKeyStore);
                kpg.initialize(spec);
                kpg.generateKeyPair();
            }
        } catch (Exception e) {
            throw new Exception("Failed to initialize this RxSecureStorage instance.", e);
        }
    }

    public Single<byte[]> encrypt(final byte[] data) {
        return Single.fromCallable(
                new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        initIfNecessary();

                        ByteArrayOutputStream outputStream = null;
                        CipherOutputStream cipherOutputStream = null;
                        try {
                            KeyStore.PrivateKeyEntry privateKeyEntry =
                                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();

                            Cipher input = getCipher();
                            input.init(Cipher.ENCRYPT_MODE, publicKey);

                            outputStream = new ByteArrayOutputStream();
                            cipherOutputStream = new CipherOutputStream(outputStream, input);
                            cipherOutputStream.write(data);
                            closeQuietely(cipherOutputStream);

                            return outputStream.toByteArray();
                        } catch (Exception e) {
                            throw new Exception("Failed to encrypt data with alias" + alias, e);
                        } finally {
                            closeQuietely(cipherOutputStream);
                            closeQuietely(outputStream);
                        }
                    }
                })
                .observeOn(Schedulers.computation());
    }

    public Single<String> encryptString(String text) {
        byte[] textBytes;
        try {
            textBytes = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return Single.error(new Exception("Failed convert text to bytes.", e));
        }
        return encrypt(textBytes)
                .map(
                        new Function<byte[], String>() {
                            @Override
                            public String apply(byte[] encryptedData) throws Exception {
                                return Base64.encodeToString(encryptedData, Base64.DEFAULT);
                            }
                        })
                .observeOn(Schedulers.computation());
    }

    public Single<byte[]> decrypt(final byte[] encryptedData) {
        return Single.fromCallable(
                new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        initIfNecessary();

                        CipherInputStream cipherInputStream = null;
                        ByteArrayOutputStream bos = null;
                        try {
                            KeyStore.PrivateKeyEntry privateKeyEntry =
                                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

                            Cipher output = getCipher();
                            output.init(Cipher.DECRYPT_MODE, privateKey);

                            cipherInputStream =
                                    new CipherInputStream(new ByteArrayInputStream(encryptedData), output);
                            bos = new ByteArrayOutputStream();

                            byte[] buffer = new byte[512];
                            int read;
                            while ((read = cipherInputStream.read(buffer)) != -1) {
                                bos.write(buffer, 0, read);
                            }

                            return bos.toByteArray();
                        } catch (Exception e) {
                            throw new Exception("Failed to decrypt data with " + alias, e);
                        } finally {
                            closeQuietely(cipherInputStream);
                            closeQuietely(bos);
                        }
                    }
                })
                .observeOn(Schedulers.computation());
    }

    public Single<String> decryptString(String encryptedText) {
        byte[] textBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        return decrypt(textBytes)
                .map(
                        new Function<byte[], String>() {
                            @Override
                            public String apply(byte[] encryptedData) throws Exception {
                                return new String(encryptedData, 0, encryptedData.length);
                            }
                        })
                .observeOn(Schedulers.computation());
    }

    public Observable<byte[]> getBytes(String name) {
        return sharedPreferences
                .getString(name)
                .asObservable()
                .map(new Function<String, byte[]>() {
                            @Override
                            public byte[] apply(String base64Value) throws Exception {
                                byte[] encryptedValue = Base64.decode(base64Value, Base64.DEFAULT);
                                return decrypt(encryptedValue).blockingGet();
                            }
                        });
    }

    public Single<Boolean> putBytes(final String name, @Nullable byte[] value) {
        if (value == null) {
            sharedPreferences.getString(name).delete();
            return Single.just(false);
        }
        return encrypt(value)
                .map(new Function<byte[], Boolean>() {
                            @Override
                            public Boolean apply(byte[] encryptedData) throws Exception {
                                String encryptedString = Base64.encodeToString(encryptedData, Base64.DEFAULT);
                                sharedPreferences.getString(name).set(encryptedString);
                                return true;
                            }
                        });
    }

    public Observable<String> getString(String name) {
        return sharedPreferences
                .getString(name)
                .asObservable()
                .map(new Function<String, String>() {
                            @Override
                            public String apply(String encryptedValue) throws Exception {
                                if (encryptedValue == null || encryptedValue.trim().isEmpty()) {
                                    return null;
                                }
                                return decryptString(encryptedValue).blockingGet();
                            }
                        });
    }

    public Single<Boolean> putString(final String name, @Nullable String value) {
        if (value == null) {
            sharedPreferences.getString(name).delete();
            return Single.just(false);
        }
        return encryptString(value)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String encryptedValue) throws Exception {
                        sharedPreferences.getString(name).set(encryptedValue);
                        return true;
                    }
                });
    }

    public void dispose() {
        this.context = null;
        this.alias = null;
        this.keyStore = null;
        this.sharedPreferences = null;
    }
}
