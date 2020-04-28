package com.crypteron.cipherdb.entity;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.crypteron.Opt;
import com.crypteron.Secure;

@Entity
@Table(name = "Customer")
public class Customer {
  @Id
  @Column(name = "OrderId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long   orderId;

  @Secure(opts = Opt.SEARCH)
  @Column(name = "CustomerName")
  private String customerName;

  @Column(name = "OrderItem")
  private String orderItem;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "Timestamp")
  private Date   timestamp;

  @Column(name = "Secure_CreditCardNumber")
  private String secureCreditCardNumber;

  @Column(name = "Secure_SocialSecurityNumber")
  private byte[] secureSocialSecurityNumber;

  @Column(name = "Secure_LegacyPIN")
  private String secureLegacyPin;

  public Long getOrderId() {
    return this.orderId;
  }

  public void setOrderId(final Long id) {
    this.orderId = id;
  }

  public String getCustomerName() {
    return this.customerName;
  }

  public void setCustomerName(final String customerName) {
    this.customerName = customerName;
  }

  public String getOrderItem() {
    return this.orderItem;
  }

  public void setOrderItem(final String orderItem) {
    this.orderItem = orderItem;
  }

  public Date getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(final Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getSecureCreditCardNumber() {
    return this.secureCreditCardNumber;
  }

  public void setSecureCreditCardNumber(final String secureCreditCardNumber) {
    this.secureCreditCardNumber = secureCreditCardNumber;
  }

  public byte[] getSecureSocialSecurityNumber() {
    return this.secureSocialSecurityNumber;
  }

  public void setSecureSocialSecurityNumber(final byte[] secureSocialSecurityNumber) {
    this.secureSocialSecurityNumber = secureSocialSecurityNumber;
  }

  public String getSecureLegacyPin() {
    return this.secureLegacyPin;
  }

  public void setSecureLegacyPin(final String secureLegacyPin) {
    this.secureLegacyPin = secureLegacyPin;
  }

  @Override
  public String toString() {
    return "Customer [orderId=" + this.orderId + ", customerName=" + this.customerName + ", orderItem=" + this.orderItem
        + ", timestamp=" + this.timestamp + ", secureCreditCardNumber=" + this.secureCreditCardNumber
        + ", secureSocialSecurityNumber=" + Arrays.toString(this.secureSocialSecurityNumber) + ", secureLegacyPin="
        + this.secureLegacyPin + "]";
  }

}