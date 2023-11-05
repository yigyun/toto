package com.main.toto.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})

public class BaseEntity {
}
