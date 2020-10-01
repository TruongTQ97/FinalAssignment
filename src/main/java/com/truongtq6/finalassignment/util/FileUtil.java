package com.truongtq6.finalassignment.util;

import com.truongtq6.finalassignment.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class FileUtil {

    public static void uploadFile(Path targetLocation, String username, MultipartFile file, String filename) {

        findUserDirectory(targetLocation);

        try {
            Files.copy(file.getInputStream(), targetLocation.toAbsolutePath().resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Unable to upload file: username: "  + username);
            log.error(e.getMessage());
        }
    }


    public static void findUserDirectory(Path path) {
        File dir = new File(path.resolve("").toAbsolutePath().toString());
        if (!dir.exists()) {
            if (!dir.mkdirs())
                log.error("Cannot create directory.");
        }
    }

    public static String generateDistinctFilename(String filename){
        return FilenameUtils.getBaseName(filename) +  Instant.now().toEpochMilli() + FileConstant.DOT_EXTENSION + FilenameUtils.getExtension(filename);
    }

    public static String processFileForDownload(Resource resource, HttpServletRequest request){
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = FileConstant.DEFAULT_CONTENT_TYPE;
        }

        return contentType;
    }

    public static boolean deleteFileByUrl(String url) {
        try {
            if (Paths.get(url).toFile().exists()) {
                Files.deleteIfExists(Paths.get(url));
                log.info("File deleted successfully! ({})", url);
                return true;
            } else {
                log.error("The file is not exist to be deleted! {}", url);
                return false;
            }
        } catch (Exception e) {
            log.error("Error when deleting file in directory {}", url);
            return false;
        }
    }


    public static File zipFiles(Path directory, String zippedFileName, List<File> fileToZip) {
        String zipPath = directory.toAbsolutePath().toString() + File.separator + zippedFileName + FileConstant.ZIP_EXTENSION;

        if (Paths.get(zipPath).toFile().exists()) { Paths.get(zipPath).toFile().delete();}

        try (FileOutputStream fos = new FileOutputStream(zipPath); ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos))) {
            zos.setLevel(9);
            for (File file : fileToZip) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    for (int c = fis.read(); c != -1; c = fis.read()) {
                        zos.write(c);
                    }
                    zos.flush();
                }
            }
        } catch (Exception e) {
            log.error("Error when creating zip file!");
        }

        File zippedFile = new File(zipPath);
        if (!zippedFile.exists()) {
            log.error("The created zip file could not be found!");
        }
        return zippedFile;
    }

}