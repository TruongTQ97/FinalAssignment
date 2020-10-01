package com.truongtq6.finalassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="UrlTbl", schema = "Filesystem")
@Data
@AllArgsConstructor@NoArgsConstructor
public class URLEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "originalURL", referencedColumnName = "download_uri")
    private FileEntity originalFile;

    private String shortenURL;

    private LocalDateTime expiredDate;

    @ManyToMany(mappedBy = "sharedUrl", fetch = FetchType.EAGER)
    private Set<UserEntity> sharedUser = new HashSet<>();

    @Builder
    public URLEntity(LocalDateTime createdAt, Long id, FileEntity originalFile, String shortenURL, LocalDateTime expiredDate, Set<UserEntity> sharedUser) {
        super(createdAt, id);
        this.originalFile = originalFile;
        this.shortenURL = shortenURL;
        this.expiredDate = expiredDate;
        this.sharedUser = sharedUser;
    }
}
