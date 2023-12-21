package com.example.DummyTalk.Util;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class FileUploadUtils {

    /***
     * @param uploadDir : 파일이 저장될 경로
     * @param fileName : 파일명
     * @param multipartFile : 파일
     *
     * @return : 저장된 파일명
     */
    public static ImageDto saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        /* 업로드 경로가 존재하지 않을 경우 경로를 먼저 생성한다. */
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        /* 파일명 리네임 */
        String uuid = UUID.randomUUID().toString();
        fileName = Objects.requireNonNull(fileName.replace(" ", "")).split("\\.")[0];
        String replaceFileName = uuid + "_" + fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());


        /* 파일 저장 */
        Path filePath;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = uploadPath.resolve(replaceFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("파일을 저장하지 못하였습니다. filename : " + fileName);
        }
        log.info("\nsaveImage fileName : \n" + replaceFileName);


        return new ImageDto(null, 0, fileName, filePath.toString() , replaceFileName, null, null);
    }

    public static void deleteFile(String uploadDir, String fileName) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}