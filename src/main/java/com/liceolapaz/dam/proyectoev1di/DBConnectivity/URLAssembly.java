package com.liceolapaz.dam.proyectoev1di.DBConnectivity;

import com.liceolapaz.dam.proyectoev1di.ResourcePaths.SSLCerts;
import io.github.cdimascio.dotenv.Dotenv;

public class URLAssembly
{
    public static String assemble()
    {
        Dotenv de = Dotenv.configure()
                .directory(URLAssembly.class.getClassLoader().getResource(".env").toString())
                .filename(".env")
                .load();

        StringBuilder url = new StringBuilder();

        url.append(de.get("DB_URL"));
        url.append("?useSSL=true");
        url.append("&sslMode=VERIFY_IDENTITY");
        url.append("&trustCertificateKeyStoreUrl=");
        url.append(URLAssembly.class.getClassLoader().getResource(SSLCerts.TRUSTSTORE.getResource_path()).toString());
        url.append("&trustCertificateKeyStorePassword=");
        url.append(de.get("TRUSTSTORE_PASSWORD"));

        return url.toString();
    }
}
