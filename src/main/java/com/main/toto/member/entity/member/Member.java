package com.main.toto.member.entity.member;

import com.main.toto.auction.entity.bid.Bid;
import com.main.toto.bookMark.entity.bookMark.BookMark;
import com.main.toto.global.domain.BaseEntity;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"roleSet","bookMarks","bids"})
@Getter
public class Member extends BaseEntity {

        @Id
        private String mid;

        private String mpassword;

        private String email;

        private boolean del; // 회원 탈퇴 여부

        private boolean social; // 소셜 로그인 여부

        @Enumerated
        @ElementCollection(fetch = FetchType.LAZY)
        @Builder.Default // HashSet을 자동으로 만듬.
        private Set<MemberRole> roleSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

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

        @Builder.Default
        @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
        private Set<BookMark> bookMarks = Collections.newSetFromMap(new ConcurrentHashMap<>());

        @Builder.Default
        @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
        private Set<Bid> bids = Collections.newSetFromMap(new ConcurrentHashMap<>());


        public void clearRoles(){
                this.roleSet.clear();
            }

        public void addBookmark(BookMark bookMark) {
            System.out.println("member: " + this);
            System.out.println("bookMark.getMember(): " + bookMark.getMember());
            System.out.println("bookMark.getBoard(): " + bookMark.getBoard());
            if(bookMark != null) {
                this.bookMarks.add(bookMark);
            }
        }

        public void removeBookmark(BookMark bookMark) {
            this.bookMarks.remove(bookMark);
        }
}
