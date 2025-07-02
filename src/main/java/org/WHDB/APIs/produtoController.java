package org.WHDB.APIs;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "*")
public class produtoController {

    private final imagemService imagemService;

    public produtoController(imagemService imagemService) {
        this.imagemService = imagemService;
    }

    @GetMapping("/todos")
    public List<produtomodel> listarTodosProdutos() {
        return Produto.listarProdutos();
    }

    @PostMapping("/inserir")
    public boolean inserirProduto(@RequestParam String nome, @RequestParam int quantidade, @RequestParam double preco) {
        return Produto.inserirProduto(nome, quantidade, preco);
    }

    @GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> buscarImagem(@PathVariable int id) {
        try {
            byte[] imagemReduzida = imagemService.getImagemReduzida(id);
            if (imagemReduzida == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
                    .body(imagemReduzida);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
