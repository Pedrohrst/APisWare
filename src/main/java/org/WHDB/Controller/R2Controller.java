package org.WHDB.Controller;

import org.WHDB.R2.R2Cliente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/r2")
public class R2Controller {

    private final R2Cliente r2Cliente;
    private final String bucketName;

    public R2Controller(
            @Value("${cloudflare.r2.access-key}") String accessKey,
            @Value("${cloudflare.r2.secret-key}") String secretKey,
            @Value("${cloudflare.r2.account-id}") String accountId,
            @Value("${cloudflare.r2.bucket-name}") String bucketName
    ) {
        this.r2Cliente = new R2Cliente(accessKey, secretKey, accountId, bucketName);
        this.bucketName = bucketName;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            System.out.println("Nome do arquivo: '" + originalName + "'");

            Path tempFile = Files.createTempFile("upload-", originalName);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            r2Cliente.upload(originalName, tempFile);

            Files.delete(tempFile);

            return ResponseEntity.ok("Upload realizado com sucesso: " + originalName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro no upload: " + e.getMessage());
        }
    }
    @GetMapping("/list")
    public ResponseEntity<List<String>> listar() {
        try {
            List<String> lista = r2Cliente.listarObjetos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/imagem/{key}")
    public ResponseEntity<byte[]> baixarImagem(@PathVariable String key) {
        try {
            // Cria a requisição para buscar o objeto no bucket
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Busca o objeto e lê o conteúdo em bytes
            ResponseBytes<?> objectBytes = r2Cliente.getS3Client().getObjectAsBytes(getObjectRequest);
            byte[] data = objectBytes.asByteArray();

            // Define o content type baseado na extensão do arquivo
            String contentType = "image/png"; // ou você pode inferir pelo key

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
