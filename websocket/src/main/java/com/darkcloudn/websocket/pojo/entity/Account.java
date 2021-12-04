package com.darkcloudn.websocket.pojo.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@DynamicInsert
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String username;

    private String businessPartnerNum;

    private String password;

    private String lastPassword;

    private String email;

    private String phoneNumber;

    @Generated(GenerationTime.INSERT)
    private String loginFlag;

    @Generated(GenerationTime.INSERT)
    private String status;

    private Date latestLoginTime;

    private Date failedLoginTime;

    private Date tempLockedTime;

    @Generated(GenerationTime.INSERT)
    private Integer failedLoginNumber;

    @Generated(GenerationTime.INSERT)
    private String updatedBy;

    @Generated(GenerationTime.INSERT)
    private String createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

    private String address;

    private Date dateOfBirth;

    @Column(name = "citizen_id_number")
    private String citizenIDNumber;

    private String gender;

    public Account() {
    }
}