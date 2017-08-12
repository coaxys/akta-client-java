package com.coaxys.akta;

import com.coaxys.akta.exception.AktaException;
import com.coaxys.akta.models.AktaFile;
import com.coaxys.akta.models.AktaProject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public Optional<AktaFile> upload(String uid, String project, File file) throws AktaException {
        return upload(uid, project, "", file);
    }

    public Optional<AktaFile> upload(String uid, String project, String arbo, File file) throws AktaException {
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
            throw new AktaException("Unable to upload your file", e);
        }
        return Optional.empty();
    }

    public Optional<AktaFile> upload(String uid, String project, String fileName, String contentType, byte[] data) throws AktaException {
        return upload(uid, project, "", fileName, contentType, data);
    }

    public Optional<AktaFile> upload(String uid, String project, String arbo, String fileName, String contentType, byte[] data) throws AktaException {
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
            throw new AktaException("Unable to upload your file", e);
        }
        return Optional.empty();
    }

    public Optional<AktaFile> edit(String uid, String project, String originFileName, String newFileName) throws AktaException {
        return edit(uid, project, "", originFileName, "", newFileName);
    }

    public Optional<AktaFile> edit(String uid, String project, String originArbo, String originFileName, String newArbo, String newFileName) throws AktaException {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("project", project)
                    .addFormDataPart("originArbo", originArbo)
                    .addFormDataPart("originFileName", originFileName)
                    .addFormDataPart("newArbo", newArbo)
                    .addFormDataPart("newFileName", newFileName)
                    .build();

            Request request = new Request.Builder().url(url + "/api/v1/edit?" + getAuthParams()).post(formBody).build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                return Optional.ofNullable(gson.fromJson(response.body().string(), AktaFile.class));
            }
        } catch (IOException e) {
            throw new AktaException("Unable to edit your file", e);
        }
        return Optional.empty();
    }

    public boolean delete(String uid, String project, String fileName) throws AktaException {
        return delete(uid, project, "", fileName);
    }

    public boolean delete(String uid, String project, String arbo, String fileName) throws AktaException {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("project", project)
                    .addFormDataPart("arbo", arbo)
                    .addFormDataPart("fileName", fileName)
                    .build();

            Request request = new Request.Builder().url(url + "/api/v1/delete?" + getAuthParams()).post(formBody).build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null && response.body().string().equals("deleted")) {
                return true;
            }
        } catch (IOException e) {
            throw new AktaException("Unable to delete your file", e);
        }
        return false;
    }

    private String getAuthParams() {
        long timestamp = new Date().getTime();
        return "apiKey=" + apiKey + "&timestamp=" + timestamp + "&control=" + getControl(timestamp);
    }

    private String getControl(long timestamp) {
        return new String(Hex.encodeHex(DigestUtils.sha256(timestamp + privateApiKey + apiKey)));
    }

    public List<AktaProject> projects(String uid) throws AktaException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/api/v1/" + uid + "/projects?" + getAuthParams())
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                Type listType = new TypeToken<ArrayList<AktaProject>>() {
                }.getType();
                return gson.fromJson(response.body().string(), listType);
            }
        } catch (IOException e) {
            throw new AktaException("Unable to retrieve your projects", e);
        }
        return new ArrayList<>();
    }

    public List<AktaFile> projectFiles(String uid, String project) throws AktaException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/api/v1/" + uid + "/" + project + "/files?" + getAuthParams())
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                Type listType = new TypeToken<ArrayList<AktaFile>>() {
                }.getType();
                return gson.fromJson(response.body().string(), listType);
            }
        } catch (IOException e) {
            throw new AktaException("Unable to retrieve your projects", e);
        }
        return new ArrayList<>();
    }
}
