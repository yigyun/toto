package com.main.toto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidDTO {

    // bid id, board id, member id
    private Long bidId;
    private Long bno;
    private String mid;
    private Long price;

}
