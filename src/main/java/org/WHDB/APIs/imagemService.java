package org.WHDB.APIs;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class imagemService {

    // Busca a imagem original do banco e reduz para o tamanho desejado
    public byte[] getImagemReduzida(int id) throws IOException {
        byte[] imagemOriginal = Produto.buscarImagemPorId(id);

        if (imagemOriginal == null) {
            return null;
        }

        return reduzirImagem(imagemOriginal, 200, 200); // largura e altura desejadas
    }

    // Método para reduzir a imagem
    private byte[] reduzirImagem(byte[] imagemOriginal, int largura, int altura) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(imagemOriginal);
        BufferedImage original = ImageIO.read(in);

        if (original == null) {
            return null; // imagem inválida ou formato não suportado
        }

        Image redimensionada = original.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = novaImagem.createGraphics();
        g.drawImage(redimensionada, 0, 0, null);
        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(novaImagem, "jpg", out);

        return out.toByteArray();
    }
}
