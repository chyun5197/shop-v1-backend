package project.shopclone.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.global.s3.S3Service;
import project.shopclone.global.util.CustomMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    // 원본사이트의 이미지 url -> 이미지로 변환 -> S3에 저장
    @Transactional
    public void imageConvertAndSaveS3()  {
        List<Product> productList =  productRepository.findAll(); // DB 모든 Product 대상
        for (Product product : productList) {
            String imageOriginUrl = product.getImage();
            String brand  = product.getBrand();
            String productNo = String.valueOf(product.getNo());

            try{
                MultipartFile multipartFileImage = convertUrlToMultipartFile(imageOriginUrl);
                String cdnUrl = s3Service.uploadFile(multipartFileImage, brand, productNo);
                product.updateCdnUrl(cdnUrl);
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

    // 외부url의 이미지 -> BufferedImage -> MultipartFile로 변환
    private MultipartFile convertUrlToMultipartFile(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        try(InputStream inputStream = url.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            // 1) image url -> byte[]
            BufferedImage urlImage = ImageIO.read(inputStream);
            ImageIO.write(urlImage, "jpg", bos);
            byte[] byteArray = bos.toByteArray();

            // 2) byte[] -> MultipartFile
            return new CustomMultipartFile(byteArray, imageUrl);
        }
    }

}
