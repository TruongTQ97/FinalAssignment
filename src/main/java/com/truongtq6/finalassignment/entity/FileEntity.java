package com.truongtq6.finalassignment.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "File_tbl", schema = "Filesystem")
@Data
@NoArgsConstructor
public class FileEntity extends BaseEntity {

    @Builder
    private FileEntity(LocalDateTime createdAt, Long id, String fileName, UserEntity userEntity, String downloadUri, long size, Set<URLEntity> urlEntities, Set<SharedFileEntity> sharedFileEntities) {
        super(createdAt, id);
        this.fileName = fileName;
        this.userEntity = userEntity;
        this.downloadUri = downloadUri;
        this.size = size;
        this.urlEntities = urlEntities;
        this.sharedFileEntities = sharedFileEntities;
    }

    @Column(name="file_name", unique = true)
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    private UserEntity userEntity;

    @Column(name="download_uri")
    private  String downloadUri;

    @Column(name="file_size")
    private long size;

    @OneToMany(mappedBy = "originalFile")
    private Set<URLEntity> urlEntities;

    @OneToMany(mappedBy = "sharedFile")
    private Set<SharedFileEntity> sharedFileEntities;

}
