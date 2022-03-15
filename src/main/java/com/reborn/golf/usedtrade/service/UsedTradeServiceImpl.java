package com.reborn.golf.usedtrade.service;

import com.reborn.golf.category.entity.Category;
import com.reborn.golf.category.repository.CategoryRepository;
import com.reborn.golf.common.api.CoinExchange;
import com.reborn.golf.common.api.ContractService;
import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.common.exception.AlreadyFinishedException;
import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.common.exception.TokenTransactionException;
import com.reborn.golf.common.exception.WrongStepException;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.member.entity.Role;
import com.reborn.golf.member.repository.MemberRepository;
import com.reborn.golf.product.dto.ProductImageDto;
import com.reborn.golf.usedtrade.dto.UsedTradeDto;
import com.reborn.golf.usedtrade.entity.UsedTrade;
import com.reborn.golf.usedtrade.entity.UsedTradeImage;
import com.reborn.golf.usedtrade.entity.UsedTradeStep;
import com.reborn.golf.usedtrade.repository.ImageRepository;
import com.reborn.golf.usedtrade.repository.UsedTradeRepository;
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
public class UsedTradeServiceImpl implements UsedTradeService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final UsedTradeRepository usedTradeRepository;
    private final ImageRepository imageRepository;
    private final CoinExchange coinExchange;
    private final Double tokenAmountRatePerCost = 0.1;
    private final ContractService contractService;

    @Override
    @Transactional
    public PageResultDto<Object[], UsedTradeDto> getListWithUser(Integer memberIdx, PageRequestDto requestDto) {
        Page<Object[]> result = usedTradeRepository.getPurchasedItemsbyMemberIdx(memberIdx, requestDto.getPageable(Sort.by("regDate").descending()));

//        Function<Object[], PurchasedProductDto> fn = (arr -> entitiesToDto((PurchasedProduct) arr[0], List.of((PurchasedProductImage) arr[1]), (String) arr[2]));
        Function<Object[], UsedTradeDto> fn = (arr -> {
            UsedTrade items = (UsedTrade) arr[0];
            List<UsedTradeImage> usedTradeImageList = List.of((UsedTradeImage) arr[1]);
            String categoryName = (String) arr[2];
            //이미지 처리
            List<ProductImageDto> productImageDtoList = usedTradeImageList.stream().map(productImage ->
                    ProductImageDto.builder()
                            .imgName(productImage.getImgName())
                            .path(productImage.getPath())
                            .uuid(productImage.getUuid())
                            .build()).collect(Collectors.toList());
            //유저희망가격의 비용
            Integer expectedPrice = items.getPrice();
            //유저희망가격의 토큰 수량
            Long expectedPointAmount = (long) (items.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            //제안 가격의 비용
            Integer possiblePrice = items.getPossiblePrice();
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (items.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());


            return UsedTradeDto.builder()
                    .idx(items.getIdx())
                    .catagory(categoryName)
                    .brand(items.getBrand())
                    .name(items.getName())
                    .state(items.getState())
                    .price(items.getPrice())
                    .quantity(items.getQuantity())
                    .address(items.getAddress())
                    .details(items.getDetails())
                    .expectedPrice(expectedPrice)
                    .expectedPointAmount(expectedPointAmount)
                    .proposalPrice(possiblePrice)
                    .proposalTokenAmount(possiblePointAmount)
                    .acceptedPrice(items.getAcceptedPrice())
                    .acceptedTokenPrice(items.getAcceptedTokenPrice())
                    .acceptedTokenAmount(items.getAcceptedTokenAmount())
                    .canceled(items.isCanceled())
                    .step(items.getUsedTradeStep().name())
                    .imageDtoList(productImageDtoList)
                    .regDate(items.getRegDate())
                    .modDate(items.getModDate())
                    .build();
        });

        return new PageResultDto<>(result, fn);
    }

    @Override
    public UsedTradeDto read(Long idx) {
        try {
            List<Object[]> result = usedTradeRepository.getItembyIdxWithImage(idx);
            //판매 물품 정보
            UsedTrade usedTrade = (UsedTrade) result.get(0)[0];
            //판매 물품 이미지
            List<UsedTradeImage> productImageList = new ArrayList<>();
            result.forEach(arr -> {
                UsedTradeImage usedTradeImage = (UsedTradeImage) arr[1];
                productImageList.add(usedTradeImage);
            });
            //판매자
            Member member = (Member) result.get(0)[2];

            UsedTradeDto usedTradeDto = entitiesToDto(usedTrade, productImageList, member);
            //유저희망가격의 비용
            Integer expectedPrice = usedTrade.getPrice();
            //유저희망가격의 토큰 수량
            Long expectedPointAmount = (long) (usedTrade.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            //제안 가격의 비용
            Integer possiblePrice = usedTrade.getPossiblePrice();
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (usedTrade.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

            usedTradeDto.setExpectedPrice(expectedPrice);
            usedTradeDto.setExpectedPointAmount(expectedPointAmount);
            usedTradeDto.setProposalPrice(possiblePrice);
            usedTradeDto.setProposalTokenAmount(possiblePointAmount);

            return usedTradeDto;
        } catch (IndexOutOfBoundsException e) {
            log.info(e.getMessage());
            throw new NotExistEntityException("IDX에 해당 하는 DB정보가 없습니다");
        }
    }

    @Override
    @Transactional
    public UsedTradeDto register(Integer memberIdx, Integer categoryIdx, UsedTradeDto usedTradeDto) {
        Category category = categoryRepository.findById(categoryIdx)
                .orElseThrow(() -> new NotExistEntityException("해당하는 카테고리가 없습니다"));
        Member member = memberRepository.getByIdxAndRemovedFalse(memberIdx)
                .orElseThrow(() -> new NotExistEntityException("해당하는 카테고리가 없습니다"));

        Map<String, Object> entityMap = dtoToEntities(member, category, usedTradeDto);

        UsedTrade usedTrade = (UsedTrade) entityMap.get("purchasedProduct");
        usedTrade.setStep(UsedTradeStep.RESERVATION);

        List<UsedTradeImage> imgList = (List<UsedTradeImage>) entityMap.get("imgList");

        usedTradeRepository.save(usedTrade);

        imgList.forEach(img -> {
            imageRepository.save(img);
        });
        //유저희망가격의 비용
        Integer expectedPrice = usedTrade.getPrice();
        //유저희망가격의 토큰 수량
        Long expectedPointAmount = (long) (usedTrade.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

        //Response를 위한 데이터 입력
        usedTradeDto.setIdx(usedTrade.getIdx());
        usedTradeDto.setCatagory(category.getName());
        usedTradeDto.setMemberEmail(member.getEmail());
        usedTradeDto.setMemberName(member.getName());
        usedTradeDto.setStep(usedTrade.getUsedTradeStep().name());
        usedTradeDto.setRegDate(usedTrade.getRegDate());
        usedTradeDto.setModDate(usedTrade.getModDate());
        usedTradeDto.setExpectedPrice(expectedPrice);
        usedTradeDto.setExpectedPointAmount(expectedPointAmount);

        return usedTradeDto;

    }

    @Override
    @Transactional
    public Long modify(Integer memberIdx, Integer categoryIdx, UsedTradeDto usedTradeDto) {
        UsedTrade usedTrade = usedTradeRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, usedTradeDto.getIdx())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        //PurchasedProduct 정보 변경
        usedTrade.changeCatagory(categoryIdx);
        usedTrade.changeBrand(usedTradeDto.getBrand());
        usedTrade.changeName(usedTradeDto.getName());
        usedTrade.changeQuantity(usedTradeDto.getQuantity());
        usedTrade.changeState(usedTradeDto.getState());
        usedTrade.changePrice(usedTradeDto.getPrice());
        usedTrade.changeDetails(usedTradeDto.getDetails());
        usedTrade.changeAddress(usedTradeDto.getAddress());
        usedTrade.setStep(UsedTradeStep.PROPOSAL);

        //PurchasedProduct의 변경할 이미지가 존재하면 기존 이미지 삭제후, 새로운 이미지 삽입
        List<ProductImageDto> imageDtoList = usedTradeDto.getImageDtoList();
        if (imageDtoList != null && imageDtoList.size() > 0) {
            imageRepository.deleteAllByUsedTradeIdx(usedTrade.getIdx());

            List<UsedTradeImage> usedTradeImageList = imageDtoList.stream().map(imgDto -> UsedTradeImage.builder()
                    .path(imgDto.getPath())
                    .imgName(imgDto.getImgName())
                    .uuid(imgDto.getUuid())
                    .usedTrade(usedTrade)
                    .build()
            ).collect(Collectors.toList());

            usedTradeImageList.forEach(img -> {
                imageRepository.save(img);
            });
        }
        return usedTrade.getIdx();
    }

    @Override
    @Transactional
    public void remove(Integer memberIdx, Long idx) {
        UsedTrade usedTrade = usedTradeRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        if (usedTrade.getUsedTradeStep().ordinal() == UsedTradeStep.FINISH.ordinal()) {
            throw new AlreadyFinishedException("이미 모든 거래가 끝났습니다");
        }

        usedTrade.setStep(UsedTradeStep.CANCELED);

        if (usedTrade.getUsedTradeStep().ordinal() == UsedTradeStep.ACCEPTANCE.ordinal()) {
            try {
                contractService.transferFrom(usedTrade.getMember().getWallet().getAddress(), usedTrade.getAcceptedTokenAmount() * 1000L);
            } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException
                    | IllegalAccessException | TransactionException e) {
                log.debug(e.getMessage());
                throw new TokenTransactionException("토큰 트랜젝션 에러 발생");
            }
        }

        usedTrade.changeRemoved(true);
    }

    @Override
    @Transactional
    public Map<String, Object> modifyStep(Long purchasedProductIdx, Set<String> roleSet, Integer cost) {
        UsedTrade usedTrade = usedTradeRepository.getPurchasedProductByIdxAndCanceledFalse(purchasedProductIdx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 DB정보가 없습니다"));

        Map<String, Object> map = new HashMap<>();

        if (usedTrade.getUsedTradeStep().ordinal() == UsedTradeStep.RESERVATION.ordinal()) { //PROPOSAL steop으로 이동한다

            if (!roleSet.contains(Role.ROLE_MANAGER.name())) {
                throw new WrongStepException("권한이 없습니다");
            } else if (cost == null) {
                throw new WrongStepException("Cost is NULL");
            }
            //제안할 가격을 저장한다
            usedTrade.setStep(UsedTradeStep.PROPOSAL);
            usedTrade.setPossiblePrice(cost);
            //제안 가격의 비용
            Integer possiblePrice = (int) (cost * (1 - tokenAmountRatePerCost));
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (cost * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            map.put("proposalPrice", possiblePrice);
            map.put("proposalTokenAmount", possiblePointAmount);

            //송금할 가격을 저장하고 포인트를 저장한다
        } else if (usedTrade.getUsedTradeStep().ordinal() == UsedTradeStep.PROPOSAL.ordinal()) { //ACCEPTANCE step으로 이동한다

            if (!roleSet.contains(Role.ROLE_USER.name())) {
                throw new WrongStepException("권한이 없습니다");
            }
            usedTrade.setStep(UsedTradeStep.ACCEPTANCE);
            //제안 가격의 비용
            Integer possiblePrice = usedTrade.getPossiblePrice();
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (usedTrade.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());

            try {
                contractService.transfer(usedTrade.getMember().getWallet().getAddress(), possiblePointAmount * 1000L);
            } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | TransactionException e) {
                log.debug(e.getMessage());
                throw new TokenTransactionException("토큰 트랜젝션 에러 발생");
            }

            usedTrade.setAcceptedPrice(possiblePrice);
            usedTrade.setAcceptedTokenAmount(possiblePointAmount);
            usedTrade.setAcceptedTokenPrice(possiblePrice.toString());
            map.put("proposalPrice", possiblePrice);
            map.put("proposalTokenAmount", possiblePointAmount);

            //배송이 끝난 후 Finish상태가 된다
        } else if (usedTrade.getUsedTradeStep().ordinal() == UsedTradeStep.ACCEPTANCE.ordinal()) {

            if (!roleSet.contains(Role.ROLE_MANAGER.name())) {
                throw new WrongStepException("권한이 없습니다");
            }
            usedTrade.setStep(UsedTradeStep.FINISH);
        } else {
            throw new WrongStepException("FINISH 상태인지 확인");
        }

        map.put("step", usedTrade.getUsedTradeStep().name());
        return map;
    }

    @Override
    public PageResultDto<Object[], UsedTradeDto> getList(PageRequestDto requestDto) {
        Page<Object[]> result = usedTradeRepository.getPurchasedProductByIdx(requestDto.getPageable(Sort.by("regDate").descending()));

//        Function<Object[], PurchasedProductDto> fn = (arr -> entitiesToDto((PurchasedProduct) arr[0], List.of((PurchasedProductImage) arr[1]), (String) arr[2]));
        Function<Object[], UsedTradeDto> fn = (arr -> {
            UsedTrade items = (UsedTrade) arr[0];
            List<UsedTradeImage> usedTradeImageList = List.of((UsedTradeImage) arr[1]);
            Member member = (Member) arr[2];
            String categoryName = (String) arr[3];
            //이미지 처리
            List<ProductImageDto> productImageDtoList = usedTradeImageList.stream().map(productImage ->
                    ProductImageDto.builder()
                            .imgName(productImage.getImgName())
                            .path(productImage.getPath())
                            .uuid(productImage.getUuid())
                            .build()).collect(Collectors.toList());

            //유저희망가격의 비용
            Integer expectedPrice = items.getPrice();
            //유저희망가격의 토큰 수량
            Long expectedPointAmount = (long) (items.getPrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());
            //제안 가격의 비용
            Integer possiblePrice = items.getPossiblePrice();
            //제안 가격의 토큰 수량
            Long possiblePointAmount = (long) (items.getPossiblePrice() * tokenAmountRatePerCost / coinExchange.getTokenPrice());


            return UsedTradeDto.builder()
                    .idx(items.getIdx())
                    .catagory(categoryName)
                    .brand(items.getBrand())
                    .name(items.getName())
                    .state(items.getState())
                    .price(items.getPrice())
                    .quantity(items.getQuantity())
                    .address(items.getAddress())
                    .details(items.getDetails())
                    .memberEmail(member.getEmail())
                    .memberName(member.getName())
                    .expectedPrice(expectedPrice)
                    .expectedPointAmount(expectedPointAmount)
                    .proposalPrice(possiblePrice)
                    .proposalTokenAmount(possiblePointAmount)
                    .canceled(items.isCanceled())
                    .step(items.getUsedTradeStep().name())
                    .imageDtoList(productImageDtoList)
                    .regDate(items.getRegDate())
                    .modDate(items.getModDate())
                    .build();
        });

        return new PageResultDto<>(result, fn);
    }
}
