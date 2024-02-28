package me.forbai;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        System.out.println(getHWID());
        System.out.println(decrypt("kw9GDVdlzrAzti65rjjNOMYXX76ViwnXtkzqafUjsXBJ69QtRD0qm1FrQDxyUFq/mgH8anOVYBUd7k+n9puycmpJOo6FVYn8cQODCAZ02Mc=", String.valueOf(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().hashCode())));
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDialog dialog = createDialog();
        dialog.setVisible(true);
    }

    private static JDialog createDialog() {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter your license key:"));
        JTextField textField = new JTextField(20);
        panel.add(textField);
        panel.add(createSubmitButton(textField, dialog));

        dialog.add(panel);
        return dialog;
    }

    private static String encrypt(String strToEncrypt, String secret) {
        try {
            byte[] key = secret.getBytes(StandardCharsets.UTF_8);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            Key secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    private static String decrypt(String strToDecrypt, String secret) {
        try {
            byte[] key = secret.getBytes(StandardCharsets.UTF_8);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            Key secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }

    private static String getHWID() {
        return String.valueOf((System.getProperty("os.name") +
                System.getProperty("os.version") +
                System.getProperty("os.arch") +
                System.getProperty("user.home") +
                System.getProperty("java.version") +
                System.getProperty("java.vendor") +
                System.getenv("PROCESSOR_IDENTIFIER") +
                System.getenv("COMPUTERNAME") +
                System.getProperty("user.name")).hashCode());
    }

    private static JButton createSubmitButton(JTextField textField, JDialog dialog) {
        JButton button = new JButton("Submit");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.GRAY);
        button.addActionListener(e -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/ForBai/JavaReversingForBeginners/main/wantedkeyfromurl.txt");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String[] splitKey = reader.readLine().split("-");
                reader.close();
                System.out.println(Arrays.toString(splitKey));
                String encryptionToken = splitKey[0];
                String hwid = splitKey[1].replaceAll("\"", "");
                String licenseKey = splitKey[2];

                if (textField.getText().equals(decrypt(licenseKey, encryptionToken))) {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(null, "Correct license key!");
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect license key!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        });
        return button;
    }


}