package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.Address;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerSessionCreateParams;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.sun.mail.smtp.SMTPTransport;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.mapper.PaymentMapper;
import com.utar.uhauction.model.dto.CreateItemDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.model.vo.FundMonthVO;
import com.utar.uhauction.model.vo.ItemVO;
import com.utar.uhauction.model.vo.TopContributorVO;
import com.utar.uhauction.model.vo.TrendCategoryVO;
import com.utar.uhauction.service.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.validation.Valid;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private EmailService emailService;

    @Resource
    PaymentMapper paymentMapper;

    @Resource
    IFundService iFundService;

    @Resource
    private IItemService iItemService;
    @Resource
    private IUmsUserService umsUserService;

    @GetMapping("/trend")
    public ApiResult<List<TrendCategoryVO>> trendCategory() {
        List<TrendCategoryVO> list = iItemService.trendCategory();
        return ApiResult.success(list);
    }

    @GetMapping("/topDonor")
    public ApiResult<List<TopContributorVO>> getTopDonor() {
        List<TopContributorVO> list = iItemService.selectTopDonor();
        return ApiResult.success(list);
    }

    @GetMapping("/itemMonth")
    public ApiResult<List<FundMonthVO>> getItemByMonth() {
        List<FundMonthVO> itemMonth = iItemService.selectItemByMonth();
        return ApiResult.success(itemMonth);
    }

    @GetMapping("/fundMonth")
    public ApiResult<List<FundMonthVO>> getFundByMonth() {
        List<FundMonthVO> fundMonthVOS = iItemService.selectFundByMonth();
        return ApiResult.success(fundMonthVOS);
    }

    @GetMapping("/topBidder")
    public ApiResult<List<TopContributorVO>> getTopBidder() {
        List<TopContributorVO> list = iItemService.selectTopBidder();
        return ApiResult.success(list);
    }

    @GetMapping("/list")
    public ApiResult<Page<ItemVO>> list(@RequestParam(value = "tab", defaultValue = "ongoing") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<ItemVO> list = iItemService.getList(new Page<>(pageNo, pageSize), tab);

        return ApiResult.success(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<Item> create(@RequestHeader(value = USER_NAME) String userName
            , @RequestBody CreateItemDTO dto) {
        User user = umsUserService.getUserByUsername(userName);
        Item item = iItemService.create(dto, user);
        return ApiResult.success(item);
    }

    @GetMapping()
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = iItemService.viewTopic(id);
        return ApiResult.success(map);
    }

    @GetMapping("/deliver")
    public ApiResult deliver(@RequestParam("id") String id) {
        Item byId = iItemService.getById(id);
        byId.setIsPay(2);
        iItemService.updateById(byId);
        return ApiResult.success();
    }

    @GetMapping("/receive")
    public ApiResult receive(@RequestParam("id") String id) {
        Item byId = iItemService.getById(id);
        byId.setIsPay(3);
        iItemService.updateById(byId);
        return ApiResult.success();
    }


    @PostMapping("/update")
    public ApiResult<Item> update(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody Item item) {
        User user = umsUserService.getUserByUsername(userName);
        //Assert.isTrue(user.getId().equals(item.getDonorId()), "Only author can edit");
        item.setModifyTime(new Date());
        iItemService.updateById(item);
        return ApiResult.success(item);
    }

    @PostMapping("/admin/update")
    public ApiResult<Item> adminUpdate(@RequestBody Item item) {
        item.setModifyTime(new Date());
        iItemService.updateById(item);
        return ApiResult.success(item);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        User user = umsUserService.getUserByUsername(userName);
        Item byId = iItemService.getById(id);
        Assert.notNull(byId, "The item does not exist");
        Assert.isTrue(byId.getDonorId().equals(user.getId()), "Why you can delete other people's item");
        iItemService.removeById(id);
        return ApiResult.success(null, "Delete success");
    }

    @GetMapping("/all")
    public ApiResult<List<Item>> allItem() {
        return ApiResult.success(iItemService.list());
    }

    @GetMapping("/admin/delete")
    public ApiResult<String> deleteUser(@RequestParam String id) {
        iItemService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @GetMapping("/images")
    public ApiResult<List<Images>> getImagesByItemId(@RequestParam String id) {
        List<Images> imagesByItemId = iItemService.getImagesByItemId(id);
        return ApiResult.success(imagesByItemId);
    }

    @GetMapping("/image/delete")
    public ApiResult<String> deleteComment(@RequestParam String id) {
        iItemService.removeImageById(id);
        return ApiResult.success("Delete successfully");
    }


    @GetMapping("/fund/all")
    public ApiResult<List<Fund>> allFund() {
        return ApiResult.success(iFundService.list());
    }

    @GetMapping("/fund/delete")
    public ApiResult<String> deleteFund(@RequestParam String id) {
        iFundService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @GetMapping("/recommend")
    public ApiResult<List<Item>> getRecommend(@RequestParam("itemId") String id) {
        List<Item> items = iItemService.getRecommend(id);
        return ApiResult.success(items);
    }

    @GetMapping("/pay/success")
    public ApiResult paySuccess(@RequestParam("sessionId") String sessionId, @RequestParam("itemId") String itemId) throws Exception {
        Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
        Session session = Session.retrieve(sessionId);
        Item item = iItemService.getById(itemId);

        if ("paid".equals(session.getPaymentStatus())) {

            item.setIsPay(1);

            User user = umsUserService.getById(item.getWinnerId());
            String toEmailAddress = user.getEmail();
            String subject = "Payment Successful!";
            String text = "Dear " + user.getAlias() + ",<br><br>Your payment for the item: " + item.getTitle()
                    + " has been successfully processed.<br><br>Thank you for your prompt payment!<br><br>Best regards,<br>Your Auction Team";
            String imagePath = "http://localhost:9000/uhauction/item/img/" + item.getCover();
            emailService.sendEmailWithImage(toEmailAddress, subject, text, imagePath);

            ShippingDetails shippingDetails = session.getShippingDetails();
            Address address = shippingDetails.getAddress();
            String fullAddress = address.getLine1() + address.getLine2() + " " + address.getPostalCode() + " " + address.getState();
            item.setAddress(fullAddress);

            iItemService.updateById(item);
        }

        return ApiResult.success("success");
    }

    @PostMapping("/sendMail")
    public ApiResult<String> sendMail(@RequestBody Item item) {

        try {
            User user = umsUserService.getById(item.getWinnerId());
            String toEmailAddress = user.getEmail();
            String subject = "Congratulations! You are the winner";
            String text = "Dear " + user.getAlias() + ",<br><br>Congratulations! You have won the auction for the item: "
                    + item.getTitle() + ".<br><br>Best regards,<br>Your Auction Team";
            String imagePath = "http://localhost:9000/uhauction/item/img/" + item.getCover();
            emailService.sendEmailWithImage(toEmailAddress, subject, text, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        item.setIsNotify(1);
        iItemService.updateById(item);

        return ApiResult.success("");
    }

    private static String base64Encode(String value) {
        return javax.xml.bind.DatatypeConverter.printBase64Binary(value.getBytes());
    }

    @PostMapping("/fund/update")
    public ApiResult<String> updateFund(@RequestBody Fund fund) {
        iFundService.updateById(fund);
        return ApiResult.success("Update Successfully");
    }

    @PostMapping("/fund/add")
    public ApiResult<String> addFund(@RequestBody Fund fund) {
        iFundService.save(fund);
        return ApiResult.success("Add successfully");
    }

    @PostMapping("/pay/add")
    public ApiResult<String> addPay(@RequestBody Payment payment) {
        payment.setDate(new Date());
        paymentMapper.insert(payment);
        return ApiResult.success("Payment success");
    }

    @PostMapping("/setEnd")
    public ApiResult setEnd(@RequestBody Item item) throws StripeException {
        if (item.getIsEnd() == 1) {
            return ApiResult.success(null, "Already set");
        } else {
            item.setIsEnd(1);
            Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
            ProductCreateParams productParams = ProductCreateParams.builder()
                                                .setName(item.getTitle())
                                                .build();

            Product product = Product.create(productParams);
            PriceCreateParams priceParams = PriceCreateParams.builder()
                                            .setCurrency("myr")
                                            .setUnitAmount((item.getHighestBid() * 100))
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
                            .setSuccessUrl("http://localhost:8080/#/pay/success?session_id={CHECKOUT_SESSION_ID}&item_id=" + item.getId())
                            .setCancelUrl("http://localhost:8080/#/pay/success?session_id={CHECKOUT_SESSION_ID}&item_id=" + item.getId())
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
            item.setPayLink(session.getUrl());
            iItemService.updateById(item);

            return ApiResult.success("Set Expire Success");
        }

    }
}
