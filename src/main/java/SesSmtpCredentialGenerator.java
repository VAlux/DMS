import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * generates amazon ses smtp credentials from aws secret key.
 */
public abstract class SesSmtpCredentialGenerator {

    /**
     *Put your AWS secret access key in this environment variable.
     */
    private static final String KEY_ENV_VARIABLE = "AWS_SECRET_ACCESS_KEY";

    /**
     * Used to generate the HMAC signature. Do not modify.
     */
    private static final String MESSAGE = "SendRawEmail";

    /**
     * Version number. Do not modify.
     */
    private static final byte VERSION =  0x02;

    /**
     * generate amazon ses smtp credentials from aws secret key.
     * @param key AWS secret key.
     * @return ses smtp password for specified secret key.
     */
    public static String generate(final String key) {
        if (key == null) {
            System.err.println("Error: no AWS secret key specified.");
            System.exit(0);
        }

        // Create an HMAC-SHA256 key from the raw bytes of the AWS secret access key.
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        String smtpPassword = null;

        try {
            // Get an HMAC-SHA256 Mac instance and initialize it with the AWS secret access key.
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);

            // Compute the HMAC signature on the input data bytes.
            byte[] rawSignature = mac.doFinal(MESSAGE.getBytes());

            // Prepend the version number to the signature.
            byte[] rawSignatureWithVersion = new byte[rawSignature.length + 1];
            byte[] versionArray = {VERSION};
            System.arraycopy(versionArray, 0, rawSignatureWithVersion, 0, 1);
            System.arraycopy(rawSignature, 0, rawSignatureWithVersion, 1, rawSignature.length);

            // To get the final SMTP password, convert the HMAC signature to base 64.
            smtpPassword = DatatypeConverter.printBase64Binary(rawSignatureWithVersion);
        } catch (Exception ex) {
            System.err.println("Error generating SMTP password: " + ex.getMessage());
        }
        return smtpPassword;
    }
}