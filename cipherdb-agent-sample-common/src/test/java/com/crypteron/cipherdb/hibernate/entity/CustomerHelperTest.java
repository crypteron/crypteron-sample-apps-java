package com.crypteron.cipherdb.hibernate.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.crypteron.cipherdb.entity.Customer;
import com.crypteron.cipherdb.entity.CustomerHelper;

public class CustomerHelperTest {
  private Customer randomCustomer;

  @Before
  public void before() {
    this.randomCustomer = CustomerHelper.randomCustomer();
  }

  @Test
  public void testCustomerHasName() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getCustomerName());
  }

  @Test
  public void testCustomerHasOrderItem() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getOrderItem());
  }

  @Test
  public void testCustomerHasTimestamp() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getTimestamp());
  }

  @Test
  public void testCustomerHasCreditCardNumber() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getSecureCreditCardNumber());
  }

  @Test
  public void testCustomerHasSocial() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getSecureSocialSecurityNumber());
  }

  @Test
  public void testCustomerHasPin() throws Exception {
    Assert.assertNotNull(this.randomCustomer.getSecureLegacyPin());
  }

}
