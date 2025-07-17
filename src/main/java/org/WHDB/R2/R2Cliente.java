package org.WHDB.R2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.util.ArrayList;
import java.util.List;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

@Component
public class R2Cliente {

    private final S3Client s3;
    private final String bucketName;

    public R2Cliente(
            @Value("${cloudflare.r2.access-key}") String accessKey,
            @Value("${cloudflare.r2.secret-key}") String secretKey,
            @Value("${cloudflare.r2.account-id}") String accountId,
            @Value("${cloudflare.r2.bucket-name}") String bucketName
    ) {
        this.bucketName = bucketName;
        this.s3 = S3Client.builder()
                .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    public void upload(String key, Path filePath) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(request, filePath);
    }

    public List<String> listarObjetos() {
        List<String> resultados = new ArrayList<>();

        ListObjectsV2Request req = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response res = s3.listObjectsV2(req);

        for (S3Object obj : res.contents()) {
            resultados.add(obj.key());
        }

        return resultados;
    }

    public S3Client getS3Client() {
        return s3;
    }


}
