package com.main.toto.auction.dto.page;

import com.main.toto.auction.entity.board.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 페이징 관련 정보와 검색의 종류, 키워드를 지정한다.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 9;

    private String keyword;

    private BoardCategory boardCategory;

    // 검색 조건과 페이징 조건을 문자열로 구성
    private String link;

    public Pageable getPageable(String...props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink(){

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=").append(this.page);
            builder.append("&size=").append(this.size);

            if(boardCategory != null){
                builder.append("&boardCategory=").append(boardCategory);
            }

            if(keyword != null){
                try{
                    builder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    // 후에 여기도 채우기
                }
            }
            link = builder.toString();
        }

        return link;
    }

    @Override
    public String toString(){
        return getLink();
    }
}
