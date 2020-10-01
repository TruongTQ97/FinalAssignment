package com.truongtq6.finalassignment.entity;

import com.truongtq6.finalassignment.constant.Permission;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="SharedFile", schema = "Filesystem")
@Data
@NoArgsConstructor
public class SharedFileEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "shared_user", referencedColumnName = "username")
    private UserEntity sharedUser;

    @ManyToOne
    @JoinColumn(name = "shared_file", referencedColumnName = "file_name")
    private FileEntity sharedFile;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    @Builder
    private SharedFileEntity(LocalDateTime createdAt, Long id, UserEntity sharedUser, FileEntity sharedFile, Permission permission) {
        super(createdAt, id);
        this.sharedUser = sharedUser;
        this.sharedFile = sharedFile;
        this.permission = permission;
    }
}
