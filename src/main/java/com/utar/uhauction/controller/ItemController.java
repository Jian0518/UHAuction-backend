package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerSessionCreateParams;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.mapper.PaymentMapper;
import com.utar.uhauction.model.dto.CreateItemDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.model.vo.ItemVO;
import com.utar.uhauction.service.IFundService;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IPaymentService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Resource
    PaymentMapper paymentMapper;

    @Resource
    IFundService iFundService;

    @Resource
    private IItemService iItemService;
    @Resource
    private IUmsUserService umsUserService;

    @GetMapping("/list")
    public ApiResult<Page<ItemVO>> list(@RequestParam(value = "tab", defaultValue = "ongoing") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
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
    public ApiResult deliver(@RequestParam("id") String id){
        Item byId = iItemService.getById(id);
        byId.setIsPay(2);
        iItemService.updateById(byId);
        return ApiResult.success();
    }

    @GetMapping("/receive")
    public ApiResult receive(@RequestParam("id") String id){
        Item byId = iItemService.getById(id);
        byId.setIsPay(3);
        iItemService.updateById(byId);
        return ApiResult.success();
    }



    @PostMapping("/update")
    public ApiResult<Item> update(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody Item item) {
        User user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getId().equals(item.getDonorId()), "Only author can edit");
        item.setModifyTime(new Date());
        iItemService.updateById(item);
        return ApiResult.success(item);
    }

    @PostMapping("/admin/update")
    public ApiResult<Item> adminUpdate( @RequestBody Item item) {
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
        return ApiResult.success(null,"Delete success");
    }

    @GetMapping("/all")
    public ApiResult<List<Item>> allItem(){
        return ApiResult.success(iItemService.list());
    }

    @GetMapping("/admin/delete")
    public ApiResult<String> deleteUser(@RequestParam String id){
        iItemService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @GetMapping("/images")
    public ApiResult<List<Images>> getImagesByItemId(@RequestParam String id){
        List<Images> imagesByItemId = iItemService.getImagesByItemId(id);
        return ApiResult.success(imagesByItemId);
    }

    @GetMapping("/image/delete")
    public ApiResult<String> deleteComment(@RequestParam String id){
        iItemService.removeImageById(id);
        return ApiResult.success("Delete successfully");
    }


    @GetMapping("/fund/all")
    public ApiResult<List<Fund>> allFund(){
        return ApiResult.success(iFundService.list());
    }

    @GetMapping("/fund/delete")
    public ApiResult<String> deleteFund(@RequestParam String id){
        iFundService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @GetMapping("/recommend")
    public ApiResult<List<Item>> getRecommend(@RequestParam("itemId") String id) {
        List<Item> items = iItemService.getRecommend(id);
        return ApiResult.success(items);
    }

    @GetMapping("/pay/success")
    public ApiResult paySuccess(@RequestParam("sessionId") String sessionId, @RequestParam("itemId") String itemId) throws StripeException {
        Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
        Session session = Session.retrieve(sessionId);
        Item item = iItemService.getById(itemId);
        System.out.println(session.getPaymentStatus());
        if("paid".equals(session.getPaymentStatus())){
            System.out.println("OK");
            item.setIsPay(1);





            ShippingDetails shippingDetails = session.getShippingDetails();
            Address address = shippingDetails.getAddress();
            String fullAddress = address.getLine1() + address.getLine2() + " " + address.getPostalCode() + " " + address.getState();
            item.setAddress(fullAddress);


            iItemService.updateById(item);
        }

        return ApiResult.success("success");
    }

    @PostMapping("/fund/update")
    public ApiResult<String > updateFund(@RequestBody Fund fund) {
        iFundService.updateById(fund);
        return ApiResult.success("Update Successfully");
    }

    @PostMapping("/fund/add")
    public ApiResult<String> addFund(@RequestBody Fund fund) {
        System.out.println("Add fund");
        iFundService.save(fund);
        return ApiResult.success("Add successfully");
    }

    @PostMapping("/pay/add")
    public ApiResult<String> addPay(@RequestBody Payment payment){
        payment.setDate(new Date());
        paymentMapper.insert(payment);
        return ApiResult.success("Payment success");
    }

    @PostMapping("/setEnd")
    public ApiResult setEnd(@RequestBody Item item) throws StripeException {
        System.out.println("hi");
        if(item.getIsEnd()==1){
            System.out.println("Already set end");
            return ApiResult.success(null,"Already set");
        }
        else{

            item.setIsEnd(1);
            Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
            ProductCreateParams productParams =
                    ProductCreateParams.builder().
                            setName(item.getTitle()).
                            build();

            Product product = Product.create(productParams);
            System.out.println(product.getId());
            PriceCreateParams priceParams =
                    PriceCreateParams.builder()
                            .setCurrency("myr")
                            .setUnitAmount((item.getHighestBid()*100))
                            .setProduct(product.getId())
                            .build();

            Price price = Price.create(priceParams);
            System.out.println(price.getId());
            SessionCreateParams.ShippingAddressCollection shippingAddressCollection = SessionCreateParams.ShippingAddressCollection.builder().addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.MY).build();
            Stripe.apiKey = "sk_test_51P65BrFk9wrYJLjb9wn0Wz06J0yv61bvL7BYlYYOffKHDlri52WgMj864z2Lznbj6ytj4qTH4PQhkfx3fRED9OWb00z29Lnjd0";
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPrice(price.getId())
                                            .build())
                            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                            .setShippingAddressCollection(shippingAddressCollection)
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:8080/#/pay/success?session_id={CHECKOUT_SESSION_ID}&item_id="+item.getId())
                            .setCancelUrl("http://localhost:8080/#/pay/success?session_id={CHECKOUT_SESSION_ID}&item_id="+item.getId())
                            .build();
            Session session = Session.create(params);

            System.out.println(session.getSuccessUrl());
            System.out.println(session.getId());
            System.out.println("Session url: " + session.getUrl());


            PaymentLinkCreateParams linkParams =
                    PaymentLinkCreateParams.builder()
                            .addLineItem(
                                    PaymentLinkCreateParams.LineItem.builder()
                                            .setPrice(price.getId())
                                            .setQuantity(1L)
                                            .build()
                            )
                            .setRestrictions(
                                    PaymentLinkCreateParams.Restrictions.builder()
                                            .setCompletedSessions(
                                                    PaymentLinkCreateParams.Restrictions.CompletedSessions.builder()
                                                            .setLimit(1L)
                                                            .build()
                                            )
                                            .build()
                            )
                            .setInactiveMessage("Sorry, you already paid!")
                            .build();

            PaymentLink paymentLink = PaymentLink.create(linkParams);
            System.out.println(paymentLink.getUrl());
            item.setPayLink(session.getUrl());
            iItemService.updateById(item);



            return ApiResult.success("Set Expire Success");
        }

    }
}
