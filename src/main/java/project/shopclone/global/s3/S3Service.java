package project.shopclone.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 파일 업로드 후 CloudFront CNAME URL로 리턴
    public String uploadFile(MultipartFile image, String brand, String productNo) throws IOException {
        String fileName = "inst/" + brand + "/" + productNo + "-" + UUID.randomUUID().toString();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(image.getSize());

        // S3에서 이미지가 바로 열리지 않고 다운로드가 되는 문제 => 코드처럼 contentType을 지정해줘야한다.
        objMeta.setContentType("image/jpg");

        s3Client.putObject(bucket, fileName, image.getInputStream(), objMeta);
        return "https://cdn.hyun-clone.shop/" + fileName;
    }

}
