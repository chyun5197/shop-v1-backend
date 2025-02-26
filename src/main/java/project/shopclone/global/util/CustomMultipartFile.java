package project.shopclone.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

// BufferedImage를 MultipartFile로 바꾸기 위해서는 MultipartFile 인터페이스를 implements해 구현체를 만들어줘야한다.
// 참고) https://velog.io/@o_z/Image-URL-MultipartFile-%EB%B3%80%ED%99%98%ED%95%98%EA%B8%B0-tmc3pnb8
public class CustomMultipartFile implements MultipartFile {
    private byte[] input;
    private String filename;

    public CustomMultipartFile(byte[] input,String filename) {
        this.input = input;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return input == null || input.length == 0;
    }

    @Override
    public long getSize() {
        return input.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return input;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(dest)){
            fos.write(input);
        }
    }
}
