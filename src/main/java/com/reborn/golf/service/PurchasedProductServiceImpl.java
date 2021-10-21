package com.reborn.golf.service;

import com.reborn.golf.api.CoinExchange;
import com.reborn.golf.api.ContractService;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.dto.exception.TokenTransactionException;
import com.reborn.golf.dto.exception.WrongStepException;
import com.reborn.golf.dto.shop.ProductDto;
import com.reborn.golf.dto.shop.ProductImageDto;
import com.reborn.golf.dto.shop.PurchasedProductDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.PurchasedProductStep;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.repository.CategoryRepository;
import com.reborn.golf.repository.MemberRepository;
import com.reborn.golf.repository.PurchasedProductImageRepository;
import com.reborn.golf.repository.PurchasedProductRepository;
import jnr.a64asm.Mem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class PurchasedProductServiceImpl implements PurchasedProductService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final PurchasedProductImageRepository purchasedProductImageRepository;
    private final CoinExchange coinExchange;
    private final Double tokenAmountRatePerCost = 0.1;
    private final ContractService contractService;

    @Override
    @Transactional
    public PageResultDto<Object[], PurchasedProductDto> getListWithUser(Integer memberIdx, PageRequestDto requestDto) {
        Page<Object[]> result = purchasedProductRepository.getPurchasedItemsbyMemberIdx(memberIdx, requestDto.getPageable(Sort.by("regDate").descending()));

//        Function<Object[], PurchasedProductDto> fn = (arr -> entitiesToDto((PurchasedProduct) arr[0], List.of((PurchasedProductImage) arr[1]), (String) arr[2]));
        Function<Object[], PurchasedProductDto> fn = (arr -> {
            PurchasedProduct items = (PurchasedProduct) arr[0];
            List<PurchasedProductImage> purchasedProductImageList = List.of((PurchasedProductImage) arr[1]);
            String categoryName = (String) arr[2];
            //이미지 처리
            List<ProductImageDto> productImageDtoList = purchasedProductImageList.stream().map(productImage ->
                    ProductImageDto.builder()
                            .imgName(productImage.getImgName())
                            .path(productImage.getPath())
                            .uuid(productImage.getUuid())
                            .build()).collect(Collectors.toList());
            //유저희망가격의 비용
            Integer expectedPrice = (int) (items.getPrice() * (1 - tokenAmountRatePerCost));
            //유저희망가격의 토큰 수량
            Long expectedPointAmount = (long) (items.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            //제안 가격의 비용
            Integer possiblePrice = (int) (items.getPossiblePrice() * (1 - tokenAmountRatePerCost));
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (items.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());


            return PurchasedProductDto.builder()
                    .idx(items.getIdx())
                    .catagory(categoryName)
                    .brand(items.getBrand())
                    .name(items.getName())
                    .state(items.getState())
                    .price(items.getPrice())
                    .quentity(items.getQuentity())
                    .address(items.getAddress())
                    .details(items.getDetails())
                    .expectedPrice(expectedPrice)
                    .expectedPointAmount(expectedPointAmount)
                    .proposalPrice(possiblePrice)
                    .proposalTokenAmount(possiblePointAmount)
                    .canceled(items.isCanceled())
                    .step(items.getPurchasedProductStep().name())
                    .imageDtoList(productImageDtoList)
                    .regDate(items.getRegDate())
                    .modDate(items.getModDate())
                    .build();
        });

        return new PageResultDto<>(result, fn);
    }

    @Override
    public PurchasedProductDto read(Integer memberIdx, Long idx) {
        try {
            List<Object[]> result = purchasedProductRepository.getItembyIdxAndMemberIdxWithImage(memberIdx, idx);
            //판매 물품 정보
            PurchasedProduct purchasedProduct = (PurchasedProduct) result.get(0)[0];
            //판매 물품 이미지
            List<PurchasedProductImage> productImageList = new ArrayList<>();
            result.forEach(arr -> {
                PurchasedProductImage purchasedProductImage = (PurchasedProductImage) arr[1];
                productImageList.add(purchasedProductImage);
            });
            //판매자
            Member member = (Member) result.get(0)[2];

            PurchasedProductDto purchasedProductDto = entitiesToDto(purchasedProduct, productImageList, member);
            //유저희망가격의 비용
            Integer expectedPrice = (int) (purchasedProduct.getPrice() * (1 - tokenAmountRatePerCost));
            //유저희망가격의 토큰 수량
            Long expectedPointAmount = (long) (purchasedProduct.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            //제안 가격의 비용
            Integer possiblePrice = (int) (purchasedProduct.getPossiblePrice() * (1 - tokenAmountRatePerCost));
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (purchasedProduct.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

            purchasedProductDto.setExpectedPrice(expectedPrice);
            purchasedProductDto.setExpectedPointAmount(expectedPointAmount);
            purchasedProductDto.setProposalPrice(possiblePrice);
            purchasedProductDto.setProposalTokenAmount(possiblePointAmount);

            return purchasedProductDto;
        } catch (IndexOutOfBoundsException e) {
            log.info(e.getMessage());
            throw new NotExistEntityException("IDX에 해당 하는 DB정보가 없습니다");
        }
    }

    @Override
    @Transactional
    public PurchasedProductDto register(Integer memberIdx, Integer categoryIdx, PurchasedProductDto purchasedProductDto) {
        Category category = categoryRepository.findById(categoryIdx)
                .orElseThrow(() -> new NotExistEntityException("해당하는 카테고리가 없습니다"));
        Member member = memberRepository.getMemberByIdxAndRemovedFalse(memberIdx)
                .orElseThrow(() -> new NotExistEntityException("해당하는 카테고리가 없습니다"));

        Map<String, Object> entityMap = dtoToEntities(member, category, purchasedProductDto);

        PurchasedProduct purchasedProduct = (PurchasedProduct) entityMap.get("purchasedProduct");
        purchasedProduct.setStep(PurchasedProductStep.RESERVATION);

        List<PurchasedProductImage> imgList = (List<PurchasedProductImage>) entityMap.get("imgList");

        purchasedProductRepository.save(purchasedProduct);

        imgList.forEach(img -> {
            purchasedProductImageRepository.save(img);
        });
        //유저희망가격의 비용
        Integer expectedPrice = (int) (purchasedProduct.getPrice() * (1 - tokenAmountRatePerCost));
        //유저희망가격의 토큰 수량
        Long expectedPointAmount = (long) (purchasedProduct.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

        //Response를 위한 데이터 입력
        purchasedProductDto.setIdx(purchasedProduct.getIdx());
        purchasedProductDto.setCatagory(category.getName());
        purchasedProductDto.setMemberEmail(member.getEmail());
        purchasedProductDto.setMemberName(member.getName());
        purchasedProductDto.setStep(purchasedProduct.getPurchasedProductStep().name());
        purchasedProductDto.setRegDate(purchasedProduct.getRegDate());
        purchasedProductDto.setModDate(purchasedProduct.getModDate());
        purchasedProductDto.setExpectedPrice(expectedPrice);
        purchasedProductDto.setExpectedPointAmount(expectedPointAmount);

        return purchasedProductDto;

    }

    @Override
    @Transactional
    public Long modify(Integer memberIdx, Integer categoryIdx, PurchasedProductDto purchasedProductDto) {
        PurchasedProduct purchasedProduct = purchasedProductRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, purchasedProductDto.getIdx())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        //PurchasedProduct 정보 변경
        purchasedProduct.changeCatagory(categoryIdx);
        purchasedProduct.changeBrand(purchasedProductDto.getBrand());
        purchasedProduct.changeName(purchasedProductDto.getName());
        purchasedProduct.changeQuentity(purchasedProductDto.getQuentity());
        purchasedProduct.changeState(purchasedProductDto.getState());
        purchasedProduct.changePrice(purchasedProductDto.getPrice());
        purchasedProduct.changeDetails(purchasedProductDto.getDetails());
        purchasedProduct.changeAddress(purchasedProductDto.getAddress());
        purchasedProductRepository.save(purchasedProduct);

        //PurchasedProduct의 변경할 이미지가 존재하면 기존 이미지 삭제후, 새로운 이미지 삽입
        List<ProductImageDto> imageDtoList = purchasedProductDto.getImageDtoList();
        if (imageDtoList != null && imageDtoList.size() > 0) {
            purchasedProductImageRepository.deleteAllByPurchasedProductIdx(purchasedProduct.getIdx());

            List<PurchasedProductImage> purchasedProductImageList = imageDtoList.stream().map(imgDto -> PurchasedProductImage.builder()
                    .path(imgDto.getPath())
                    .imgName(imgDto.getImgName())
                    .uuid(imgDto.getUuid())
                    .purchasedProduct(purchasedProduct)
                    .build()
            ).collect(Collectors.toList());

            purchasedProductImageList.forEach(img -> {
                purchasedProductImageRepository.save(img);
            });
        }
        return purchasedProduct.getIdx();
    }

    @Override
    public void remove(Integer memberIdx, Long idx) {
        PurchasedProduct purchasedProduct = purchasedProductRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        purchasedProduct.changeRemoved(true);
        purchasedProductRepository.save(purchasedProduct);
    }

    @Override
    public Map<String, Object> modifyStep(Long purchasedProductIdx, Set<String> roleSet, Integer cost) {
        PurchasedProduct purchasedProduct = purchasedProductRepository.getPurchasedProductByIdxAndCanceledFalse(purchasedProductIdx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        Map<String, Object> map = new HashMap<>();

        if (purchasedProduct.getPurchasedProductStep().ordinal() == PurchasedProductStep.RESERVATION.ordinal()) { //PROPOSAL steop으로 이동한다

            if (!roleSet.contains(Role.ROLE_MANAGER.name())) {
                throw new WrongStepException("권한이 없습니다");
            } else if (cost == null) {
                throw new WrongStepException("Cost is NULL");
            }
            //제안할 가격을 저장한다
            purchasedProduct.setStep(PurchasedProductStep.PROPOSAL);
            purchasedProduct.setPossiblePrice(cost);
            //제안 가격의 비용
            Integer possiblePrice = (int) (cost * (1 - tokenAmountRatePerCost));
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (cost * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            map.put("proposalPrice", possiblePrice);
            map.put("proposalTokenAmount", possiblePointAmount);

            //송금할 가격을 저장하고 포인트를 저장한다
        } else if (purchasedProduct.getPurchasedProductStep().ordinal() == PurchasedProductStep.PROPOSAL.ordinal()) { //ACCEPTANCE step으로 이동한다

            if (!roleSet.contains(Role.ROLE_USER.name())) {
                throw new WrongStepException("권한이 없습니다");
            }
            purchasedProduct.setStep(PurchasedProductStep.ACCEPTANCE);
            //제안 가격의 비용
            Integer possiblePrice = (int) (purchasedProduct.getPossiblePrice() * (1 - tokenAmountRatePerCost));
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (purchasedProduct.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

            try {
                contractService.transfer(purchasedProduct.getMember().getWallet().getAddress(), possiblePointAmount * 1000L);
            } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | TransactionException e) {
                log.debug(e.getMessage());
                throw new TokenTransactionException("토큰 트랜젝션 에러 발생");
            }

            purchasedProduct.setAcceptedPrice(possiblePrice);
            purchasedProduct.setAcceptedTokenAmount(possiblePointAmount);
            map.put("proposalPrice", possiblePrice);
            map.put("proposalTokenAmount", possiblePointAmount);

            //배송이 끝난 후 Finish상태가 된다
        } else if (purchasedProduct.getPurchasedProductStep().ordinal() == PurchasedProductStep.ACCEPTANCE.ordinal()) {

            if (!roleSet.contains(Role.ROLE_MANAGER.name())) {
                throw new WrongStepException("권한이 없습니다");
            }
            purchasedProduct.setStep(PurchasedProductStep.FINISH);
        } else {
            throw new WrongStepException("FINISH 상태인지 확인");
        }

        purchasedProductRepository.save(purchasedProduct);
        map.put("step", purchasedProduct.getPurchasedProductStep().name());
        return map;
    }
}
