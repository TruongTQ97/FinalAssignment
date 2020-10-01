package com.truongtq6.finalassignment.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="User_tbl", schema = "Filesystem")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor@NoArgsConstructor@Builder
public class UserEntity extends BaseEntity implements Serializable {

    @Column(name="username", length = 70, unique = true)
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="role", length = 10)
    private String role;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(
            name="Users_Roles_Tbl", schema = "Filesystem",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")}
    )
    private Set<RoleEntity> roles = new HashSet<>();


    @OneToMany(mappedBy = "userEntity")
    private Set<FileEntity> fileEntity;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(
            name="Url_Sharing_Tbl", schema = "Filesystem",
            joinColumns = {@JoinColumn(name="username", referencedColumnName = "username")},
            inverseJoinColumns = {@JoinColumn(name="shortenURL", referencedColumnName = "shortenURL")}
    )
    private Set<URLEntity> sharedUrl = new HashSet<>();

    @OneToMany(mappedBy = "sharedUser", cascade = CascadeType.ALL)
    private Set<SharedFileEntity> mySharedFile;

}
