package com.crypteron.cipherdb.entity;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.crypteron.commons.utility.RandomUtil;

public class CustomerHelper {

  private CustomerHelper() {
  }

  public static final Customer randomCustomer() {
    final String ssn = RandomUtil.randomSSN();
    final Customer randomCustomer = new Customer();
    randomCustomer.setCustomerName(RandomUtil.randomName());
    randomCustomer.setOrderItem(RandomUtil.randomItem());
    randomCustomer.setTimestamp(new Date());
    randomCustomer.setSecureCreditCardNumber(RandomUtil.randomCC());
    randomCustomer.setSecureSocialSecurityNumber(ssn.getBytes(StandardCharsets.UTF_8));
    randomCustomer.setSecureLegacyPin(RandomUtil.randomPIN());
    return randomCustomer;
  }
}
