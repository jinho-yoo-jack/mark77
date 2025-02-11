package jack.labs.mark77.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class WishItem {
    private final long productId;
    private final String size;

    public static WishItem of(ReqAddNewProduct requestDto){
        return WishItem.builder()
                .productId(requestDto.getProductId())
                .size(requestDto.getSize())
                .build();
    }
}
