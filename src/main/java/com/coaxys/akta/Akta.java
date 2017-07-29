package com.coaxys.akta;

import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Akta {

    private static Akta instance;
    private final String apiKey;
    private final String privateApiKey;
    private final String url;

    private Akta(String apiKey, String privateApiKey, String url) {
        this.apiKey = apiKey;
        this.privateApiKey = privateApiKey;
        this.url = url;
    }

    public static Akta get() throws ConfigurationException {
        if (instance == null) {
            throw new ConfigurationException("You must init() first");
        }
        return instance;
    }

    public static void init(String apiKey, String privateApiKey, String url) {
        instance = new Akta(apiKey, privateApiKey, url);
    }

    public void upload(String uid, String project, File file) {
        upload(uid, project, "", file);
    }

    public void upload(String uid, String project, String arbo, File file) {
        try {
            Path mediaTypeSource = Paths.get(file.getAbsolutePath());
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("project", project)
                    .addFormDataPart("arbo", arbo)
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse(Files.probeContentType(mediaTypeSource)), file))
                    .build();

            Request request = new Request.Builder().url(url + "/api/v1/upload?" + getAuthParams()).post(formBody).build();

            Response response = client.newCall(request).execute();
            System.out.println(response.code());
            System.out.println(response.message());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAuthParams() {
        long timestamp = new Date().getTime();
        return "apiKey=" + apiKey + "&timestamp=" + timestamp + "&control=" + getControl(timestamp);
    }

    private String getControl(long timestamp) {
        return new String(Hex.encodeHex(DigestUtils.sha256(timestamp + privateApiKey + apiKey)));
    }

}
