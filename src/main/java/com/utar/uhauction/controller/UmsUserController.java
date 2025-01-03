package com.utar.uhauction.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.dto.LoginDTO;
import com.utar.uhauction.model.dto.RegisterDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController {

    @Resource
    private IUmsUserService iUmsUserService;
    @Resource
    private IItemService iItemService;

    @Autowired
    private com.utar.uhauction.service.IBidService IBidService;


    @GetMapping("/username")
    public ApiResult<String> getusernameByID(@RequestParam(value = "userid", defaultValue = "") String userid) {
        User user = iUmsUserService.getById(userid);
        return ApiResult.success(user.getAlias());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = iUmsUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("Register Failed");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        String token = iUmsUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("Wrong password");
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "Login successfully");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<User> getUser(@RequestHeader(value = USER_NAME) String userName) {
        User user = iUmsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        return ApiResult.success(null, "Log out success");
    }


    @GetMapping()
    public ApiResult<List<User>> allUser(){
        //List<User> users = iUmsUserService.allUsers();
        //Map<String, Object> map = new HashMap<>(16);
        //map.put("users",users);
        return ApiResult.success(iUmsUserService.allUsers());
    }


    @GetMapping("/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User user = iUmsUserService.getUserByUsername(username);
        Assert.notNull(user, "User does not exist");

        // Handle donated items
        Page<Item> donatedPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>().eq(Item::getDonorId, user.getId()));


        Page<Item> deliverPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getDonorId, user.getId())
                        .eq(Item::getIsPay, 1));

        Page<Item> pendingPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getDonorId, user.getId())
                        .eq(Item::getIsPay, 0));

        Page<Item> completedPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getDonorId, user.getId())
                        .in(Item::getIsPay, 2, 3));


        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        map.put("items", donatedPage);
        map.put("delivers",deliverPage);
        map.put("pendings",pendingPage);
        map.put("completes",completedPage);
        return ApiResult.success(map);
    }


    @GetMapping("/bid/{username}")
    public ApiResult<Map<String, Object>> getBidPage(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User user = iUmsUserService.getUserByUsername(username);
        Assert.notNull(user, "User does not exist");

        LocalDate today = LocalDate.now();

        // Handle bids
        QueryWrapper<Bid> wrapperBid = new QueryWrapper<>();
        wrapperBid.lambda().eq(Bid::getUserId, user.getId());
        List<Bid> bids = IBidService.list(wrapperBid);

        Page<Item> bidPage = new Page<>(pageNo, size);
        Page<Item> lostPage = new Page<>(pageNo, size);
        Page<Item> pendingPage = new Page<>(pageNo, size);
        if (!bids.isEmpty()) {
            Set<String> itemSet = bids.stream().map(Bid::getItemId).collect(Collectors.toSet());

            bidPage = iItemService.page(new Page<>(pageNo, size),
                    new LambdaQueryWrapper<Item>()
                            .in(Item::getId, itemSet));


            lostPage = iItemService.page(new Page<>(pageNo, size),
                    new LambdaQueryWrapper<Item>()
                            .in(Item::getId, itemSet)
                            .ne(Item::getWinnerId, user.getId())
                            .le(Item::getEndTime, today));


            pendingPage = iItemService.page(new Page<>(pageNo, size),
                    new LambdaQueryWrapper<Item>()
                            .in(Item::getId, itemSet)
                            .ge(Item::getEndTime, today));


        } else {
            // Return an empty page
            bidPage.setRecords(Collections.emptyList());
            bidPage.setTotal(0);

            lostPage.setRecords(Collections.emptyList());
            lostPage.setTotal(0);

            pendingPage.setRecords(Collections.emptyList());
            pendingPage.setTotal(0);
        }


        // Handle won items
        Page<Item> wonPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getWinnerId, user.getId())
                        .le(Item::getEndTime, today));

        Page<Item> receivePage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getWinnerId, user.getId())
                        .le(Item::getEndTime, today)
                        .in(Item::getIsPay, 1, 2));

        Page<Item> completedPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getWinnerId, user.getId())
                        .le(Item::getEndTime, today)
                        .eq(Item::getIsPay, 3));

        Page<Item> payPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getWinnerId, user.getId())
                        .le(Item::getEndTime, today)
                        .eq(Item::getIsPay, 0));

        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        map.put("bidPage", bidPage);
        map.put("wonPage", wonPage);
        map.put("lostPage",lostPage);
        map.put("pendingPage",pendingPage);
        map.put("receivePage",receivePage);
        map.put("completedPage",completedPage);
        map.put("payPage",payPage);
        return ApiResult.success(map);
    }


    @PostMapping("/update")
    public ApiResult<User> updateUser(@RequestBody User user) {
        iUmsUserService.updateById(user);
        return ApiResult.success(user);
    }

    @GetMapping("/topup/success")
    public ApiResult<String> topupSuccess(@RequestParam("amount") long amount, @RequestParam("userId") String userId) throws Exception {
        User user = iUmsUserService.getById(userId);
        user.setBalance(user.getBalance() + amount);
        iUmsUserService.updateById(user);
        return ApiResult.success("success");
    }

    @GetMapping("/topup")
    public ApiResult<String> topup(@RequestParam("userId") String userId,@RequestParam("amount") long amount) throws StripeException {
        System.out.println("Top Up Function: " + userId +" " + amount);
        User user = iUmsUserService.getById(userId);
        Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(user.getAlias() + " Top Up")
                .build();
        Product product = Product.create(productParams);
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency("myr")
                .setUnitAmount(amount*100)
                .setProduct(product.getId())
                .build();


        Price price = Price.create(priceParams);
        SessionCreateParams.ShippingAddressCollection shippingAddressCollection = SessionCreateParams.ShippingAddressCollection.builder().addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.MY).build();
        Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(price.getId())
                        .build())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.GRABPAY)
                .setCurrency("myr")
                .setShippingAddressCollection(shippingAddressCollection)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/#/topup/success?amount="+amount+"&userId=" + user.getId())
                .setCancelUrl("http://localhost:8080/#/pay/cancel")
                .build();
        Session session = Session.create(params);

        PaymentLinkCreateParams linkParams = PaymentLinkCreateParams.builder()
                .addLineItem(PaymentLinkCreateParams
                        .LineItem.builder()
                        .setPrice(price.getId())
                        .setQuantity(1L).build())
                .setRestrictions(
                        PaymentLinkCreateParams.Restrictions.builder()
                                .setCompletedSessions(
                                        PaymentLinkCreateParams.Restrictions
                                                .CompletedSessions.builder()
                                                .setLimit(1L).build())
                                .build())
                .setInactiveMessage("Sorry, you already paid!")
                .build();

        PaymentLink paymentLink = PaymentLink.create(linkParams);
        System.out.println(session.getUrl());
        return ApiResult.success(session.getUrl());
    }


    @GetMapping("/delete")
    public ApiResult<String> deleteUser(@RequestParam String id){
        iUmsUserService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

//    @GetMapping("/resetPassword")
//    public ApiResult<> resetPassword(String userName){
//
//    }

}
