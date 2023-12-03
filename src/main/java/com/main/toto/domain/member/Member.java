package com.main.toto.domain.member;

import com.main.toto.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "roleSet")
@Getter
public class Member extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String mid;

        private String mpassword;

        private String email;

        private boolean del; // 회원 탈퇴 여부

        private boolean social; // 소셜 로그인 여부

        @Enumerated
        @ElementCollection(fetch = FetchType.LAZY)
        @Builder.Default // HashSet을 자동으로 만듬.
        private Set<MemberRole> roleSet = new HashSet<>();

        public void changePassword(String mpassword){
            this.mpassword = mpassword;
        }

        public void changeEmail(String email){
            this.email = email;
        }

        public void changeDel(boolean del){
            this.del = del;
        }

        public void changeSocial(boolean social){
            this.social = social;
        }

        public void addRole(MemberRole memberRole){
            this.roleSet.add(memberRole);
        }

        public void clearRoles(){
            this.roleSet.clear();
        }
}
