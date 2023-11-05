package com.main.toto.domain.member;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "roleSet")
@Getter
public class Member {
}
