package com.coaxys.akta;

import com.google.gson.Gson;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

public class Akta {

    private static Akta instance;
    private final String apiKey;
    private final String privateApiKey;
    private final String url;

    private Gson gson = new Gson();

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

    public Optional<AktaFile> upload(String uid, String project, File file) {
        return upload(uid, project, "", file);
    }

    public Optional<AktaFile> upload(String uid, String project, String arbo, File file) {
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
            if (response.code() == 200 && response.body() != null) {
                return Optional.ofNullable(gson.fromJson(response.body().string(), AktaFile.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<AktaFile> upload(String uid, String project, String fileName, String contentType, byte[] data) {
        return upload(uid, project, "", fileName, contentType, data);
    }

    public Optional<AktaFile> upload(String uid, String project, String arbo, String fileName, String contentType, byte[] data) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("project", project)
                    .addFormDataPart("arbo", arbo)
                    .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse(contentType), data))
                    .build();

            Request request = new Request.Builder().url(url + "/api/v1/upload?" + getAuthParams()).post(formBody).build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                return Optional.ofNullable(gson.fromJson(response.body().string(), AktaFile.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String getAuthParams() {
        long timestamp = new Date().getTime();
        return "apiKey=" + apiKey + "&timestamp=" + timestamp + "&control=" + getControl(timestamp);
    }

    private String getControl(long timestamp) {
        return new String(Hex.encodeHex(DigestUtils.sha256(timestamp + privateApiKey + apiKey)));
    }

}
